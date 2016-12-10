package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import metier.Command;

public class GameThread extends Thread {

	private Socket socket;
	private ObjectOutputStream Output;
	private ObjectInputStream Input;

	private int uniqueid; // unique id easier to remove
	private boolean inGame = false;
	private String pseudo;
	private GameThread opponentPairThread;
	private boolean isItYourTurn;

	private Command command;

	public GameThread(Socket socket) {
		this.setSocket(socket);
		this.setUniqueid(Server.uniqueId++);
		try {
			Output = new ObjectOutputStream(socket.getOutputStream());
			Input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void run() {
		while (!inGame) {
			try {
				command = (Command) Input.readObject();
			} catch (ClassNotFoundException e) {
				System.out.println("ici");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("là");
				e.printStackTrace();
			}
			switch (command.getType()) {
			case Command.AJOUT:
				addPlayer(command.getMsg());
				System.out.println(isItYourTurn);
				sendMessage(new Command(Command.OKAJOUT, isItYourTurn));
				break;
			case Command.ADVERSAIRE:
				if (opponentPairThread != null) {
					sendMessage(new Command(Command.OKADVERSAIRE, opponentPairThread.getPseudo()));
				} else {
					sendMessage(new Command(Command.NOADVERSAIRE, "You have to wait..."));
				}
				break;
			case Command.HIT:
				System.out.println(this.pseudo+" send un hit à "+ opponentPairThread.pseudo);
				opponentPairThread.sendMessage(new Command(Command.HIT, command.getPosX(), command.getPosY()));
				break;
			case Command.TOUCHED:
				System.out.println("Le touched est recu niveau serveur");
				opponentPairThread.sendMessage(new Command(Command.TOUCHED, command.getPosX(), command.getPosY()));
				isItYourTurn = true;
				break;
			case Command.MISS:
				System.out.println("Le Missed est recu niveau serveur");
				opponentPairThread.sendMessage(new Command(Command.MISS, command.getPosX(), command.getPosY()));
				isItYourTurn = true;
				break;
			case Command.WIN:
				System.out.println("Le Win est recu niveau serveur");
				opponentPairThread.sendMessage(new Command(Command.WIN, command.getPosX(), command.getPosY()));
				isItYourTurn = false;
				inGame = false;
				break;
			}
		}
	}

	public void addPlayer(String s) {
		if (Server.getWaitingRoom().size() == 1) {
			opponentPairThread = Server.getWaitingRoom().get(0);
			opponentPairThread.setOpponentPairThread(this);
			pseudo = s;
			Server.getWaitingRoom().clear();
			isItYourTurn = false;
		} else {
			Server.getWaitingRoom().add(this);
			pseudo = s;
			isItYourTurn = true;
		}
	}

	private void display(String msg) {
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
		String time = date.format(new Date()) + " " + msg;
		System.out.println(time);
	}

	/*
	 * To send a message to the server
	 */
	public void sendMessage(Command msg) {
		try {
			Output.writeObject(msg);
			Output.flush();
		} catch (IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	private void sendHit(int posX, int posY) {
		sendMessage(new Command(Command.HIT, posX, posY));
		isItYourTurn = false;
	}
	
	private void sendTouched(int posX, int posY) {
		sendMessage(new Command(Command.TOUCHED, posX, posY));
		isItYourTurn = false;
	}

	private void sendMissed(int posX, int posY) {
		sendMessage(new Command(Command.MISS, posX, posY));
		isItYourTurn = false;
	}
	public int getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(int uniqueid) {
		this.uniqueid = uniqueid;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public GameThread getOpponentPairThread() {
		return opponentPairThread;
	}

	public void setOpponentPairThread(GameThread opponentPairThread) {
		this.opponentPairThread = opponentPairThread;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

}
