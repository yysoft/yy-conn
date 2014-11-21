/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-10-20
 */
package net.caiban.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.caiban.db.util.ConnHelper;

import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;

import com.google.common.base.Strings;


/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class YYConnPool {
	
	
	private static YYConnPool _instance =null;
	
	public static synchronized YYConnPool getInstance(){
		if(_instance==null){
			_instance = new YYConnPool();
		}
		return _instance;
	}
	
	private Logger LOG = Logger.getLogger(YYConnPool.class);
//	private String DEFAULT_DB = "defaultdb";
	private String DEFAULT_URL = "jdbc:mysql://127.0.0.1/default?useUnicode=true&characterEncoding=UTF-8";
	private String DEFAULT_DRIVER = "org.gjt.mm.mysql.Driver";
	private String DEFAULT_MAXCONN = "10";
	private String DEFAULT_PROP="db.properties";
	private String DEFAULT_PROP_CHARSET="UTF-8";
	
	/**
	 * 
	 */
	private YYConnPool(){
		
	}
	
	/**
	 * 读取配置文件,并初始化数据库连接池
	 */
	public  void initConnPools(String prop){
		
		prop = Strings.isNullOrEmpty(prop)?DEFAULT_PROP:prop;
		
		LOG.debug("[YYConn] Reading Properties :"+prop+"...");
		
		Map<String, String> props=null;
		try {
			props = ConnHelper.readPropertyFile(prop, DEFAULT_PROP_CHARSET);
		} catch (IOException e) {
			LOG.warn("[YYConn] Can not read properties :"+prop+"");
			e.printStackTrace();
		}
		
		List<String> dblist = ConnHelper.splitToString(props.get("dblist"));
		
		for(String s:dblist){
			registerConnection(s, 
					getProp(props, s+".url", DEFAULT_URL),
					getProp(props, s+".username", null),
					getProp(props, s+".password", null),
					Integer.valueOf(getProp(props, s+".maxconn", DEFAULT_MAXCONN)),
					getProp(props, s+".driver", DEFAULT_DRIVER));
		}
		
	}
	
	private String getProp(Map<String, String> props, String key, String def){
		String v=props.get(key);
		if(Strings.isNullOrEmpty(v)){
			return def;
		}
		return v;
	}
	
	/**
	 * 注册数据库连接池
	 * @param db:数据库名称
	 * @param url:连接用的url
	 * @param username:用户名
	 * @param password:密码
	 * @param maxconn:最大连接数
	 * @param driver:驱动
	 */
	private void registerConnection(String db, String url, String username, String password, int maxconn, String driver){
		//TODO 注册数据库连接池
		LOG.debug("[YYConn] Registing Connection... DB:"+db);
		try {
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
		} catch (ClassNotFoundException e) {
			LOG.error("[YYConn] Can not loading ProxoolDriver.",e);
			e.printStackTrace();
		}
		
		String proxoolURL = "proxool." + db + ":" + driver + ":" + url;
		Properties info = new Properties();
		info.setProperty("proxool.maximun-connection-count", String.valueOf(maxconn));
		info.setProperty("user", username);
		info.setProperty("password", password);
		info.setProperty("proxool", "true");
		
		try {
			LOG.debug("[YYConn] Connecting to DB:"+db);
			ProxoolFacade.registerConnectionPool(proxoolURL, info);
		} catch (ProxoolException e) {
			LOG.error("[YYConn] Can not connect to DB:"+db, e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 得到一个数据库连接
	 * @param db:数据库名称
	 * @return
	 */
	public Connection getConnection(String db){
		LOG.debug("[YYConn] Get connection which connect to DB:"+db);
		Connection connection = null;
		try {
			if(db!=null){
				connection = DriverManager.getConnection("proxool."+db);
			}
		} catch (SQLException e) {
			LOG.error("[YYConn] Can not connect to DB:"+db,e);
		}
		return connection;
	}
	
	final static int DESTORY_DELAY = 5000;
	
	/**
	 * 关闭数据库连接
	 */
	public void destoryConnectionPools(){
		LOG.debug("[YYConn] Connection Pool will destory in "+(DESTORY_DELAY/1000)+" seconds.");
		ProxoolFacade.shutdown(DESTORY_DELAY);
	}
	
}
