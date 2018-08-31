package com.tigerjoys.onion.pcserver.configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.tigerjoys.extension.jpa.JPACustomJpaRepositoryFactoryBean;
import com.tigerjoys.onion.pcserver.constant.Constant;

/**
 * 解析配置获取Datasource
 * @author chengang
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.tigerjoys.onion.pcserver.inter",repositoryFactoryBeanClass=JPACustomJpaRepositoryFactoryBean.class)
@EnableTransactionManagement
public class DruidDataSourceConfiguration implements EnvironmentAware {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceConfiguration.class);

	private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
    }
    
    /**
     * 生成DruidDataSource数据源
     * @return DataSource
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
	@Bean(destroyMethod = "close", initMethod = "init")
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource writeDataSource() throws SQLException {
    	LOGGER.info("注入druid！！！");
        
        DruidDataSource datasource = new DruidDataSource();
        
        datasource.setUrl(propertyResolver.getProperty("url"));
        datasource.setUsername(propertyResolver.getProperty("username"));
        datasource.setPassword(propertyResolver.getProperty("password"));
        datasource.setDriverClassName(propertyResolver.getProperty("driverClassName"));
        datasource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("maxActive")));
        datasource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("minIdle")));
        datasource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initialSize")));
        datasource.setMaxWait(Long.valueOf(propertyResolver.getProperty("maxWait")));
        datasource.setTimeBetweenEvictionRunsMillis(Long.valueOf(propertyResolver.getProperty("timeBetweenEvictionRunsMillis")));
        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(propertyResolver.getProperty("minEvictableIdleTimeMillis")));
        datasource.setConnectionProperties(propertyResolver.getProperty("connectionProperties"));
        datasource.setValidationQuery(propertyResolver.getProperty("validationQuery"));
        datasource.setConnectionInitSqls(propertyResolver.getProperty("connectionInitSqls", List.class));
        datasource.setTestWhileIdle(Boolean.valueOf(propertyResolver.getProperty("testWhileIdle")));
        datasource.setTestOnBorrow(Boolean.valueOf(propertyResolver.getProperty("testOnBorrow")));
        datasource.setTestOnReturn(Boolean.valueOf(propertyResolver.getProperty("testOnReturn")));
        
        List<Filter> filters = new ArrayList<>(2);
        //测试环境需要打印SQL查询
        if(Constant.IS_TEST) {
        	filters.add(logFilter());
        }
        //SQL状态
    	filters.add(statFilter());
    	//SQL防火墙
    	filters.add(wallFilter(datasource));
        
        //记录日志，SQL状态,wall等
        datasource.setProxyFilters(filters);
        
        return datasource;
    }
    
    /**
     * Druid log filter设置
     * @return Slf4jLogFilter
     */
    @Bean
    public Slf4jLogFilter logFilter() {
    	Slf4jLogFilter filter = new Slf4jLogFilter();
    	filter.setConnectionLogEnabled(false);
    	filter.setStatementLogEnabled(true);
    	filter.setResultSetLogEnabled(false);
    	filter.setStatementExecutableSqlLogEnable(false);
    	
    	return filter;
    }
    
    /**
     * Druid stat filter设置
     * @return StatFilter
     */
    @Bean
    public StatFilter statFilter() {
    	StatFilter filter = new StatFilter();
    	//合并sql 对相似sql归并到一个sql进行检测统计
    	filter.setMergeSql(Boolean.valueOf(propertyResolver.getProperty("logSlowSql" , "false")));
    	//慢查询的毫秒数
    	filter.setSlowSqlMillis(Long.valueOf(propertyResolver.getProperty("slowSqlMillis" , "1000")));
    	//是否打印慢查询
    	filter.setLogSlowSql(Boolean.valueOf(propertyResolver.getProperty("mergeSql" , "false")));
    	
    	return filter;
    }
    
    /**
     * SQL wall Filter设置
     * @return WallFilter
     */
    @Bean
    public WallFilter wallFilter(DataSourceProxy dataSource) {
    	WallFilter filter = new WallFilter();
    	filter.init(dataSource);
    	//初始化完成后获得WallConfig
    	
    	//因为如果配置@Bean WallConfig有一个问题，就是dbType得写死mysql后者oracle等，不优雅。
    	WallConfig config = filter.getConfig();
    	//SELECT查询中是否允许INTO字句
    	config.setSelectIntoAllow(false);
    	//truncate语句是危险，缺省打开，若需要自行关闭
    	config.setTruncateAllow(false);
    	//是否允许执行Alter Table语句
    	config.setAlterTableAllow(false);
    	//是否允许删除表
    	config.setDropTableAllow(false);
    	//是否允许执行mysql的use语句，缺省打开
    	config.setUseAllow(false);
    	//是否允许执行mysql的describe语句，缺省打开
    	config.setDescribeAllow(false);
    	//是否允许执行mysql的show语句，缺省打开
    	config.setShowAllow(false);
    	//检查DELETE语句是否无where条件，这是有风险的，但不是SQL注入类型的风险
    	config.setDeleteWhereNoneCheck(true);
    	//检查UPDATE语句是否无where条件，这是有风险的，但不是SQL注入类型的风险
    	config.setUpdateWhereNoneCheck(true);
    	//是否允许调用Connection.getMetadata方法，这个方法调用会暴露数据库的表信息[JPA不能禁用]
    	//config.setMetadataAllow(false);
    	//是否允许调用Connection/Statement/ResultSet的isWrapFor和unwrap方法，这两个方法调用，使得有办法拿到原生驱动的对象，绕过WallFilter的检测直接执行SQL。
    	config.setWrapAllow(false);
    	//批量sql
    	config.setMultiStatementAllow(true);
    	return filter;
    }
    
	/**
	 * 
	 * 注册一个StatViewServlet
	 * @return ServletRegistrationBean
	 */
	@Bean
	public ServletRegistrationBean druidStatViewServle() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/tiger/druid_stat/*");

		// 添加初始化参数：initParams
		// 白名单：
		//servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
		// IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to
		// view this page.
		//servletRegistrationBean.addInitParameter("deny", "192.168.1.73");
		// 登录查看信息的账号密码.
		servletRegistrationBean.addInitParameter("loginUsername", "tiger");
		servletRegistrationBean.addInitParameter("loginPassword", "tiger1205");
		// 是否能够重置数据.
		servletRegistrationBean.addInitParameter("resetEnable", "true");

		return servletRegistrationBean;
	}
	
	/**
     * 注册一个：filterRegistrationBean
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter(){
       FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

       //添加过滤规则.
       filterRegistrationBean.addUrlPatterns("/*");
       //添加不需要忽略的格式信息.
       filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");

       return filterRegistrationBean;
    }

}
