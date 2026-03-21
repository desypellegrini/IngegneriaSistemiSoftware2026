package conwayGuiPageServer;
import conway.io.IoJavalin;
//import main.java.conway.devices.OutInWs;
//import main.java.conway.domain.*;
import unibo.basicomm23.utils.CommUtils;
public class MainConwayGui  {

   	
   	public MainConwayGui() {
	    CommUtils.outgreen("MainConway | STARTS " );  
  		new IoJavalin("guiserver");
  	}
  	
    public static void main(String[] args) {
	    
		var resource = MainConwayGui.class.getResource("/page");
		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);

	    MainConwayGui app = new MainConwayGui();
	    System.out.println("MainConway | ENDS " );  
    }

}