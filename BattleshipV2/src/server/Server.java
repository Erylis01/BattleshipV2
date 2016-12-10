package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {

	// a unique ID for each connection
	public static int uniqueId;
	// the port number to listen for connection
	private int port;
	// date to display
	private SimpleDateFormat date;
	// ArrayList of clients
	private ArrayList<GameThread> clients;
	// ArrayList of clients
	private static ArrayList<GameThread> waitingRoom;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;
	private InetAddress ip;

	public Server(int port) {
		this.port = port;
		this.date = new SimpleDateFormat("HH:mm:ss");
		this.clients = new ArrayList<>();
		this.waitingRoom = new ArrayList<>();
		try {
			this.ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start server on port 8051 unless a PortNumber is specified
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int portNumber = 1050;
		switch (args.length) {
		case 1:
			try {
				portNumber = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.out.println("Invalid port number.");
				System.out.println("Usage is: > java Server [portNumber]");
				return;
			}
		case 0:
			break;
		default:
			System.out.println("Usage is: > java Server [portNumber]");
			return;
		}
		// create a server object and start it
		Server server = new Server(portNumber);
		server.start(server);
	}

	public void start(Server server) {
		keepGoing = true;
		try {
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);
			while (keepGoing) {
				display("Server waiting for Clients on" + ip + ":" + port + ".");
				Socket socket = serverSocket.accept();
				if (!keepGoing)
					break;
				GameThread pt = new GameThread(socket);
				pt.start();
			} // I was asked to stop
			try {
				serverSocket.close();
			} catch (Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
			String msg = date.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}

	public void stop() {
		keepGoing = false;
		try {
			new Socket("localhost", port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to remove a client using the LOGOUT message
	 * 
	 * @param id
	 */
	synchronized void remove(int id) {
		// scan the array list until we found the Id
		for (int i = 0; i < clients.size(); ++i) {
			GameThread gt = clients.get(i);
			// found it
			if (gt.getUniqueid() == id) {
				clients.remove(i);
				return;
			}
		}
	}

	private void display(String msg) {
		String time = date.format(new Date()) + " " + msg;
		System.out.println(time);
	}

	public ArrayList<GameThread> getClients() {
		return clients;
	}

	public void setClients(ArrayList<GameThread> clients) {
		this.clients = clients;
	}

	public static ArrayList<GameThread> getWaitingRoom() {
		return waitingRoom;
	}

	public void setWaitingRoom(ArrayList<GameThread> waitingRoom) {
		this.waitingRoom = waitingRoom;
	}
}
