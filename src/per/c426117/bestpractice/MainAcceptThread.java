package per.c426117.bestpractice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ClientInfoStatus;


public class MainAcceptThread implements Runnable 
{
	private ServerSocket serverSocket = null;
	private ServerSocket fileServerSocket = null;
	private Socket client = null; // 用于传指令
	private Socket fileclient = null; //用于传文件
	private Thread th = null;
  	
	@Override
	public void run() 
	{
		while (Signal.serverStopFlag)
		{
			try 
			{
				serverSocket  = new ServerSocket(Signal.mainAcceptPort);
				fileServerSocket = new ServerSocket(Signal.mainFilePort);
				MainWindows.w.printLog("接收服务启动等待链接");
			} 
			catch (Exception e) 
			{
				MainWindows.w.printLog("接收服务启动失败，请重启服务器");
				serverSocket = null;
				fileServerSocket = null;
				e.printStackTrace();
			}
			
			try
			{
				client = serverSocket.accept();
				fileclient = fileServerSocket.accept();
				th = new Thread(new StringThread(client,fileclient));
				th.start();
				Signal.userCounter++;
				MainWindows.w.userCounter.setText(String.valueOf(Signal.userCounter));
				serverSocket.close();
				fileServerSocket.close();
				serverSocket = null;
				fileclient = null;
			}
			catch (IOException e)
			{
				serverSocket = null;
				e.printStackTrace();
			}
			
			
			
		}
	}
}
