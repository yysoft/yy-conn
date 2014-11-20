/**
 * Copyright 2010 YYSoft
 * All right reserved.
 * Created on 2010-10-18
 */
package net.caiban.db;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Clob;

/**
 * @author Mays (x03570227@gmail.com)
 *
 */
public class YYConn {
	
	private Logger LOG = Logger.getLogger(YYConn.class);
	
	private Statement stmt;
	private PreparedStatement prepStmt;
	private Connection conn;
	private String dbName;
	private Boolean autoCommit;
	
	final static boolean DEFAULT_COMMIT = true;

	public YYConn(String db){
		init(db,DEFAULT_COMMIT);
	}
	
	public YYConn(String db, boolean autoCommit){
		init(db,autoCommit);
	}
	
	public YYConn(){
		
	}
	
	public YYConn(boolean autoCommit){
		init(null, autoCommit);
	}
	
	public void init(String db){
		init(db, DEFAULT_COMMIT);
	}
	
	public void init(String db, boolean autoCommit){
		LOG.debug("正在获取数据库连接...");
		this.dbName = db;
		this.autoCommit = autoCommit;
		conn = YYConnPool.getInstance().getConnection(db);
		try {
			stmt = conn.createStatement();
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			LOG.error("无法创建statement",e);
		}
	}
	
	public Connection getConnection(){
		return conn;
	}
	
	public Statement getStatement(){
		return stmt;
	}
	
	public PreparedStatement getPreparedStatement(){
		return prepStmt;
	}
	
	public void setTransactionIsolation(int level) throws SQLException{
		if(conn!=null){
			conn.setTransactionIsolation(level);
		}
	}
	
	public void commit() throws SQLException{
		if(conn!=null){
			conn.commit();
		}
	}
	
	public void rollback() throws SQLException{
		if(conn!=null){
			conn.rollback();
		}
	}
	
	public void prepareStatement(String sql) throws SQLException{
		prepStmt = conn.prepareStatement(sql);
	}
	
	public void prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException{
		prepStmt = conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}
	
	public void setString(int i, String s) throws SQLException{
		prepStmt.setString(i, s);
	}
	
	public void setInt(int i, int j) throws SQLException{
		prepStmt.setInt(i, j);
	}
	
	public void setBoolean(int i, boolean b) throws SQLException{
		prepStmt.setBoolean(i, b);
	}
	
	public void setDate(int i, Date date) throws SQLException{
		prepStmt.setDate(i, date);
	}
	
	public void setTimestamp(int i, Timestamp timestamp) throws SQLException{
		prepStmt.setTimestamp(i, timestamp);
	}
	
	public void setTimestamp(int i, Timestamp timestamp, Calendar calendar) throws SQLException{
		prepStmt.setTimestamp(i, timestamp, calendar);
	}
	
	public void setShort(int i, short s) throws SQLException{
		prepStmt.setShort(0, s);
	}
	
	public void setLong(int i, long l) throws SQLException{
		prepStmt.setLong(i, l);
	}
	
	public void setFloat(int i, float f) throws SQLException{
		prepStmt.setFloat(i, f);
	}
	
	public void setBlob(int i, Blob blob) throws SQLException{
		prepStmt.setBlob(i, blob);
	}
	
	public void setClob(int i, Clob clob) throws SQLException{
		prepStmt.setClob(i, clob);
	}
	
	public void setObject(int i, Object obj) throws SQLException{
		prepStmt.setObject(i, obj);
	}
	
	public void setArray(int i, Array arr) throws SQLException {
		prepStmt.setArray(i, arr);
	}
	
	public void setRef(int i, Ref ref) throws SQLException {
		prepStmt.setRef(i, ref);
	}

	public void setAsciiStream(int i, InputStream in, int length)
			throws SQLException {
		prepStmt.setAsciiStream(i, in, length);
	}
	
	public void setBigDecimal(int i, BigDecimal x) throws SQLException {
		prepStmt.setBigDecimal(i, x);
	}

