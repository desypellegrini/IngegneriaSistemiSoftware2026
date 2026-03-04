package main.java.conway.devices;

import main.java.conway.domain.IGrid;
import main.java.conway.domain.IOutDev;
import unibo.basicomm23.utils.CommUtils;

public class MockOutdev implements IOutDev{

	@Override
	public void display(String msg) {
		CommUtils.outblue(msg);
	}
	
	@Override
	public void close() {
		CommUtils.outcyan("MockOutdev closed");
	}

	@Override
	public void displayGrid(IGrid grid) {
		 System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"  );
		 System.out.println(grid.toString());
		 System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"  );
	}

	@Override
	public void displayCell(IGrid grid, int row, int col) {
		CommUtils.outcyan("cell x="+row + " y="+col + " " +grid.getCell(row, col).isAlive());		
	}


}
