package com.leessy.SQL;

import java.sql.*;
import java.util.ArrayList;

public class MySqlUtil {
	//定义MySql JDBC驱动字符串
	private static final String jdbc="com.mysql.jdbc.Driver";
	//定义 MySql 连接数据库URL字符串
	private static final String url="jdbc:mysql://127.0.0.1/";
	//数据库名称
	private static final String datebase="test";
	//用户名
	private static final String user="root";
	//用户密码
	private static final String password="123456";
	//全局连接
	private Connection conn=null;
	//默认打印debug信息
	private boolean debug=true;
	
	/**
	 * 设置是否打印调试信息
	 * @param isDebug
	 */
	public void setDeug(boolean isDebug){
		debug=isDebug;
	}
	
	/**
	 * 打印数据源相关信息
	 */
	public void printDatabaseInfo() {
		try {
			//使用DatabaseMetaData对象获取数据源相关信息
			DatabaseMetaData dm=conn.getMetaData();
			//获得数据源URL
			System.out.println("Data Source URL:"+dm.getURL());
			//获得驱动程序名称
			System.out.println("Driver Name:"+dm.getDriverName());
			//获得驱动程序版本号
			System.out.println("Driver Version:"+dm.getDriverVersion());
			//获得数据源连接的DBMS名称
			System.out.println("DataBase ProductName:"+dm.getDatabaseProductName());
			//获得数据源连接的DBMS版本号
			System.out.println("DataBase ProductVersion:"+dm.getDatabaseProductVersion());
		} catch (SQLException e) {
			System.out.println("printDatabaseInfo error:"+e.getMessage());
		}
	}
	
	/**
	 * 设置连接对象Connection相关属性
	 * @param readOnly
	 * @param autoCommit
	 */
	private void setConnAttrs(boolean readOnly, boolean autoCommit) {
		try {
			conn.setReadOnly(readOnly);
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			System.out.println("setConnAttrs error:"+e.getMessage());
		}
	}
	
	/**
	 * 设置Statement对象相关属性
	 * @param stat
	 * @param maxFieldSize
	 * @param maxRows
	 * @param seconds
	 */
	private void setStatementAttrs(Statement stat, int maxFieldSize, int maxRows, int seconds) {
		try {
			//列最大值
			stat.setMaxFieldSize(maxFieldSize);
			//行最大值
			stat.setMaxRows(maxRows);
			//查询等待时间
			stat.setQueryTimeout(seconds);
		} catch (SQLException e) {
			System.out.println("setStatementAttrs error:"+e.getMessage());
		}
	}
	
	/**
	 * 动态加载MySql驱动
	 * @throws SQLException
	 */
	public void loadJdbcDriver() throws SQLException {
		try {
			if(debug) System.out.println("Loading JDBC Driver...");
			Class.forName(jdbc);
		} catch (ClassNotFoundException e1) {
			throw new SQLException("Load JDBC Driver Error : " + e1.getMessage());
		}catch (Exception e2) {
			throw new SQLException("Load JDBC Driver Error : " + e2.getMessage());
		}
	}
	
	/**
	 * 连接MySql数据库
	 * @throws SQLException
	 */
	public void connect() throws SQLException{
		try {
			if(debug) System.out.println("Connect to MySql Server...");
			conn=DriverManager.getConnection(url+datebase,user,password);
			//设置连接相关属性
			//setConnAttrs(readOnly,autoCommit)
		} catch (SQLException e) {
			throw new SQLException("Connect to MySql Server Error : "+ e.getMessage());
		}
	}
	
	/**
	 * 关闭MySql数据库
	 * @throws SQLException
	 */
	public void disconnect() throws SQLException{
		try {
			if(debug) System.out.println("DisConnect to MySql Server...");
			conn.close();
		} catch (SQLException e) {
			throw new SQLException("DisConnect to MySql Server Error : " + e.getMessage());
		}
	}
	
	/**
	 * 数据库执行操作：insert,update,delete,create table,drop table...
	 * @param sql:数据库执行语句
	 * @throws SQLException
	 */
	public void execSql(String sql) throws SQLException {
		try {
			if(debug) System.out.println("Sql exec cmd: "+sql);
			// 创建语句对象
			//也可以使用PreparedStatement创建语句对象，若数据库支持预编译效率更高，而且可以使用?来代替可变参数，再使用setXXX方法设置参数值
			Statement stat=conn.createStatement();
			//设置Statement相关属性
			//setStatementAttrs(stat,maxFieldSize,maxRows,seconds);
			// 执行语句
			int lineNum=stat.executeUpdate(sql);
			if(debug) System.out.println("影响的行数:"+lineNum);
			// 关闭语句
			stat.close();
		} catch (SQLException e) {
			throw new SQLException("Load excSql Driver Error :"+ e.getMessage());
		}
	}
	
	/**
	 * 从数据库获取数据(此处假设全是String类型)
	 * @param sql
	 * @param colNum：数据的列个数
	 * @return
	 */
	public ArrayList<ArrayList<String>> getStringData(String sql, int colNum) {
		ArrayList<ArrayList<String>> data= new ArrayList<>();
		if(debug) System.out.println("Sql query cmd: "+sql);
		try {
			// 创建语句对象
			Statement stat=conn.createStatement();
			// 执行查询(executeQuery只能执行一条查询返回一个ResultSet对象结果集)
			ResultSet rs=stat.executeQuery(sql);
			//execute方法可以返回多个结果集，至少有一个结果集返回就为true,可以使用getResultSet(),getMoreResultSet(),getUpdateCount()等方法处理结果集
//			stat.execute(sql);
//			rs=stat.getResultSet();
			while(rs.next()){//循环迭代
				ArrayList<String> tmp=new ArrayList<>();
				tmp.clear();
				for(int index=1;index<=colNum;index++){//数据库的列序号是从1开始的
					tmp.add(rs.getString(index));//除了输序列号还可以是列名，getInt,getDouble...针对其他类型
				}
				data.add(tmp);
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			System.out.println("getStringData Error: " + e.getMessage());
		}
		return data;
	}
	
//	public static void main(String[] args) throws SQLException {
//		MySqlUtil demo=new MySqlUtil();
////		demo.setDeug(false);
//		demo.loadJdbcDriver();
//		demo.connect();
////		String insertStr="insert into student values('3','lucy',18)";
////		String delStr="delete from student where id=3;";
////		demo.execSql(delStr);
////		String queryStr="select ID,NAME from student;";
////		ArrayList<ArrayList<String>> date= new ArrayList<>();
////		date=demo.getStringData(queryStr, 2);
////		System.out.println(date);
//		demo.printDatabaseInfo();
//		demo.disconnect();
//	}
}