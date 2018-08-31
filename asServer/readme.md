### 框架
spring boot + JPA + netty4

### JPA实体和DAO自动生成器使用
src/test/java<br/>
com.tigerjoys.onion.pcserver.jpa.JPACodeGenerator<br/>

```java
String[] ss = new String[]{"表名称1","表名称2"};
for(String table : ss) {
	JPAFreeMarkerHandler.makeFiles(table, author, directory ,OnionDatabaseService.class);
}
```

main方法执行即可，自动生成目录在{home}/test目录下，拷贝到项目中即可。

### JPA使用
```java
@Test
public void testSort() {
	/*ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
            .withStringMatcher(StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
            .withIgnoreCase(true) //改变默认大小写忽略方式：忽略大小写
            .withMatcher("address", GenericPropertyMatchers.startsWith()) //地址采用“开始匹配”的方式查询
            .withIgnorePaths("focus");  //忽略属性：是否关注。因为是基本类型，需要忽略掉
     */
	
	//agencyRepository.findByUsername("阿萨德");
	
	//agencyManagerLogRepository.findAll(spec);
	
	/*Junction cc = Restrictions.disjunction();
	cc.add(Restrictions.eq("agencyId", 2));
	cc.add(Restrictions.eq("agencyId", 3));*/
	
	JPACustomQuery query = advAduitLogRepository.createCustomQuery(1, 1)
			/*.eq("agencyId", 1)
			.gt("createTime", new Date())
			.ne("agencyId", 1)
			.isNotNull("agencyId")
			.isNull("createTime")*/
			//.like("remark", "登").ilike("remark", "录", LikeMatchMode.END)
			//.not(Restrictions.in("agencyId", new Integer[] {1}))
			//.eqOrIsNull("agencyId", 1)
			//.neOrIsNotNull("agencyId", null)
			//.between("agencyId", 1, 10)
			//.eqProperty("agencyId", "type")
			//.neProperty("agencyId", "type")
			//.ltProperty("agencyId", "type")
			//.leProperty("agencyId", "type")
			//.gtProperty("agencyId", "type")
			//.geProperty("agencyId", "type")
			//.and(
			//	Restrictions.or(Restrictions.eq("agencyId", 2) , Restrictions.eq("agencyId", 2)), 
			//	Restrictions.or(Restrictions.eq("agencyId", 2) , Restrictions.eq("agencyId", 2))
			//)
			/*.conjunction(Restrictions.or(
					Restrictions.and(Restrictions.eq("agencyId", 2) , Restrictions.eq("agencyId", 2)), 
					Restrictions.and(Restrictions.eq("agencyId", 2) , Restrictions.eq("agencyId", 2))
				),
				Restrictions.gt("agencyId", 0)
			)*/
			//.conjunction(Restrictions.or(Restrictions.eq("agencyId", 2) , Restrictions.eq("agencyId", 2)) , cc)
			//.not(Restrictions.locate("remark", "登", '+'))
			//.locate("remark", "登", '+')
			.alwaysTrue()
			;
	
	//List<AdvAduitLogEntity> logList = advAduitLogRepository.findAll(query);
	//System.err.println(JsonHelper.toJson(logList));
	
	//CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	//CriteriaQuery<AgencyManagerLogEntity> criteriaQuery = criteriaBuilder.createQuery(AgencyManagerLogEntity.class);
	//Root<AgencyManagerLogEntity> adminRoot = criteriaQuery.from(AgencyManagerLogEntity.class);
	
	//Pageable pageable= new PageRequest(1, 10, SortTools.create().asc("agencyId").desc("id").bindSort());
	//agencyManagerLogRepository.findAll(SortTools.create().asc("agencyId").desc("id").bindSort());
	//agencyManagerLogRepository.findByAgencyId(1,pageable);
	//agencyManagerLogRepository.findByAgencyId(1,SortTools.create().asc("agencyId").desc("id").bindSort());
	
	//agencyManagerLogRepository.findAll(spec, sort);
	//agencyManagerLogRepository.findAll(example);
	
	/*IJPACustomSpecification<AgencyManagerLogEntity> predicate = agencyManagerLogRepository
			.createCustomQuery()
			.equals("agencyId", 1)
			.equals("type", 1)
			.buildAndPredicate();
	
	agencyManagerLogRepository.findAll(predicate);*/
	
	/*IJPACustomCountSpecification<AgencyManagerLogEntity> count = agencyManagerLogRepository.createCustomCountQuery()
		.equals("agencyId", 1)
		.equals("type", 1)
		.buildAndPredicate();
	agencyManagerLogRepository.count(count);*/
			
	/*CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<AgencyManagerLogEntity> criteriaQuery = criteriaBuilder.createQuery(AgencyManagerLogEntity.class);
	Root<AgencyManagerLogEntity> root = criteriaQuery.from(AgencyManagerLogEntity.class);
	
	ParameterExpression<Integer> age = criteriaBuilder.parameter(Integer.class);
	Predicate condition = criteriaBuilder.ge(criteriaBuilder.locate(root.get("agencyId"),"1"), age);
	
	criteriaQuery.where(condition);
	
	TypedQuery<AgencyManagerLogEntity> typeQuery = entityManager.createQuery(criteriaQuery);
	typeQuery.setParameter(age, 1);
	
	List<AgencyManagerLogEntity> sss = typeQuery.getResultList();
	System.err.println(JsonHelper.toJson(sss));*/
	
	/*agencyManagerLogRepository.findAll(new Specification<AgencyManagerLogEntity>() {
		
		@Override
		public Predicate toPredicate(Root<AgencyManagerLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			ParameterExpression<Integer> age = cb.parameter(Integer.class);
			Predicate condition = cb.gt(root.get("agencyId"), age);
			query.where(condition);
			TypedQuery<?> testQuery = entityManager.createQuery(query);
			testQuery.setParameter(age, 24);
			
			return cb.and(
				cb.ge(root.get("agencyId"), 1),
				cb.equal(root.get("type"), 1)
			);
		}
	});*/
}
```

### Netty配置说明
socket.server.enable = true 开启netty服务<br/>
socket.server.port = 9527	netty服务端端口号<br/>
socket.server.basePackages	自动扫描的API接口包，主要用于接收Client端的调用，以及返回Client端需要的数据信息<br/>

### 如何使用
在命令接口包中创建类

```java
@Command
public class TestCommand {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestCommand.class);
	
	@Autowired
	private IServerService serverService;
	
	@CommandMapping("/api/test/{abc}")
	public List<String> test(RequestMessage request , int abc , JSONObject json , TestDto vo){
		LOGGER.info("我进来了此方法！！！");
		LOGGER.info("server receive body : " + request.getBody());
		LOGGER.info("abc = " + abc);
		LOGGER.info("json = " + json.toJSONString());
		LOGGER.info("vo = " + JsonHelper.toJson(json));
		
		ServerEntity ss = serverService.findServer(1000, "192.168.0.20");
		
		List<String> dto = new ArrayList<>();
		dto.add(ss.getIp());
		
		return dto;
	}
	
}
```

参数说明：<br/>
RequestMessage request 	请求对象<br/>
int abc					@CommandMapping注解中指定的{abc}<br/>
JSONObject json			client端的请求JSON参数体转为JSONObject对象<br/>
TestDto vo				client端的请求JSON参数体转为JAVA DTO对象<br/>
<br/>
return List<String>		返回给Client端的对象数据<br/>