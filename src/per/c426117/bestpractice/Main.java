package per.c426117.bestpractice;

import java.io.File;

public class Main 
{
	public static void main (String[] args) throws Exception
	{
		//创建服务器文件夹
		File dataFloder = new File(Signal.dataFloder);
		if(dataFloder.exists())
		{
			
		}
		else
		{
			dataFloder.mkdirs();
		}
		
		//显示界面
		MainWindows.w.show();
	}
}
