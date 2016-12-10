package model;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Ship {

	private String[][] shipTable;
	private int size;
	private boolean isDead = false;

	public Ship(int size) {
		this.shipTable = new String[size][3];
		this.size = size;
	}

	public void updateShip(int box, int row, int col, String state) {
		this.shipTable[box][0] = Integer.toString(row);
		this.shipTable[box][1] = Integer.toString(col);
		this.shipTable[box][2] = state;
	}

	public void updateShip(int box, String row, String col, String state) {
		this.shipTable[box][0] = row;
		this.shipTable[box][1] = col;
		this.shipTable[box][2] = state;
	}

	public int getMinRow() {
		int minRow = Integer.parseInt(this.shipTable[0][0]);
		for (int i = 0; i < this.size; i++) {
			if (shipTable[i][0] != null && minRow > Integer.parseInt(shipTable[i][0])) {
				minRow = Integer.parseInt(this.shipTable[i][0]);
			}
		}
		return minRow;
	}

	public int getMaxRow() {
		int maxRow = Integer.parseInt(this.shipTable[0][0]);
		for (int i = 0; i < this.size; i++) {
			if (shipTable[i][0] != null && maxRow < Integer.parseInt(shipTable[i][0])) {
				maxRow = Integer.parseInt(this.shipTable[i][0]);
			}
		}
		return maxRow;
	}

	public int getMinColumn() {
		int minCol = Integer.parseInt(this.shipTable[0][1]);
		for (int i = 0; i < this.size; i++) {
			if (shipTable[i][0] != null && minCol > Integer.parseInt(shipTable[i][1])) {
				minCol = Integer.parseInt(this.shipTable[i][1]);
			}
		}
		return minCol;
	}

	public int getMaxColumn() {
		int maxCol = Integer.parseInt(this.shipTable[0][1]);
		for (int i = 0; i < this.size; i++) {
			if (shipTable[i][0] != null && maxCol < Integer.parseInt(shipTable[i][1])) {
				maxCol = Integer.parseInt(this.shipTable[i][1]);
			}
		}
		return maxCol;
	}

	public boolean checkDeath() {
		int nbHit = 0;
		for (int i = 0; i < shipTable.length; i++) {
			if ("death".equals(shipTable[i][2])) {
				nbHit++;
			}
		}
		if (nbHit == size) {
			isDead = true;
		}
		return isDead;
	}

	public String[][] getShip() {
		return shipTable;
	}

	public void setShip(String[][] shipTable) {
		this.shipTable = shipTable;
	}

	public void display() {
		Logger log = Logger.getLogger(Ship.class.getName());
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < shipTable[i].length; j++) {
				log.log(Level.INFO, shipTable[i][j] + " ");
			}
			log.log(Level.INFO, " ");
		}
	}
}
