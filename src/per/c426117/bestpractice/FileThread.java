package per.c426117.bestpractice;

import java.net.Socket;
import java.io.*;



public class FileThread implements Runnable 
{
	private Socket fileSocket = null;
	private String userName = null;
	private String fileFloderPath = null;
	private String fileName = null;
	private DataInputStream din = null;
	private int lenght = 0;
	
	public FileThread(DataInputStream din,String userName,String fileName,int filelenght) 
	{
		this.lenght = filelenght;
		this.din = din;
		this.fileName = fileName;
		this.userName = userName;
		fileFloderPath = Signal.dataFloder+File.separator+userName;
		File file = new File(fileFloderPath);
		if(file.exists())
		{
			
		}
		else
		{
			file.mkdir();
		}
	}
	
	@Override
	public void run() 
	{
		try
		{
			
			FileOutputStream fout = null;
			fout = new FileOutputStream(fileFloderPath+File.separator+fileName);
			byte[] buf = new byte[2048];
			int len = 0;
			int count = 0;
			while((len=din.read(buf))>0)
			{
				fout.write(buf,0,len);
				fout.flush();
				count = count + len;
				if(count>=lenght)
				{
					break;
				}
			}
			fout.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
