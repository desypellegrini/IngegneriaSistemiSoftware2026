package protoactor26;

import java.util.Observable;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.IObserver;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;

public class CallerToProtoactor implements IObserver {
    private Interaction conn;
    private String name;
    private String destName = "sistemaS"; 

    public CallerToProtoactor(String name) {
        this.name = name;
        connectToServer();
        doJob();
    }

    protected void doJob() {
        // Messaggi pronti per "sistemaS"
        IApplMessage m = CommUtils.buildDispatch(name, "do", "2.0", destName);
        IApplMessage req = CommUtils.buildRequest(name, "eval", "0.0", destName);
        
        try {
            // 1. Richiesta sincrona (attende la risposta)
            CommUtils.outblue(name + " | invio richiesta sincrona=" + req);
            IApplMessage reply = conn.request(req); 
            CommUtils.outcyan(name + " | ricevuta reply=" + reply);
            
            // 2. Invio dispatch (fire and forget)
            CommUtils.outblue(name + " | invio dispatch=" + m);
            conn.forward(m);
            
            // 3. Invio richiesta asincrona (la risposta arriverà nel metodo update)
            CommUtils.outblue(name + " | invio richiesta asincrona=" + req);
            conn.forward(req);
            
            CommUtils.delay(5000); // Aspettiamo per vedere l'update
            CommUtils.outblue(name + " | FINE LAVORO");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void connectToServer() {
        if (conn == null)
            try {
                CommUtils.outgreen(name + " | Connessione al server porta 8050...");
                conn = WsConnection.create("localhost:8050", "eval", this);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void update(Observable o, Object arg) { update("" + arg); }

    @Override
    public void update(String value) {
        CommUtils.outyellow(name + " | ricevuto update asincrono: " + value);
    }

    public static void main(String[] args) {
        new CallerToProtoactor("acaller");
    }
}