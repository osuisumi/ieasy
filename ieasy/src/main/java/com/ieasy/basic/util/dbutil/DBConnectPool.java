package com.ieasy.basic.util.dbutil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

public class DBConnectPool {

	private static DBConnectPool databasePool = null;
	private static DruidDataSource dds = null;
	static {
		Properties properties = loadPropertyFile("db.properties");
		try {
			dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
			dds.getConnection() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DBConnectPool() {
	}

	public static synchronized DBConnectPool getInstance() {
		if (null == databasePool) {
			databasePool = new DBConnectPool();
		}
		return databasePool;
	}

	public DruidDataSource getDataSource() throws SQLException {
		return dds;
	}

	public DruidPooledConnection getConnection() throws SQLException {
		return dds.getConnection();
	}

	public static Properties loadPropertyFile(String fullFile) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream resourceAsStream = loader.getResourceAsStream("config/"+fullFile);
			Properties p = System.getProperties() ;
			p.load(resourceAsStream) ;
			return p;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}