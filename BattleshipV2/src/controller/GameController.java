package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;

import client.Player;
import model.BoardPlayer;
import model.Ship;
import view.Draughtboard;

public class GameController implements ActionListener {

	private Draughtboard view;
	private BoardPlayer boardPlayer;
	private Player player;

	// Variable pour création et placement des bateaux
	private Ship[] listShip = new Ship[5];
	private Ship carrier = new Ship(5,"porte-avion");
	private Ship battleship = new Ship(4,"croiseur");
	private Ship cruiser = new Ship(3,"contre-torpilleur");
	private Ship submarine = new Ship(3,"sous-marin");
	private Ship destroyer = new Ship(2,"torpilleur");
	private String empty = "empty";
	private int size = 0;
	private boolean inCol = false;
	private boolean inRow = false;

	// Permet de savoir si les bateaux sont tous placés
	private boolean isGameSet;
	// Permet de savoir si les bateaux sont tous coulés
	private boolean allShipDead=false;

	public GameController(BoardPlayer boardPlayer, Player player) {
		this.boardPlayer = boardPlayer;
		this.player = player;
		this.view = new Draughtboard(this);
	}

	public void shipsPlacement(JTextArea console, int col, int row) {
		if (size < 5) {
			console.setText("Placer votre porte-avions (5 cases)");
			carrierPlacement(col, row, console);
		} else if (size >= 5 && size < 9) {
			battleshipPlacement(col, row, console);
		} else if (size >= 9 && size < 12) {
			cruiserPlacement(col, row, console);
		} else if (size >= 12 && size < 15) {
			submarinePlacement(col, row, console);
		} else if (size >= 15 && size < 17) {
			destroyerPlacement(col, row, console);
		}
	}

