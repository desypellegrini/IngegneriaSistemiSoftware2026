package conway26appl;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
//import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.CommUtilsOrig;
import unibo.basicomm23.ws.WsConnection;
import java.util.Observable;

import main.java.conway.domain.GameController;
import main.java.conway.domain.ICell;
import main.java.conway.domain.IOutDev;
import main.java.conway.domain.LifeInterface;
import main.java.conway.domain.Life;
  
/*
 * PREMESSA: lanciare MainConwayGui
 */

public class LifeGameInteraction  {  
	 private String name;
	 private Interaction wsconn;
	 
    public LifeGameInteraction( String name ) throws Exception {
    	this.name = name;
    	setUp();
      }
    
     protected void setUp() throws Exception{
       LifeInterface life   = new Life( 20,20 );            //ncell in iomap.js        
 	   IOutDev iodevgui     = new OutInGuiInteraction(  );   		//dispositivo di output e anche di input 
 	   GameController  cc   = new LifeControllerAdhoc(life, iodevgui) ;   
 	   ((OutInGuiInteraction) iodevgui).setController(cc);          //iniezione del controller nella GUI
 	}


     
    
    public static void main(String[] args) throws Exception {
    	System.out.println("LifeGameInteraction Java.version="+ System.getProperty("java.version"));
    	new LifeGameInteraction("lifectrl");
     }
}

