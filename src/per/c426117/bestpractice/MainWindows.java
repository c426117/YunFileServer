package per.c426117.bestpractice;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.ProtectionDomain;
import java.sql.ResultSet;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mysql.jdbc.Clob;
import com.mysql.jdbc.Statement;

public class MainWindows 
{
	public static MainWindows w = new MainWindows();
	
	public int port = 23333;
	MainAcceptThread mainAcceptThread = null;
	
	public JTextArea printArea = new JTextArea(0,1000);//右侧输出文本框
	public JPanel fileNames= new JPanel();//左边用户显示区
	public JButton startServerButton = new JButton("启动");//启动键
	public JButton stopServerButton = new JButton("停止");//停止键
	public JLabel statFlagLabel = new JLabel();//状态标识
	public JTextField ipTextField = new JTextField(21);//显示本机ip的文本框
	public JTextField userCounter = new JTextField(21);//显示在线的用户数量
	
	public JLabel username = new JLabel();//用户名
	public JLabel age = new JLabel();//年龄
	public JLabel sex = new JLabel();//性别
	public JLabel password = new JLabel();//密码
	public JLabel birthday = new JLabel();//生日
	public JLabel photo = new JLabel();//照片
	
	public JTextArea printPersonal = new JTextArea(0,1000);//Personal输出文本框
	public JTextArea printFiles = new JTextArea(0,1000);//Personal输出文本框
	
	public JTextField searchUser = new JTextField(45);//用户搜索
	public JButton search = new JButton("查找");
	
