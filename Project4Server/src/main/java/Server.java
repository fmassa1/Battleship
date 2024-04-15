import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	private Queue<ClientThread> waitingClients = new LinkedList<>();
	private Map<ClientThread, ClientThread> pairedClients = new HashMap<>();



	Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			
			boolean pvp = false;

			ArrayList<Move> aiMoves = new ArrayList<>();
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
			
			public void updateClients(String message) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(message);
					}
					catch(Exception e) {}
				}
			}


			public void moveGenerator(ClientThread c){
				try {
					if(aiMoves.isEmpty()) {
						for (int i = 0; i < 10; i++) {
							for (int x = 0; x < 10; x++) {
								aiMoves.add(new Move(x, i));
							}
						}
					}
					Random random = new Random();
					int index = random.nextInt(aiMoves.size());
					Move sendingMove = aiMoves.get(index);
					aiMoves.remove(index);
					c.out.writeObject(sendingMove);
				}
				catch(Exception e) {}

			}
			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}


				updateClients("new client on server: client #"+count);
					
				 while(true) {
					    try {
					    	Object data = in.readObject();
							if(data instanceof String) {
								if (data.toString().equals("queue")) {
									waitingClients.add(this);
									pvp = true;
								}
								if (waitingClients.size() >= 2) {
									ClientThread client1 = waitingClients.poll();
									ClientThread client2 = waitingClients.poll();
									pairedClients.put(client1, client2);
									pairedClients.put(client2, client1);
									client1.out.writeObject("begin");
									client2.out.writeObject("begin");
								}
							}
							else if(data instanceof Move) {
								Move newMove = (Move) data;
								if(!pvp) {
									moveGenerator(this);
								}
								else{
									pairedClients.get(this).out.writeObject(new Move(newMove));
								}
							}
							else if(data instanceof Grid) {
								Grid enemyGrid = (Grid) data;
								if(pvp) {
									pairedClients.get(this).out.writeObject(enemyGrid);
								}
							}

					    	
					    	}
					    catch(Exception e) {
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	updateClients("Client #"+count+" has left the server!");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread
}
