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
		
		YYConnPool.getInstance().initConnPools(null);
		
		YYConn conn = new YYConn("parox");
		try {
			conn.prepareStatement("insert into test_api(url,gmt_create,gmt_update) values(?,now(),now())");
			conn.setString(1, "http://www.google.com");
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
