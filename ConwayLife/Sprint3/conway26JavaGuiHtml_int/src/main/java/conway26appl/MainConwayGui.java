package conway26appl;

import conway.io.OutInWs;
import main.java.conway.domain.*;
import unibo.basicomm23.utils.CommUtils;

public class MainConwayGui  {
   	private OutInWs server = new OutInWs();
   	
   	public void configureTheSystemWithHtmlWs(boolean pageexternal) {
   		Life life            = new Life( 20,20 );             //ncell in iomap.js
        GameController  cc   = new LifeController(life, server) ;   //un GameController che deve usare un outdev
        ((OutInWs) server).setController(cc);          //iniezione del controller nella GUI
 	}
  	
    public static void main(String[] args) {
	    System.out.println("MainConway | STARTS " );  
	    
		var resource = MainConwayGui.class.getResource("/page");
		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);

	    MainConwayGui app = new MainConwayGui();
	    app.configureTheSystemWithHtmlWs(true);
	    
	    System.out.println("MainConway | ENDS " );  
    }

}