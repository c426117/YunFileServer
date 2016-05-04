package per.c426117.bestpractice;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.mysql.jdbc.Statement;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class StringThread implements Runnable 
{
	private Socket client = null;
	private Socket fileClient = null;
	private String userName = null;//使用此线程的用户
	private String passWord = null;
	private SQLTool sqlTool = null;
	private DataInputStream din = null;
	
	public StringThread(Socket client,Socket fileclient) 
	{
		this.client = client;
		this.fileClient = fileclient;
		//数据库操作类
		sqlTool = new SQLTool();
	}
	@Override
	public void run() 
	{
		
		BufferedReader in = null;
		PrintStream out = null;
		String com = null;
		try
		{
			din = new DataInputStream(fileClient.getInputStream());
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintStream(client.getOutputStream());
			while(true)
			{
				com = in.readLine();
				if(Signal.SIGNUP.equals(com))//注册
				{
					userName = in.readLine();//获取用户名
					String filename = userName+"signupfile.xls";
					
					int len = Integer.parseInt(in.readLine());
					Thread th_excel = new Thread(new FileThread(din,userName,filename,len));
					th_excel.start();
					th_excel.join();
					len = Integer.parseInt(in.readLine());
					Thread th_personal = new Thread(new FileThread(din,userName,"personal.txt",len));
					th_personal.start();
					th_personal.join();
					len = Integer.parseInt(in.readLine());
					Thread th_photo = new Thread(new FileThread(din,userName,"photo.png",len));
					th_photo.start();
					th_photo.join();
					//excel处理
					Workbook workbook = null;
					Cell cell = null;
					InputStream excelIn = new FileInputStream(Signal.dataFloder+File.separator+userName+File.separator+filename);
					workbook = Workbook.getWorkbook(excelIn);
					Sheet sheet = workbook.getSheet(0);
					//写入数据库
					sqlTool.connectLocalSQL(Signal.dataBaseName, Signal.dataBaseUser, Signal.dataBasePassword);
					sqlTool.setItem(Signal.items);
					sqlTool.insertData("userinfo");
					sqlTool.pstat.setString(1, sheet.getCell(0,1).getContents());
					sqlTool.pstat.setString(2, sheet.getCell(1,1).getContents());
					sqlTool.pstat.setString(3, sheet.getCell(2,1).getContents());
					sqlTool.pstat.setInt(4, Integer.parseInt(sheet.getCell(3,1).getContents()));
					String birthday = sheet.getCell(4,1).getContents();
					sqlTool.pstat.setDate(5, sqlTool.dateToDate(birthday));
					File txt = new File(Signal.dataFloder+File.separator+userName+File.separator+"personal.txt");
					File png = new File(Signal.dataFloder+File.separator+userName+File.separator+"photo.png");
					FileInputStream ftxt = new FileInputStream(txt);
					FileInputStream fpng = new FileInputStream(png);
					sqlTool.pstat.setAsciiStream(6,ftxt,(int)txt.length());
					sqlTool.pstat.setBinaryStream(7, fpng,(int)png.length());
					sqlTool.pstat.executeUpdate();
					
					//创建文件表
					Statement stat = (Statement)sqlTool.conn.createStatement();
					String sql = "create table fileserver."+userName+" (filename varchar(45) not null primary key );";
					stat.execute(sql);
					sqlTool.close();
					
					out.print(Signal.OK+"\n");
					out.flush();
				}
				else if(Signal.LOGIN.equals(com))//登陆֤
				{
					boolean log = false;
					userName = in.readLine();
					passWord = in.readLine();
					sqlTool.connectLocalSQL(Signal.dataBaseName, Signal.dataBaseUser, Signal.dataBasePassword);
					Statement stat = (Statement)sqlTool.conn.createStatement();
					String sql = "select username,password from userinfo where username='"+userName+"';";
					ResultSet rs = stat.executeQuery(sql);
					while (rs.next())
					{
						String str = rs.getString("password");
						if(str.equals(passWord))
						{
							log=true;
						}
					}
					
					sqlTool.close();
					if(log)
					{
						out.print(Signal.OK+"\n");
						out.flush();
					}
					else
					{
						out.print(Signal.FALSE+"\n");
						out.flush();
					}
					
					
				}
				else if(Signal.SENDFILE.equals(com))//文件传输
				{
					userName = in.readLine();
					String fileName = in.readLine();
					sqlTool.connectLocalSQL(Signal.dataBaseName, Signal.dataBaseUser, Signal.dataBasePassword);
					Statement st = (Statement)sqlTool.conn.createStatement();
					String sql = "insert into fileserver."+userName+" (filename) values ('"+fileName+"');";
					st.execute(sql);
					int len = Integer.parseInt(in.readLine());
					Thread th = new Thread(new FileThread(din,userName,fileName,len));
					th.start();
					th.join();//不阻塞会导致不能连续发送
					System.out.println("接受文件结束");
					sqlTool.close();
				}
				else if(Signal.STOP.equals(com))//终止服务 
				{
					Signal.userCounter--;
					MainWindows.w.userCounter.setText(String.valueOf(Signal.userCounter));
					break;
				}
			}
			in.close();
			out.close();
			din.close();
			client.close();
			fileClient.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
