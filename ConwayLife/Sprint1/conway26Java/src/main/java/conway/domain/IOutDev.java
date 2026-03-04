package main.java.conway.domain;
/*
 * Contratto definito dalla business logic
 */
public interface IOutDev {
	public void display(String msg);      //For HMI
	public void displayCell(IGrid grid, int row, int col);   
	public void close();
	public void displayGrid(IGrid grid);

}
