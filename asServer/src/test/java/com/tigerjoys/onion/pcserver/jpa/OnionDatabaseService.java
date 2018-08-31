package com.tigerjoys.onion.pcserver.jpa;

import com.tigerjoys.extension.jpa.code.databases.AbstractDataBase;

/**
 * 刷量数据库服务
 *
 * @author chengang
 */
public class OnionDatabaseService extends AbstractDataBase {

    public OnionDatabaseService() {
        super("test","root","root","127.0.0.1",3306);
    }

    @Override
    public String getPackageName() {
        return "com.tigerjoys.onion.pcserver.inter";
    }

}

