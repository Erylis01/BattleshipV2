package model;

public class BoardPlayer {

	private String[][] board = new String[10][10];
	private String empty = "empty";

	public BoardPlayer() {
		for (int row = 0; row < board[0].length; row++) {
			for (int column = 0; column < board.length; column++) {
				board[row][column] = empty;
			}
		}
	}

	public boolean nbBoxInRowEmpty(int row, int size) {
		int nbBoxEmpty = 0;
		boolean fit = false;
		for (int i = 0; i < board[row].length; i++) {
			if (empty.equals(board[row][i])) {
				nbBoxEmpty++;
			}
		}
		if (size <= nbBoxEmpty) {
			fit = true;
		}
		return fit;
	}

	public boolean nbBoxInColumnEmpty(int column, int size) {
		int nbBoxEmpty = 0;
		boolean fit = false;
		for (int i = 0; i < board.length; i++) {
			if (empty.equals(board[i][column])) {
				nbBoxEmpty++;
			}
		}
		if (size <= nbBoxEmpty) {
			fit = true;
		}
		return fit;
	}

	public boolean adjacentBoxColumn(Ship ship, int col, int row) {
		boolean adjacent = false;
		int prevCol = Integer.parseInt(ship.getShip()[0][1]);
		int minRow = ship.getMinRow();
		int maxRow = ship.getMaxRow();
		if ((prevCol == col) && ((maxRow + 1 == row) ^ (minRow - 1 == row))) {
			adjacent = true;
		}
		return adjacent;
	}

	public boolean adjacentBoxRow(Ship ship, int col, int row) {
		boolean adjacent = false;
		int prevRow = Integer.parseInt(ship.getShip()[0][0]);
		int minCol = ship.getMinColumn();
		int maxCol = ship.getMaxColumn();
		if (prevRow == row && (maxCol + 1 == col ^ minCol - 1 == col)) {
			adjacent = true;
		}
		return adjacent;
	}

	public void updateBoard(int row, int col, String s) {
		board[row][col] = s;
	}

	public String[][] getBoard() {
		return board;
	}

	public void setBoard(String[][] board) {
		this.board = board;
	}
}
