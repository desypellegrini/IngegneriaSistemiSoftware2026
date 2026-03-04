package main.java.conway.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

import unibo.basicomm23.utils.CommUtils;

public class Grid implements IGrid{
	private int rows;
	private int cols;
	private Cell[][] cells;
    
	public Grid( int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		cells = new Cell[rows][cols];	
		initGrid();
	}

	protected void initGrid() {
		  CommUtils.outyellow("Grid | initGrid rows=" + rows + " cols=" + cols);
		  for (int i = 0; i < rows; i++) {
		     for (int j = 0; j < cols; j++) {
		    	 cells[i][j] = new Cell();
		     }
		  }
		  CommUtils.outyellow("Grid | initGrid done");
	  }	

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public int getCols() {
		return cols;
	}

	@Override
	public Cell getCell(int row, int col) {
		return cells[row][col];
	}

	@Override
	public void setCellState(int row, int col, boolean state) {
		cells[row][col].setStatus(state);
	}

	@Override
	public boolean getCellValue(int row, int col) {
		return cells[row][col].isAlive();
	}

	@Override
	public void reset() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cells[i][j].setStatus(false);
			}
		}
		
	}
	
	public String toString() {
	    return Arrays.stream(cells) // Stream di Cell[] (le righe)
        .map(row -> {
            // Trasformiamo ogni riga in una stringa di . e O
            StringBuilder sb = new StringBuilder();
            for (Cell cell : row) {
                sb.append(cell.isAlive() ? "O " : ". ");
            }
            return sb.toString();
        })
        .collect(Collectors.joining("\n")); // Uniamo le righe con un a capo  
  }
}
