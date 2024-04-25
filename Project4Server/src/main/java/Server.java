// Ricky Massa
// 04-25-2024
// UIC Spring 2024
// Server.java
// Server class, that is used to send and receive object from clients

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;


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
					
				 while(true) {
					    try {
							Object data = in.readObject();
							if (data instanceof String) {
								if (data.toString().equals("queue")) {
									waitingClients.add(this);
									pvp = true;

									if (waitingClients.size() >= 2) {
										ClientThread client1 = waitingClients.poll();
										ClientThread client2 = waitingClients.poll();
										pairedClients.put(client1, client2);
										pairedClients.put(client2, client1);
										client1.out.writeObject("begin");
										client2.out.writeObject("begin");
									}
								}
								else if (data.toString().equals("dequeue")) {
									if(pairedClients.containsKey(this)) {
										ClientThread temp = pairedClients.get(this);
										pairedClients.remove(temp);
										pairedClients.remove(this);
										pvp = false;
									}
								}
							} else if (data instanceof Move) {
								Move newMove = (Move) data;
								if (!pvp) {
									moveGenerator(this);
								} else {
									pairedClients.get(this).out.writeObject(new Move(newMove));
								}
							} else if (data instanceof Grid) {
								Grid enemyGrid = (Grid) data;
								if (pvp) {
									pairedClients.get(this).out.writeObject(enemyGrid);
								}
							} else if (data instanceof ArrayList) {
								ArrayList ships = (ArrayList) data;
								if (pvp) {
									pairedClients.get(this).out.writeObject(ships);
								}
							} else if (data instanceof Message) {
								Message mess = (Message) data;
								if (pvp) {
									pairedClients.get(this).out.writeObject(mess);
								}
							}
						}
					    catch(Exception e) {
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread
}
