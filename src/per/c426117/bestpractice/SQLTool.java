package per.c426117.bestpractice;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class SQLTool //完全不好用的东西啊喂 (-｡-;)
{
	public ResultSet resultSet = null;
	
	public Statement stat = null;
	
	public PreparedStatement pstat = null;
	
	public 	Connection conn = null;
	
	public  String[] items = null;
	
	public  boolean connectLocalSQL (String dataBaseName,String userName ,String passWord)
	{
		String dbdr = "org.gjt.mm.mysql.Driver";//加载数据库驱动
		String dbulr = "jdbc:mysql://localhost:3306/"+dataBaseName;//数据库协议：数据库类型：//ip:端口号/数据库名字 以此来制定使用的数据库
		try
		{
			Class.forName(dbdr);
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
				
		try
		{
			conn = (Connection)DriverManager.getConnection(dbulr,userName,passWord);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public  boolean close ()
	{
		try
		{
			conn.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//设置数据项
	public  boolean setItem (String[] items)
	{
		this.items = new String[items.length];
		for(int a = 0;a!=items.length;a++)
		{
			this.items[a]=items[a];
		}
		
		return true;
	}
	//插入数据
	public boolean insertData (String tableName)
	{
		String sql = "INSERT INTO "+tableName+"(";
		for(int i = 0;i!=items.length;i++)
		{
			sql=sql+items[i];
			if(i!=(items.length-1))
			{
				sql = sql + ",";
			}
		}
		sql = sql+")VALUES(";
		for(int i = 0;i!=items.length;i++)
		{
			sql=sql+"?";
			if(i!=(items.length-1))
			{
				sql = sql + ",";
			}
		} 
		sql = sql+")";
		try
		{
			pstat = (PreparedStatement)conn.prepareStatement(sql);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public java.sql.Date dateToDate (String date)
	{
		java.util.Date temp = null;
		try
		{
			temp = new SimpleDateFormat("yyyyMMdd").parse(date);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		java.sql.Date bir = new java.sql.Date(temp.getTime());
		return bir;
	}
	
}
