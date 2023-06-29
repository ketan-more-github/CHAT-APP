package chat_app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

	public static ArrayList<ClientHandler> arr = new ArrayList<>();
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	private String clientUsername;
	
	
	public ClientHandler(Socket socket) {
		

	try {
		this.socket = socket;
		this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		this.clientUsername = br.readLine();
		arr.add(this);
		broadcastMessage("SERVER: " + clientUsername + " has enterd the chat");
		
	} catch (IOException e) {
		closeEverything(socket , br , bw);
	}
		
	}


	@Override
	public void run() {
		
		String msgFromClient;
		
		while(socket.isConnected())
		{
			try {
				msgFromClient = br.readLine();
				broadcastMessage(msgFromClient);
			} catch (IOException e) {
				closeEverything(socket , br , bw);
				break;
			}
		}
		
		
	}
	
	public void broadcastMessage(String msgTOSend) {
		
		for(ClientHandler clientHandler : arr)
		{
			try {
				if(!clientHandler.clientUsername.equals(clientUsername))
				{
					clientHandler.bw.write(msgTOSend);
					clientHandler.bw.newLine();
					clientHandler.bw.flush();
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	public void removeClienthandler(){
		arr.remove(this);
		broadcastMessage("Server: " + clientUsername + " has left the chat!");
	}
	
  public void closeEverything(Socket socket , BufferedReader bufferedReader , BufferedWriter bufferedWriter){
	    removeClienthandler();
	    
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
	

}