	private MainWindows() //使用单例模式运作
	{
		//初始化程序
		
		//停止按钮为不可用
		stopServerButton.setEnabled(false);
		
		//获取本机的IP
		String str = new String();
		try
		{
			str=InetAddress.getLocalHost().getHostAddress();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		finally
		{
			ipTextField.setText(str+" : "+String.valueOf(port));
		}
		
	}
	
	public void show ()//界面绘制
	{
		//主窗口设置
		JFrame jFrame = new JFrame("文件服务器端");
		jFrame.addWindowListener(new WindowAdapter() {			
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				System.out.println("窗口被关闭");
				System.exit(1);
			}
		});
		jFrame.setSize(1050, 700);
		jFrame.setLocation(200, 20);
		jFrame.setBackground(Color.white);
		jFrame.setResizable(false);
		jFrame.setLayout(null);
		//标签设置
		Font font = new Font("微软雅黑", Font.BOLD, 14);
		JLabel lJLabel = new JLabel("文件",JLabel.CENTER);
		lJLabel.setOpaque(true);
		lJLabel.setBackground(Color.GRAY);
		lJLabel.setForeground(Color.white);
		lJLabel.setFont(font);
		lJLabel.setBounds(0, 0, 150, 40);
		jFrame.add(lJLabel);
		
		JLabel cJLabel = new JLabel("用户信息",JLabel.CENTER);
		cJLabel.setOpaque(true);
		cJLabel.setBackground(Color.GRAY);
		cJLabel.setForeground(Color.white);
		cJLabel.setFont(font);
		cJLabel.setBounds(150, 0, 1050-150-300, 40);
		jFrame.add(cJLabel);
		
		JLabel rJLabel = new JLabel("服务器",JLabel.CENTER);
		rJLabel.setOpaque(true);
		rJLabel.setBackground(Color.GRAY);
		rJLabel.setForeground(Color.white);
		rJLabel.setFont(font);
		rJLabel.setBounds(1050-300, 0,300, 40);
		jFrame.add(rJLabel);
		
		//左边的用户显示区域
		fileNames.setLayout(null);
		fileNames.setBounds(0, 40, 150, 700-40);
		fileNames.setBackground(Color.white);
		
		JScrollPane printSroll3 = new JScrollPane(printFiles,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		printSroll3.setBounds(0, 0, 150 , 700-40);
		printFiles.setBackground(Color.white);
		printFiles.setBounds(0, 0, 150 , 700-40);
		printFiles.setLineWrap(true);//自动换行
		fileNames.add(printSroll3);
		
		//中间区域
		JPanel infoShow = new JPanel();
		infoShow.setLayout(null);
		infoShow.setBounds(150,40,1050-300-150,700-40);
		//infoShow.setBackground(Color.white);
		
		JLabel nameLabel = new JLabel("用户名");
		nameLabel.setFont(font);
		nameLabel.setBounds(5,5,80,40);
		infoShow.add(nameLabel);
		
		username.setFont(font);
		username.setBounds(90,5,90,40);
		username.setOpaque(true);
		username.setBackground(Color.white);
		infoShow.add(username);
		
		JLabel ageLabel = new JLabel("年龄");
		ageLabel.setFont(font);
		ageLabel.setBounds(5,50,80,40);
		infoShow.add(ageLabel);
		
		age.setFont(font);
		age.setBounds(90,50,90,40);
		age.setOpaque(true);
		age.setBackground(Color.white);
		infoShow.add(age);
		
		JLabel sexLabel = new JLabel("性别");
		sexLabel.setFont(font);
		sexLabel.setBounds(5,95,80,40);
		infoShow.add(sexLabel);
		
		sex.setFont(font);
		sex.setBounds(90,95,90,40);
		sex.setOpaque(true);
		sex.setBackground(Color.white);
		infoShow.add(sex);
		
		JLabel passWordlabel = new JLabel("密码");
		passWordlabel.setFont(font);
		passWordlabel.setBounds(185,5,80,40);
		infoShow.add(passWordlabel);
		
		password.setFont(font);
		password.setBounds(270,5,90,40);
		password.setOpaque(true);
		password.setBackground(Color.white);
		infoShow.add(password);
		
		JLabel birthdayLabel = new JLabel("生日");
		birthdayLabel.setFont(font);
		birthdayLabel.setBounds(185,50,80,40);
		infoShow.add(birthdayLabel);
		
		birthday.setFont(font);
		birthday.setBounds(270,50,90,40);
		birthday.setOpaque(true);
		birthday.setBackground(Color.white);
		infoShow.add(birthday);
		
		photo.setFont(font);
		photo.setBounds(365,5,1050-450-370,300);
		photo.setBackground(Color.white);
		infoShow.add(photo);
		
		JLabel personalLabel = new JLabel("个人简介");
		personalLabel.setFont(font);
		personalLabel.setBounds(5,270,80,40);
		infoShow.add(personalLabel);
		
		JScrollPane printSroll2 = new JScrollPane(printPersonal,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		printSroll2.setBounds(5, 310, 1050-450-10 , 250);
		printPersonal.setBackground(Color.white);
		printPersonal.setBounds(5, 310, 1050-450-10 , 250);
		printPersonal.setLineWrap(true);//自动换行
		infoShow.add(printSroll2);
		
		searchUser.setBounds(5, 570, 1050-450-100-5, 40);
		searchUser.setOpaque(true);
		searchUser.setBackground(Color.white);
		searchUser.setFont(font);
		searchUser.setEditable(true);
		infoShow.add(searchUser);
		
		search.setBounds(1050-450-100+10,570,80,40);
		search.setBackground(Color.white);
		infoShow.add(search);
		
		//右边的显示区域
		JPanel serverControl = new JPanel();
		serverControl.setBounds(1050-300, 40, 300, 700-40);
		serverControl.setBackground(Color.gray);
		serverControl.setLayout(null);
		
		startServerButton.setBounds(5,700-70-45,90,40);
		startServerButton.setFont(font);
		startServerButton.setBackground(Color.white);
		serverControl.add(startServerButton);
		
		stopServerButton.setBounds(5+85+10, 700-70-45, 90, 40);
		stopServerButton.setFont(font);
		stopServerButton.setBackground(Color.white);
		serverControl.add(stopServerButton);
	
		statFlagLabel.setOpaque(true);
		statFlagLabel.setBackground(Color.red);
		statFlagLabel.setBounds(90+85+10+10, 700-70-45, 95, 40);
		serverControl.add(statFlagLabel);
		
		JScrollPane printSroll = new JScrollPane(printArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		printSroll.setBounds(5, 0, 285 , 700-40-130-50);
		printArea.setBackground(Color.white);
		printArea.setBounds(5, 0, 277 ,700-40-130-50);
		printArea.setLineWrap(true);//自动换行
		serverControl.add(printSroll);
		
		JLabel userSum = new JLabel("在线用户:");
		userSum.setFont(font);
		userSum.setBounds(5, 700-40-130-50+5, 80, 40);
		serverControl.add(userSum);
		
		userCounter.setBounds(85, 700-40-130-50+5, 300-90-5, 40);
		userCounter.setBackground(Color.white);
		userCounter.setEditable(false);
		userCounter.setFont(font);
		serverControl.add(userCounter);
		
		JLabel iPjLabel = new JLabel("IP地址：");
		iPjLabel.setFont(font);
		iPjLabel.setBounds(5,(700-40-130)+5,80, 40);
		serverControl.add(iPjLabel);
		
		ipTextField.setBounds(85,(700-40-130)+5, 300-90-5, 40);
		ipTextField.setBackground(Color.white);
		ipTextField.setEditable(false);
		ipTextField.setFont(font);
		serverControl.add(ipTextField);
		
		jFrame.add(fileNames);
		jFrame.add(serverControl);
		jFrame.add(infoShow);
		jFrame.setVisible(true);
		
		//以上是绘图部分  -------------------------------- 以下是响应事件部分
		
		//启动服务器事件
		startServerButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource()==startServerButton)
				{
					statFlagLabel.setBackground(Color.green);
					startServerButton.setEnabled(false);
					stopServerButton.setEnabled(true);
					startServer();
					
				}
			}
		});
		