	public void carrierPlacement(int col, int row, JTextArea console) {
		if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 0) {
			updateOneCell(carrier, row, col);
			size++;
		} else if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 1) {
			if ((carrier.getShip()[0][0].equals(Integer.toString(row + 1))
					|| carrier.getShip()[0][0].equals(Integer.toString(row - 1)))
					&& carrier.getShip()[0][1].equals(Integer.toString(col))) {
				setInCol(true);
				updateOneCell(carrier, row, col);
				size++;
			} else if ((carrier.getShip()[0][1].equals(Integer.toString(col + 1))
					|| carrier.getShip()[0][1].equals(Integer.toString(col - 1)))
					&& carrier.getShip()[0][0].equals(Integer.toString(row))) {
				setInRow(true);
				updateOneCell(carrier, row, col);
				size++;
			}

		} else if ((empty.equals(boardPlayer.getBoard()[row][col])) && size >= 2) {
			if (inCol && !inRow) {
				if (boardPlayer.adjacentBoxColumn(carrier, col, row)) {
					updateOneCell(carrier, row, col);
					size++;
				}
			} else if (inRow && !inCol) {
				if (boardPlayer.adjacentBoxRow(carrier, col, row)) {
					updateOneCell(carrier, row, col);
					size++;
				}
			}
			if (size == 5) {
				listShip[0] = carrier;
				console.setText("Placer votre croiseur (4 cases)");
			}
		}
	}

	public void battleshipPlacement(int col, int row, JTextArea console) {
		if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 5) {
			updateOneCell(battleship, row, col);
			setInRow(false);
			setInCol(false);
			size++;
		} else if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 6) {
			if ((battleship.getShip()[0][0].equals(Integer.toString(row + 1))
					|| battleship.getShip()[0][0].equals(Integer.toString(row - 1)))
					&& battleship.getShip()[0][1].equals(Integer.toString(col))) {
				setInCol(true);
				updateOneCell(battleship, row, col);
				size++;
			} else if ((battleship.getShip()[0][1].equals(Integer.toString(col + 1))
					|| battleship.getShip()[0][1].equals(Integer.toString(col - 1)))
					&& battleship.getShip()[0][0].equals(Integer.toString(row))) {
				setInRow(true);
				updateOneCell(battleship, row, col);
				size++;
			}
		} else if ((empty.equals(boardPlayer.getBoard()[row][col])) && size >= 7) {
			if (inCol && !inRow) {
				if (boardPlayer.adjacentBoxColumn(battleship, col, row)) {
					updateOneCell(battleship, row, col);
					size++;
				}
			} else if (inRow && !inCol) {
				if (boardPlayer.adjacentBoxRow(battleship, col, row)) {
					updateOneCell(battleship, row, col);
					size++;
				}
			}
			if (size == 9) {
				listShip[1] = battleship;
				console.setText("Placer votre contre-torpilleurs (3 cases)");
			}
		}
	}

	public void cruiserPlacement(int col, int row, JTextArea console) {
		if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 9) {
			updateOneCell(cruiser, row, col);
			setInRow(false);
			setInCol(false);
			size++;
		} else if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 10) {
			if ((cruiser.getShip()[0][0].equals(Integer.toString(row + 1))
					|| cruiser.getShip()[0][0].equals(Integer.toString(row - 1)))
					&& cruiser.getShip()[0][1].equals(Integer.toString(col))) {
				setInCol(true);
				updateOneCell(cruiser, row, col);
				size++;
			} else if ((cruiser.getShip()[0][1].equals(Integer.toString(col + 1))
					|| cruiser.getShip()[0][1].equals(Integer.toString(col - 1)))
					&& cruiser.getShip()[0][0].equals(Integer.toString(row))) {
				setInRow(true);
				updateOneCell(cruiser, row, col);
				size++;
			}
		} else if ((empty.equals(boardPlayer.getBoard()[row][col])) && size >= 11) {
			if (inCol && !inRow) {
				if (boardPlayer.adjacentBoxColumn(cruiser, col, row)) {
					updateOneCell(cruiser, row, col);
					size++;
				}
			} else if (inRow && !inCol) {
				if (boardPlayer.adjacentBoxRow(cruiser, col, row)) {
					updateOneCell(cruiser, row, col);
					size++;
				}
			}
			if (size == 12) {
				listShip[2] = cruiser;
				console.setText("Placer votre sous-marin (3 cases)");
			}
		}
	}

	public void submarinePlacement(int col, int row, JTextArea console) {
		if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 12) {
			updateOneCell(submarine, row, col);
			setInRow(false);
			setInCol(false);
			size++;
		} else if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 13) {
			if ((submarine.getShip()[0][0].equals(Integer.toString(row + 1))
					|| submarine.getShip()[0][0].equals(Integer.toString(row - 1)))
					&& submarine.getShip()[0][1].equals(Integer.toString(col))) {
				setInCol(true);
				updateOneCell(submarine, row, col);
				size++;
			} else if ((submarine.getShip()[0][1].equals(Integer.toString(col + 1))
					|| submarine.getShip()[0][1].equals(Integer.toString(col - 1)))
					&& submarine.getShip()[0][0].equals(Integer.toString(row))) {
				setInRow(true);
				updateOneCell(submarine, row, col);
				size++;
			}
		} else if ((empty.equals(boardPlayer.getBoard()[row][col])) && size >= 14) {
			if (inCol && !inRow) {
				if (boardPlayer.adjacentBoxColumn(submarine, col, row)) {
					updateOneCell(submarine, row, col);
					size++;
				}
			} else if (inRow && !inCol) {
				if (boardPlayer.adjacentBoxRow(submarine, col, row)) {
					updateOneCell(submarine, row, col);
					size++;
				}
			}
			if (size == 15) {
				listShip[3] = submarine;
				console.setText("Placer votre torpilleur (2 cases)");
			}
		}
	}

	public void destroyerPlacement(int col, int row, JTextArea console) {
		if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 15) {
			updateOneCell(destroyer, row, col);
			setInRow(false);
			setInCol(false);
			size++;
		} else if (empty.equals(boardPlayer.getBoard()[row][col]) && size == 16) {
			if ((destroyer.getShip()[0][0].equals(Integer.toString(row + 1))
					|| destroyer.getShip()[0][0].equals(Integer.toString(row - 1)))
					&& destroyer.getShip()[0][1].equals(Integer.toString(col))) {
				setInCol(true);
				updateOneCell(destroyer, row, col);
				console.setText("Vous avez placé tous vos bateaux!\n Cliquez sur connecter pour commencer.");
				size++;
			} else if ((destroyer.getShip()[0][1].equals(Integer.toString(col + 1))
					|| destroyer.getShip()[0][1].equals(Integer.toString(col - 1)))
					&& destroyer.getShip()[0][0].equals(Integer.toString(row))) {
				setInRow(true);
				updateOneCell(destroyer, row, col);
				console.setText("Vous avez placé tous vos bateaux!\n Cliquez sur connecter pour commencer.");
				size++;

			}
			listShip[4] = destroyer;
			isGameSet = true;
			System.out.println(isGameSet);
		}
	}

	public void updateOneCell(Ship ship, int row, int col) {
		boardPlayer.updateBoard(row, col, "full");
		if (ship.equals(carrier)) {
			ship.updateShip(size, row, col, "safe");
		} else if (ship.equals(battleship)) {
			ship.updateShip(size - 5, row, col, "safe");
		} else if (ship.equals(cruiser)) {
			ship.updateShip(size - 9, row, col, "safe");
		} else if (ship.equals(submarine)) {
			ship.updateShip(size - 12, row, col, "safe");
		} else if (ship.equals(destroyer)) {
			ship.updateShip(size - 15, row, col, "safe");
		}
		Component c = view.getBoardPlayer().getComponentAt(col * 34, row * 34);
		c.setBackground(Color.blue);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		for (Component c : view.getBoardPlayer().getComponents()) {
			if (a.getSource() == c) {
				int posX = c.getX() / (340 / 10);
				int posY = c.getY() / (340 / 10);
				shipsPlacement(view.getConsole(), posX, posY);
			}
		}

		if (a.getSource() == view.getBtnConnection() && isGameSet) {
			player.setPseudo(view.getFieldPseudo().getText());
			player.setPort(getPort());
			player.start(this);
		}

		for (Component c : view.getBoardOpponent().getComponents()) {
			if (a.getSource() == c && player.isOpponentFind() && Player.isItYourTurn()) {
				int posX = c.getX() / (340 / 10);
				int posY = c.getY() / (340 / 10);
				player.sendHit(posX, posY);

			}
		}
	}

	public boolean checkIfTouch(int col, int row) {
		boolean touched = false;
		loopShip: for (Ship s : listShip) {
			String[][] st = s.getShip();
			for (int i = 0; i < st.length; i++) {
				if (Integer.toString(col).equals(st[i][1]) && Integer.toString(row).equals(st[i][0])) {
					s.updateShip(i, row, col, "touched");
					touched = true;
					break loopShip;
				}
			}
		}
		return touched;
	}

	public boolean checkAllShipDeath(){
		int nbShipDead = 0;
		for (Ship s : listShip){
			if (s.checkDeath()){
				nbShipDead++;
			}
		}
		if(nbShipDead==listShip.length){
			allShipDead =true;
		}
		return allShipDead;
	}
	
	public void activeFrame() {
		this.view.getFrame().requestFocus();
		this.view.getFrame().revalidate();
		this.view.getFrame().repaint();
	}

	public void updateOpponentTouchedCell(int col, int row){
		boardPlayer.updateBoard(row, col, "touched");
		Component c = view.getBoardPlayer().getComponentAt(col * 34, row * 34);
		c.setBackground(Color.RED);

	}
	
	public void updateOpponentMissedCell(int col, int row){
		boardPlayer.updateBoard(row, col, "hit");
		Component c = view.getBoardPlayer().getComponentAt(col * 34, row * 34);
		c.setBackground(Color.GREEN);
	}
	
	public void updatePlayerTouchedCell(int col, int row){
		Component c = view.getBoardOpponent().getComponentAt(col * 34, row * 34);
		c.setBackground(Color.RED);

	}
	
	public void updatePlayerMissedCell(int col, int row){
		Component c = view.getBoardOpponent().getComponentAt(col * 34, row * 34);
		c.setBackground(Color.GREEN);
	}
	
	public int getPort() {
		return Integer.parseInt(view.getFieldPort().getText());
	}

	public boolean isGameSet() {
		return isGameSet;
	}

	public void setGameSet(boolean isGameSet) {
		this.isGameSet = isGameSet;
	}

	public String getPseudo() {
		return view.getFieldPseudo().getText();
	}

	public boolean isInCol() {
		return inCol;
	}

	public void setInCol(boolean inCol) {
		this.inCol = inCol;
	}

	public boolean isInRow() {
		return inRow;
	}

	public void setInRow(boolean inRow) {
		this.inRow = inRow;
	}

	public void displayText(String msg) {
		this.view.getConsole().setText(msg);
	}
}
