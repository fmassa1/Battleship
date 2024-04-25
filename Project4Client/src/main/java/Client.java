// Ricky Massa
// 04-25-2024
// UIC Spring 2024
// Client.java
// Client class, that is used to send and receive object from the server

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	
	private Consumer<Object> callback;
	
	Client(Consumer<Object> call){
	
		callback = call;
	}
	
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		while(true) {
			 
			try {
				Object data = in.readObject();
				if(data instanceof Move) {
					Move newMove = (Move) data;
					callback.accept(newMove);
				}
				else if(data instanceof String) {
					callback.accept(data.toString());
				}
				else if(data instanceof Grid) {
					Grid enemyGrid = (Grid) data;
					callback.accept(enemyGrid);
				}
				else if(data instanceof ArrayList) {
					ArrayList enemyShips = (ArrayList) data;
					callback.accept(enemyShips);
				}
				else if(data instanceof Message) {
					Message mess = (Message) data;
					callback.accept(mess);
				}
			}
			catch(Exception e) {

			}
		}
	
    }
	
	public void send(Object data) {
		
		try {
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
