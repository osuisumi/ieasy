package com.ieasy.basic.util.dbutil;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Component;

@Component("dbutil")
public class DBUtilsHelper implements IDBUtilsHelper {

	private DataSource ds = null;
	private QueryRunner qr = null;

	public DBUtilsHelper() {
		try {
			this.ds = DBConnectPool.getInstance().getDataSource();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (this.ds != null) {
			this.qr = new QueryRunner(this.ds);
		}
	}

	public DBUtilsHelper(DataSource ds) {
		this.ds = ds;
		this.qr = new QueryRunner(this.ds);
	}

	public String getDataBase() {
		String database = null;
		try {
			Connection conn = this.ds.getConnection();
			database = conn.getCatalog();
			conn.close();
			return database;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public QueryRunner getQr() {
		return this.qr;
	}


	public static void main(String[] args) {
		DBUtilsHelper d = new DBUtilsHelper();
		System.out.println(d.getDataBase());
		
		/*Object[] param = new Object[]{"zhangyi"} ;
		try {
			String sql2 = "select t.*, p.* from ieasy_sys_user t inner join ieasy_org_emp p on(t.emp_id=p.id) where t.account=?" ;
			long s = System.currentTimeMillis() ;
			List<UserForm> query = d.getQr().query(sql2, new BeanListHandler<UserForm>(UserForm.class), param) ;
			System.out.println(query.size() + "记录");
			for (UserForm p : query) {
				System.out.println(p);
			}
			long e = System.currentTimeMillis() ;
			System.out.println(e-s);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
}