	public void setByte(int i, byte x) throws SQLException {
		prepStmt.setByte(i, x);
	}

	public void setNull(int i, int sqlType) throws SQLException {
		prepStmt.setNull(i, sqlType);
	}

	public void setDouble(int i, double d) throws SQLException {
		prepStmt.setDouble(i, d);
	}

	public void setBytes(int i, byte abyte0[]) throws SQLException {
		prepStmt.setBytes(i, abyte0);
	}
	
	public void clearParameters() throws SQLException {
		prepStmt.clearParameters();
	}

	public void clearBatch() throws SQLException {
		prepStmt.clearBatch();
	}
	
	public void addBatch() throws SQLException {
		if (prepStmt != null){
			prepStmt.addBatch();
		}
	}

	public void addBatch(String s) throws SQLException {
		if (prepStmt != null){
			prepStmt.addBatch(s);
		}
	}
	
	public ResultSet executeQuery(String s) throws SQLException {
		if (stmt != null){
			long currTime = System.currentTimeMillis();
			ResultSet rs = null;
			try {
				LOG.debug("正在执行查询: "+s);
				rs = stmt.executeQuery(s);
			}catch (SQLException e) {
				LOG.error("执行查询发生错误", e);
			}finally{
				long currTime2 = System.currentTimeMillis();
				LOG.debug("执行时间："+(currTime2-currTime)+"ms  数据库:"+dbName);
			}
			return rs;
		}
		
		return null;
	}

	public ResultSet executeQuery() throws SQLException {
		if (prepStmt != null){
			long currTime = System.currentTimeMillis();
			ResultSet rs = null;
			try {
				LOG.debug("正在执行查询...");
				rs = prepStmt.executeQuery();
			}catch (SQLException e) {
				LOG.error("执行查询发生错误", e);
			}finally{
				long currTime2 = System.currentTimeMillis();
				LOG.debug("执行时间："+(currTime2-currTime)+"ms  数据库:"+dbName);
			}
			return rs;
		}
		return null;
	}

	public int[] executeBatch() throws SQLException {
		if (prepStmt != null){
			long currTime = System.currentTimeMillis();
			int[] ret = null;
			try {
				LOG.debug("正在执行批量操作");
				ret = prepStmt.executeBatch();
			}catch (SQLException e) {
				LOG.error("执行查询发生错误", e);
			}
			finally{
				long currTime2 = System.currentTimeMillis();
				LOG.debug("执行时间："+(currTime2-currTime)+"ms  数据库:"+dbName);
			}
			return ret;
		}
		return null;
	}

	public int executeUpdate(String s) throws SQLException {
		if (stmt != null){
			long currTime = System.currentTimeMillis();
			int ret = 0;
			try {
				LOG.debug("正在执行查询："+s);
				ret = stmt.executeUpdate(s);
			}catch (SQLException e) {
				LOG.error("执行查询发生错误", e);
			}
			finally{
				long currTime2 = System.currentTimeMillis();
				LOG.debug("执行时间："+(currTime2-currTime)+"ms  数据库:"+dbName);
			}
			return ret;
		}
		return 0;
	}

	public int executeUpdate() throws SQLException {
		if (prepStmt != null){
			long currTime = System.currentTimeMillis();
			int ret = 0;
			try {
				LOG.debug("正在执行查询...");
				ret = prepStmt.executeUpdate();
			}catch (SQLException e) {
				LOG.error("执行查询发生错误", e);
			}finally{
				long currTime2 = System.currentTimeMillis();
				LOG.debug("执行时间："+(currTime2-currTime)+"ms  数据库:"+dbName);
			}
			return ret;
		}
		return 0;
	}
	
	public void close() {
		try {
			LOG.debug("正在关闭连接...");
			if (prepStmt != null) {
				prepStmt.close();
				prepStmt = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				if (autoCommit == false){
					conn.commit();
				}
				conn.close();
			}
		} catch (SQLException e) {
			LOG.error("关闭连接时发生错误...",e);
		}
	}
}

