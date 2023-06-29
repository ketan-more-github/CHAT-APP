package chat_app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String  username;
	
	public Client(Socket socket, String username) {
		
		try {
			this.socket = socket;
			this.username = username;
			this.bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
		
			closeEverything(socket , bufferedReader , bufferedWriter);
		}
	}
	
	public void sendMsg()
	{
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			Scanner sc = new Scanner(System.in);
			
			while(socket.isConnected())
			{
				String msgtosend = sc.nextLine();
				bufferedWriter.write(username + ":" + msgtosend);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
			
			
		}catch (Exception e) {
			closeEverything(socket , bufferedReader , bufferedWriter);
		}
	}
	
	public void listenformsg()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				String msgfromgroupchat;
				
				while(socket.isConnected())
				{
					try {
						msgfromgroupchat = bufferedReader.readLine();
						System.out.println(msgfromgroupchat);
					} catch (IOException e) {
						closeEverything(socket , bufferedReader , bufferedWriter);
					}
					
				}
				
			}
		}).start();
	}
	
	 public void closeEverything(Socket socket , BufferedReader bufferedReader , BufferedWriter bufferedWriter){
		    
		    try {
		    	if(bufferedReader != null)
		    	{
		    		bufferedReader.close();
		    	}
		    	if(bufferedWriter != null)
		    	{
		    		bufferedWriter.close();
		    	}
		    	if(socket != null)
		    	{
		    		socket.close();
		    	}
		    }catch (Exception e) {
		    	e.printStackTrace();
			}
		}
	 
	 public static void main(String[] args) throws UnknownHostException, IOException {
		
		 Scanner sc  = new Scanner(System.in);
		 System.out.println("Enter your name for group chat: ");
		 String username = sc.nextLine();
		 Socket socket = new Socket("localhost", 8080);
		 Client client = new Client(socket, username);
		 client.listenformsg();
		 client.sendMsg();
		 
		 
	}

}
