/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-10-22
 */
package net.caiban.db;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class YYConnTest {

	/**
	 * Test method for {@link net.caiban.db.YYConn#init(java.lang.String)}.
	 */
	@Test
	public void testInitString() {
		YYConn conn = new YYConn("yyeasytest");
		try {
			conn.prepareStatement("insert into eshop(name,site_url,remark,gmtcreate,domain) values(?,?,?,now(),now())");
			conn.setString(1, "网店名称，测试");
			conn.setString(2, "http://www.google.com");
			conn.setString(3, "here is a remark");
			int i = conn.executeUpdate();
			assertEquals(1, i);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			conn.close();
		}
		YYConnPool.getInstance().destoryConnectionPools();
	}

}
