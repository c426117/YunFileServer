package per.c426117.bestpractice;

import java.io.File;

public class Signal 
{
	public static boolean serverStopFlag = true;//服务器启停标志
	public static int mainAcceptPort = 2333;//主服务进程的端口号
	public static int mainFilePort = 2334;//主文件接受进程端口号
	public static int userCounter = 0;//用户数量统计
	public static String dataFloder = "D:"+File.separator+"ServerData";//程序数据文件夹根目录
	public static String[] items = {"username","password","sex","age","birthday","personal","photo"};//数据库注册的时候使用的数据列，与SQLTool配合使用
	public static String dataBaseName = "fileserver";
	public static String dataBaseUser = "root";
	public static String dataBasePassword = "0000";
	
	
	
	
	//服务器指令
	public static final String ISLINK = "islink";//判断是不是链接成功
	public static final String LOGIN = "login";//登陆
	public static final String SIGNUP = "signup";//注册
	public static final String STOP = "stop";//终止链接
	public static final String OK = "ok";//成功
	public static final String FALSE = "false";//失败
	public static final String SENDFILE = "sendfile";//传输文件
	public static final String IMAGEPERSONAL = "imagepersonal";//上传用户照片和大文本自我介绍
	public static final String SAVEOVER = "saveover";//接受一个文件完毕
 }
