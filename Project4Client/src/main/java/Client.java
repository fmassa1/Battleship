import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
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
