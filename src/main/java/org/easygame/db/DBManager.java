package org.easygame.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBManager {
	private static Logger logger = LoggerFactory.getLogger(DBManager.class);
	private static final String configFile = "dbcp.properties";
	private static DataSource dataSource;

	static {
		Properties dbProperties = new Properties();
		try {
			dbProperties.load(DBManager.class.getClassLoader().getResourceAsStream(configFile));
			dataSource = BasicDataSourceFactory.createDataSource(dbProperties);
			Connection conn = getConn();
			DatabaseMetaData mdm = conn.getMetaData();
			logger.info("Connected to " + mdm.getDatabaseProductName() + " " + mdm.getDatabaseProductVersion());
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			logger.error("初始化连接池失败：" + e);
		}
	}

	private DBManager() {
	}

	/**
	 * 获取链接，用完后记得关闭
	 * 
	 * @see {@link DBManager#closeConn(Connection)}
	 * @return
	 */
	public static final Connection getConn() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error("获取数据库连接失败：" + e);
		}
		return conn;
	}

	/**
	 * 关闭连接
	 * 
	 * @param conn
	 *            需要关闭的连接
	 */
	public static void closeConn(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				conn.close();
			}
		} catch (SQLException e) {
			logger.error("关闭数据库连接失败：" + e);
		}
	}

	// test
	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			Connection conn = DBManager.getConn();
			System.out.print(i + "   \n");
			DBManager.closeConn(conn);
		}
		long end = System.currentTimeMillis();
		System.out.println("用时：" + (end - begin));
	}
}
