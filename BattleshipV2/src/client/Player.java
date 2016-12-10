package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JTextArea;

import controller.GameController;
import metier.Command;
import model.BoardPlayer;
import view.Draughtboard;

public class Player {

	private String pseudo;
	private String serverAddress;
	private int port;

	private Socket socket;
	private ObjectInputStream Input; // to read from the socket
	private ObjectOutputStream Output; // to write on the socket
	private Command command;

	private boolean partnerAwait = false;
	private boolean opponentFind = false;
	private static boolean isItYourTurn;

	public Player(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public static void main(String[] args) {
		String serverAddress = "localhost";
		Player p = new Player(serverAddress);
		BoardPlayer bp = new BoardPlayer();
		GameController c = new GameController(bp, p);
	}

	/*
	 * To start the dialog
	 */
	public void start(GameController game) {

		// try to connect to the server
		try {
			socket = new Socket(serverAddress, port);
		}
		// if it failed not much I can so
		catch (Exception ec) {
			display(game, "Error connectiong to server:" + ec);
		}
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(game, msg);

		/* Creating both Data Stream */
		try {
			Input = new ObjectInputStream(socket.getInputStream());
			Output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (!partnerAwait) {
			sendMessage(new Command(Command.AJOUT, game.getPseudo()));
				try {
					command = (Command) Input.readObject();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				switch (command.getType()) {
				case Command.OKAJOUT:
					if (command.isBool()) {
						partnerAwait = true;
						isItYourTurn = true;
					} else {
						partnerAwait = true;
						isItYourTurn = false;
					}
					break;
				}
			}
		game.activeFrame();
		while (!opponentFind) {
			sendMessage(new Command(Command.ADVERSAIRE, game.getPseudo()));
			try {
				command = (Command) Input.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			switch (command.getType()) {
			case Command.OKADVERSAIRE:
				display(game, "Votre adversaire sera " + command.getMsg());
				if (isItYourTurn) {
					display(game, "\n C'est votre tour");
				} else {
					display(game, "\n C'est le tour de votre adversaire");
				}
				opponentFind = true;
				break;
			case Command.NOADVERSAIRE:
				display(game, command.getMsg());
				break;
			}
		}
		game.activeFrame();
		new PlayerThread(socket, Input, Output, game, isItYourTurn).start();
	}

	/*
	 * To send a message to the console of the GUI
	 */
	private void display(GameController c, String msg) {
		c.displayText(msg);
	}

	/*
	 * To send a message to the server
	 */
	public void sendMessage(Command msg) {
		try {
			Output.writeObject(msg);
			Output.flush();
		} catch (IOException e) {
			System.out.println("Exception writing to server: " + e);
		}
	}

	public void sendHit(int posX, int posY) {
		sendMessage(new Command(Command.HIT, posX, posY));
		isItYourTurn = false;
	}

	/*
	 * When something goes wrong Close the Input/Output streams and disconnect
	 * not much to do in the catch clause
	 */
	public void disconnect() {
		try {
			if (Input != null)
				Input.close();
		} catch (Exception e) {
		} // not much else I can do
		try {
			if (Output != null)
				Output.close();
		} catch (Exception e) {
		} // not much else I can do
		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {
		} // not much else I can do

	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isPartnerAwait() {
		return partnerAwait;
	}

	public void setPartnerAwait(boolean partnerAwait) {
		this.partnerAwait = partnerAwait;
	}

	public boolean isOpponentFind() {
		return opponentFind;
	}

	public void setOpponentFind(boolean opponentFind) {
		this.opponentFind = opponentFind;
	}

	public static boolean isItYourTurn() {
		return isItYourTurn;
	}

	public static void setItYourTurn(boolean isItYourTurn) {
		Player.isItYourTurn = isItYourTurn;
	}
}
