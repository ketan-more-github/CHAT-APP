package chat_app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private ServerSocket serversocket;
	

	public Server(ServerSocket serversocket) {
		this.serversocket = serversocket;
	}
	
	public void startServer() {
	
		try {
			
			while(!serversocket.isClosed())
			{
				Socket soc = serversocket.accept();
				System.out.println("A new client is connected.");
				ClientHandler clienthandler = new ClientHandler(soc);
				
				Thread thread = new Thread(clienthandler);
				thread.start();
				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	public void closeServerSocket() {
	try{
		if(serversocket != null){
			serversocket.close();
		   }
	    }catch (IOException e) {
		 e.printStackTrace();	
		}
	}





	public static void main(String[] args) throws IOException {
		ServerSocket serversoc = new ServerSocket(8080);
		Server server = new Server(serversoc);
		System.out.println("Hey keten Server is Working...Welcome to CHAT-APP");
		server.startServer();
		
		
	}

}
