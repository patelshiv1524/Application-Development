package senecacollege;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ChatServer extends Application{

	private TextArea ta = new TextArea();
	private Hashtable<Socket, DataOutputStream> outputStream = new Hashtable<Socket, DataOutputStream>();
	private ServerSocket ss;
	
	@Override
	public void start(@SuppressWarnings("exports") Stage ps) throws Exception {
		// TODO Auto-generated method stub
		ta.setWrapText(true);//true
		
		Scene sc = new Scene(new ScrollPane(ta),200,200);
		ps.setTitle("Server");
		ps.setScene(sc);
		ps.show();
		
		new Thread(()->listen()).start();
		
	}
	
	private void listen() {
		try {
			ss = new ServerSocket(8000);
			
			Platform.runLater(()->
							ta.appendText("Multi-Threaded server started: "+new Date()+"\n"));
			while(true) {
			
				Socket s = ss.accept();
				
				Platform.runLater(()-> ta.appendText("Connection from " + s + " at " + new Date()  + "\n") );
				
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				
				outputStream.put(s, dout);
				
				new ServerThread(this, s);
				
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	Enumeration<DataOutputStream> getOutputStreams() {
		return outputStream.elements();
	}
	
	void sendToAll(String message) {
	    Enumeration<DataOutputStream> e = getOutputStreams();
	    while (e.hasMoreElements()) {
	        DataOutputStream dout = (DataOutputStream) e.nextElement();
	        try {
	            if (!dout.equals(null) && !dout.equals("")) {
	                dout.writeUTF(message);
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	}




	void removeClient(Socket socket) {
        outputStream.remove(socket);
        Platform.runLater(() -> ta.appendText("Client " + socket + " disconnected\n"));
    }
	class ServerThread extends Thread{
		private ChatServer server;
		private Socket socket;
		
		public ServerThread(ChatServer server, Socket socket) {
			this.socket = socket;
			this.server = server;
			start();
		}
		
		public void run() {
	        try {
	            DataInputStream din = new DataInputStream(socket.getInputStream());

	            while (true) {
	                String s;
	                try {
	                    s = din.readUTF();
	                } catch (EOFException e) {
	                    // Client disconnected, close resources and break
	                    din.close();
	                    socket.close();
	                    server.removeClient(socket);
	                    break;
	                }

	                server.sendToAll(s);

	                Platform.runLater(() -> ta.appendText(s + "\n"));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
public static void main(String[] args) {
	launch(args);
}

}

