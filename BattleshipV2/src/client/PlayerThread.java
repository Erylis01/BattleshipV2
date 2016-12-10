package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controller.GameController;
import metier.Command;

public class PlayerThread extends Thread {

	private Socket socket;
	private ObjectInputStream Input; // to read from the socket
	private ObjectOutputStream Output; // to write on the socket
	private Command command;

	private boolean isDead = false;
	private GameController game;
	private boolean isItYourTurn;

	public PlayerThread(Socket socket, ObjectInputStream Input, ObjectOutputStream Output, GameController game,
			boolean isItYourTurn) {
		this.socket = socket;
		this.Input = Input;
		this.Output = Output;
		this.game = game;
		this.setItYourTurn(isItYourTurn);
	}

	public void run() {
		System.out.println("le client " + socket.getLocalAddress() + " s'est connecté");
		while (!isDead) {
			try {
				command = (Command) Input.readObject();
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
			switch (command.getType()) {
			case Command.HIT:
				System.out.println("LE THREAD A BIEN RECUPERE LE HIT");
				boolean touched = game.checkIfTouch(command.getPosX(), command.getPosY());
				if (touched) {
					if (game.checkAllShipDeath()) {
						sendMessage(new Command(Command.WIN, "Vous avez gagné!!!!"));
						game.updateOpponentTouchedCell(command.getPosX(), command.getPosY());
						game.displayText("Vous avez perdu!! Vous êtes vraiment trop nul...");
						isDead=false;
					} else {
						sendTouched(command.getPosX(), command.getPosY());
						game.updateOpponentTouchedCell(command.getPosX(), command.getPosY());
					}
				} else {
					sendMissed(command.getPosX(), command.getPosY());
					game.updateOpponentMissedCell(command.getPosX(), command.getPosY());
				}
				Player.setItYourTurn(true);
				String strAsciiTab = Character.toString((char) (command.getPosX() + 65));
				game.displayText("Impact en " + strAsciiTab + command.getPosY() + "! A vous de jouer");
				break;
			case Command.MISS:
				game.updatePlayerMissedCell(command.getPosX(), command.getPosY());
				System.out.println("LE THREAD A BIEN RECUPERE LE MISS");
				game.displayText("Loupé ! Au tour adverse");
				game.activeFrame();
				break;
			case Command.TOUCHED:
				game.updatePlayerTouchedCell(command.getPosX(), command.getPosY());
				System.out.println("LE THREAD A BIEN RECUPERE LE TOUCHED");
				game.displayText("Touché ! Au tour adverse");
				game.activeFrame();
				break;
			case Command.WIN:
				System.out.println("LE THREAD A BIEN RECUPERE LE WIN");
				game.updatePlayerTouchedCell(command.getPosX(), command.getPosY());
				game.displayText("Vous avez gangé!!!");
				isDead = true;
				break;
			}
		}
	}

	/*
	 * To send a message to the server
	 */
	public void sendMessage(Command msg) {
		try {
			Output.writeObject(msg);
		} catch (IOException e) {
			System.out.println("Exception writing to server: " + e);
		}
	}

	public void sendHit(int posX, int posY) {
		sendMessage(new Command(Command.HIT, posX, posY));
		setItYourTurn(false);
	}

	private void sendTouched(int posX, int posY) {
		sendMessage(new Command(Command.TOUCHED, posX, posY));
		setItYourTurn(false);
	}

	private void sendMissed(int posX, int posY) {
		sendMessage(new Command(Command.MISS, posX, posY));
		setItYourTurn(false);
	}

	public boolean isItYourTurn() {
		return isItYourTurn;
	}

	public void setItYourTurn(boolean isItYourTurn) {
		this.isItYourTurn = isItYourTurn;
	}

}
