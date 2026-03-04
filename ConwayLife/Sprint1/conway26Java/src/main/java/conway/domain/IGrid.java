package main.java.conway.domain;

public interface IGrid {
	public int getRows();
    public int getCols();
    public void setCellState(int row, int col, boolean state);
    public ICell getCell(int row, int col);
    public boolean getCellValue(int row, int col);
    public void reset();
}
