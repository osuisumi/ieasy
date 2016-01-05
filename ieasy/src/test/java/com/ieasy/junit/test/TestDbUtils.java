package com.ieasy.junit.test;

import java.beans.IntrospectionException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.inject.Inject;

import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ieasy.basic.util.dbutil.DBConnectPool;
import com.ieasy.basic.util.dbutil.DBUtilsHelper;
import com.ieasy.junit.BasicJunitTest;
import com.ieasy.module.system.web.form.UserForm;

public class TestDbUtils extends BasicJunitTest {

	@Inject
	private DBUtilsHelper db;

	@Test
	public void testDBStatus() throws SQLException {
		DruidDataSource ds = DBConnectPool.getInstance().getDataSource() ;
		for (int i = 0; i < 50; i++) {
			DruidPooledConnection c = ds.getConnection() ;
			System.out.println(i+"=="+c);
			c.close() ;
			System.out.println(ds.getMaxActive()+","+ds.getActiveCount());
		}
		
		/*for (int i = 0; i < 1150; i++) {
			System.out.println(i+"=="+this.db.getDataBase());
		}*/
		
		
	}
	
	
	@Test
	public void testInsertWithBeanBatch() throws SQLException, IntrospectionException {
		String sql = "insert user(name,password) values(?,?)";
		QueryRunner query = db.getQr();
		PreparedStatement ps = DBConnectPool.getInstance().getConnection()
				.prepareStatement(sql);

		UserForm user = new UserForm();
		user.setName("fansofjava");
		user.setPassword("2012");

		UserForm user2 = new UserForm();
		user2.setName("fansofjava");
		user2.setPassword("2013");

		String[] propertyName = { "name", "password" };
		query.fillStatementWithBean(ps, user, propertyName);
		ps.addBatch();
		query.fillStatementWithBean(ps, user2, propertyName);
		ps.addBatch();
		ps.executeBatch();
	}
	
