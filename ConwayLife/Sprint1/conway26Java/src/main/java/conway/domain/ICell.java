package main.java.conway.domain;

public interface ICell {
	boolean isAlive();
	void setStatus(boolean status);
	void switchCellState();
}
