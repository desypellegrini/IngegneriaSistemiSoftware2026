package main.java.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.conway.domain.Grid;


public class GridTest {
	private Grid grid;
	private final int nRows = 5;
	private final int nCols = 5;
	
	@Before
	public void setup() {
		System.out.println("GridTest | setup");	
		grid = new Grid(nRows,nCols);
	}
	
	@After
	public void down() {
		System.out.println("GridTest | down");
	}
	
	@Test
	public void testDims() {
		System.out.println("testDims ---------------------" );
		int nr = grid.getRows();
		int nc = grid.getCols();
		assertTrue( nr==nRows && nc==nCols );
	}
	
	@Test
	public void testCGridCellValue() {
		System.out.println("testCGridCellValue ---------------------" );
		grid.setCellState(0,0,true);
		assertTrue(   grid.getCellValue(0,0) );
		assertFalse(  grid.getCellValue(0,1) );
	}
	@Test
	public void testGridRep() {
		System.out.println("testGridRep ---------------------" );
 		System.out.println(""+grid);
		assertTrue( grid.toString().startsWith(". . . . ."));
	}
	@Test
	public void testPrintGrid() {
		System.out.println("testPrintGrid ---------------------" );
		grid.setCellState(0,0,true);
		grid.setCellState(0,1,true);
		grid.setCellState(0,2,true);
		grid.setCellState(0,3,true);
		grid.setCellState(0,4,true);
		//grid.printGrid();
	}
	
	
}
