package protoactor26;

import java.util.Observable;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;

public class CallerToProtoactor implements IObserver{
	private Interaction conn ;
	private String name;
 	
	public CallerToProtoactor(String name ) {
		this.name = name;
		doJob();
	}

	protected void doJob() {
		IApplMessage m     = CommUtils.buildDispatch(name, "do",  "2.0", "pa2");
	    IApplMessage req   = CommUtils.buildRequest(name, "eval", "0.0", "pa2");
		connectToServer();
		try {

//			CommUtils.outblue(name + " | request=" +req);
//			IApplMessage reply = conn.request(req);  //bloccante sincrona long
//			CommUtils.outcyan(name + " | reply=" + reply);
			
//			CommUtils.outblue(name + " | forward=" + m);
//			conn.forward(m);
			
			//Invio  request senza attendere risposta: vedi update
			conn.forward(req);
			
			CommUtils.delay(2000);
			
			CommUtils.outblue(name + " | ENDS");
			
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
	protected void connectToServer() {
		if( conn == null  )
		try {
			CommUtils.outgreen(name + " | connectToServer ..................... :");
			conn = WsConnection.create("localhost:8070", "eval",this);
			((WsConnection)conn).setTrace(true);
 		} catch (Exception e) {
 			e.printStackTrace();
		}		
	}

  


	@Override
	public void update(Observable o, Object arg) {
		//CommUtils.outblack(name + " | update 2 arg ");		
		update(""+arg);
	}

	@Override
	public void update(String value) {
		CommUtils.outblack(name + " | update:" + value);
	}

	
    public static void main(String[] args) {
    	new CallerToProtoactor("acalller");
     }

}