	/*public void testDBUtilSelect() {  
        Connection conn = DbManager.getInstance().getConnection();  
  
        QueryRunner queryRunner = new QueryRunner();  
  
        try {  
            // 返回单行记录，使用Map  
            System.out.println("使用Map处理单行记录！");  
            Map<String, Object> map = queryRunner.query(conn,  
                    "select * from tab where rownum=1", new MapHandler(),  
                    (Object[]) null);  
  
            for (Iterator<Entry<String, Object>> i = map.entrySet().iterator(); i  
                    .hasNext();) {  
                Entry<String, Object> e = i.next();  
                System.out.println(e.getKey() + "=" + e.getValue());  
            }  
  
            System.out.println("处理多行记录！");  
            List<Map<String, Object>> list = queryRunner.query(conn,  
                    "select * from tab where rownum<=3", new MapListHandler(),  
                    (Object[]) null);  
  
            for (Iterator<Map<String, Object>> li = list.iterator(); li  
                    .hasNext();) {  
                System.out.println("--------------");  
                Map<String, Object> m = li.next();  
                for (Iterator<Entry<String, Object>> mi = m.entrySet()  
                        .iterator(); mi.hasNext();) {  
                    Entry<String, Object> e = mi.next();  
                    System.out.println(e.getKey() + "=" + e.getValue());  
                }  
            }  
  
            System.out.println("使用Bean处理单行记录！");  
  
            // com.exam.test.TestSomething.Tab  
            Tab tab = queryRunner.query(conn,  
                    "select tname from tab where rownum=1",  
                    new BeanHandler<Tab>(Tab.class));  
            System.out.println("tname=" + tab.getTname());  
            System.out.println("tabtype=" + tab.getTabtype());  
  
            System.out.println("使用Array处理单行记录！");  
            Object[] array = queryRunner.query(conn,  
                    "select * from tab where rownum=1", new ArrayHandler());  
  
            for (int i = 0; i < array.length; i++) {  
                System.out.println(array[i]);  
            }  
  
            System.out.println("使用Array处理多行记录！");  
            List<Object[]> arraylist = queryRunner  
                    .query(conn, "select * from tab where rownum<=3",  
                            new ArrayListHandler());  
  
            for (Iterator<Object[]> itr = arraylist.iterator(); itr.hasNext();) {  
                Object[] a = itr.next();  
                System.out.println("--------------");  
                for (int i = 0; i < a.length; i++) {  
                    System.out.println(a[i]);  
  
                }  
            }  
  
            System.out.println("使用ColumnListHandler处理单行记录，返回其中指定的一列！");  
            List<Object> colList = queryRunner.query(conn,  
                    "select * from tab where rownum=1", new ColumnListHandler(  
                            "tname"));  
            for (Iterator<Object> itr = colList.iterator(); itr.hasNext();) {  
                System.out.println(itr.next());  
            }  
  
            System.out  
                    .println("使用ScalarHandler处理单行记录，只返回结果集第一行中的指定字段，如未指定字段，则返回第一个字段！");  
            Object scalar1 = queryRunner.query(conn, "select * from tab",  
                    new ScalarHandler("tname"));  
            System.out.println(scalar1);  
            Object scalar2 = queryRunner.query(conn,  
                    "select tname,tabtype from tab", new ScalarHandler());  
            System.out.println(scalar2);  
  
            // 使用自定义的行处理器  
            // Map中的KEY可按输入顺序输出  
            System.out.println("使用Map处理单行记录(使用自定义行处理器)！");  
            Map<String, Object> linkedmap = queryRunner  
                    .query(  
                            conn,  
                            "select tabtype,tname,'wallimn' as programmer from tab where rownum=1",  
                            new MapHandler(new BasicRowProcessorEx()),  
                            (Object[]) null);  
  
            for (Iterator<Entry<String, Object>> i = linkedmap.entrySet()  
                    .iterator(); i.hasNext();) {  
                Entry<String, Object> e = i.next();  
                System.out.println(e.getKey() + "=" + e.getValue());  
            }  
  
            // 使用自定义的行处理器  
            // Map中的KEY可按输入顺序输出  
            System.out.println("处理多行记录(使用自定义行处理器)！");  
            List<Map<String, Object>> listLinedMap = queryRunner  
                    .query(  
                            conn,  
                            "select tabtype,tname,'wallimn' as programmer from tab where rownum<=3",  
                            new MapListHandler(new BasicRowProcessorEx()),  
                            (Object[]) null);  
  
            for (Iterator<Map<String, Object>> li = listLinedMap.iterator(); li  
                    .hasNext();) {  
                System.out.println("--------------");  
                Map<String, Object> m = li.next();  
                for (Iterator<Entry<String, Object>> mi = m.entrySet()  
                        .iterator(); mi.hasNext();) {  
                    Entry<String, Object> e = mi.next();  
                    System.out.println(e.getKey() + "=" + e.getValue());  
                }  
            }  
        } catch (SQLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        DbUtils.closeQuietly(conn);  
    }  
  
    public void testDBUtilInsertDeleteUpdateSelect() {  
        // 建一个简单的测试表，建表脚本如下  
        // create table T_DBUTILTEST(  
        // id integer,  
        // name varchar2(255)  
        // );  
        Connection conn = DbManager.getInstance().getConnection();  
  
        QueryRunner queryRunner = new QueryRunner(true);  
  
        try {  
            queryRunner.update(conn, "delete from T_DBUTILTEST");  
            // queryRunner.update(conn, "truncate table T_DBUTILTEST");  
            // 插一条  
            for (int i = 0; i < 10; i++) {  
                queryRunner.update(conn,  
                        "insert into T_DBUTILTEST (id,name) values (?,?)", i,  
                        "http://wallimn.iteye.com");  
            }  
  
            // 再插多条  
            queryRunner.batch(conn,  
                    "insert into T_DBUTILTEST (id,name) values (?,?)",  
                    new Object[][] { { 11, "batch:wallimn@sohu.com" },  
                            { 12, "batch:wallimn@sohu.com" } });  
  
            // 删除示例  
            queryRunner.update(conn, "delete from T_DBUTILTEST where id=1");  
            queryRunner.update(conn, "delete from T_DBUTILTEST where id=?", 2);  
            queryRunner.batch(conn, "delete from T_DBUTILTEST where id=?",  
                    new Object[][] { { 3 }, { 4 } });  
  
            // 修改示例  
            queryRunner.update(conn,  
                    "update T_DBUTILTEST set name = ? where id=?", "修改后的新值", 5);  
  
            System.out.println("最终结果显示结果");  
            List<Map<String, Object>> list = queryRunner.query(conn,  
                    "select name,id from T_DBUTILTEST", new MapListHandler(),  
                    (Object[]) null);  
  
            for (Iterator<Map<String, Object>> li = list.iterator(); li  
                    .hasNext();) {  
                System.out.println("--------------");  
                Map<String, Object> m = li.next();  
                for (Iterator<Entry<String, Object>> mi = m.entrySet()  
                        .iterator(); mi.hasNext();) {  
                    Entry<String, Object> e = mi.next();  
                    System.out.print(e.getValue());  
                    System.out.print(",");  
                }  
                System.out.println();  
            }  
        } catch (SQLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        DbUtils.closeQuietly(conn);  
    }  */

}