		//关闭服务器事件
		stopServerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource()==stopServerButton)
				{
					statFlagLabel.setBackground(Color.red);
					stopServerButton.setEnabled(false);
					startServerButton.setEnabled(true);
					stopServer();
				}
			}
		});
		
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getSource()==search)
				{
					printFiles.setText(null);
					boolean find = false;
					String user = searchUser.getText();
					SQLTool sqlTool = new SQLTool();
					sqlTool.connectLocalSQL(Signal.dataBaseName, Signal.dataBaseUser, Signal.dataBasePassword);
					Statement stat = null;
					ResultSet rS = null;
					try
					{
						stat = (Statement)sqlTool.conn.createStatement();
						String sql = "select username,password,sex,age,birthday,personal from fileserver.userinfo where username='"+user+"'";
						rS = stat.executeQuery(sql);
						while(rS.next())
						{
							username.setText(rS.getString(1));
							password.setText(rS.getString(2));
							sex.setText(rS.getString(3));
							age.setText(String.valueOf(rS.getInt(4)));
							birthday.setText(rS.getDate(5).toString());
							Clob clob = (Clob)rS.getClob(6);
							printPersonal.setText(clob.getSubString(1, (int)clob.length()));
							find = true;
							break;
						}
						if(find)
						{
							Icon icon = new ImageIcon(Signal.dataFloder+File.separator+user+File.separator+"photo.png");
							photo.setIcon(icon);	
							
							sql = " select * from fileserver."+user;
							rS = stat.executeQuery(sql);
							while(rS.next())
							{
								printFiles.append(rS.getString(1)+"\n");
							}
							if(printFiles.getText()==null)
							{
								printFiles.setText("没有文件");
								
							}
						}
						sqlTool.close();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	
	
	
	public void printLog (String str)//使滚动条可以滚动的输出函数，输出到状态显示文本框
	{
		printArea.append(str+"\n");
		printArea.setCaretPosition(printArea.getText().length());
	}
	
	public boolean startServer ()
	{
		mainAcceptThread = new MainAcceptThread();
		Thread serverMainAcceptThread = new Thread(mainAcceptThread);
		serverMainAcceptThread.start();
		
		userCounter.setText(String.valueOf(Signal.userCounter));
		return true;
	}
	
	public boolean stopServer ()
	{
		Signal.serverStopFlag=false;
		return true;
	}
	
}
