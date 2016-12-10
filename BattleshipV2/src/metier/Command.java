package metier;

import java.io.Serializable;

import controller.GameController;

public class Command implements Serializable {

	private static final long serialVersionUID = -8587913443098248304L;
	public static final int AJOUT = 0, OKAJOUT = 1, ADVERSAIRE = 2, OKADVERSAIRE = 3, NOADVERSAIRE = 4, HIT = 5,
			TOUCHED = 6, MISS = 7, WIN = 8;
	private String msg;
	private int type;
	private boolean bool;
	private int posX;
	private int posY;

	public Command(int type, String msg) {
		this.setType(type);
		this.setMsg(msg);
	}

	public Command(int type, Boolean bool) {
		this.setType(type);
		this.setBool(bool);
	}

	public Command(int type, int posX, int posY) {
		this.setType(type);
		this.setPosX(posX);
		this.setPosY(posY);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public void display(){
		System.out.println("type: "+type+ " msg: "+msg+" bool: "+bool+" posX: "+posX+" posY: "+posY);
	}
}
