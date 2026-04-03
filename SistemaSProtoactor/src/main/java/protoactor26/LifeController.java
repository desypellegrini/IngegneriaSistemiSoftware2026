package protoactor26;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;
import java.util.concurrent.TimeUnit;

public class LifeController extends AbstractProtoactor26 {
    private boolean running = false;

    public LifeController(String name, ProtoActorContext26 ctx) {
        super(name, ctx);
    }

    @Override
    protected void elabDispatch(IApplMessage m) {
        CommUtils.outgreen(name + " | Ricevuto comando: " + m.msgId());
        if (m.msgId().equals("start")) running = true;
        if (m.msgId().equals("stop"))  running = false;
    }

    @Override
    protected IApplMessage elabRequest(IApplMessage req) {
        if (req.msgId().equals("getState")) {
            return CommUtils.buildReply(name, "getState", "stato_griglia_qui", req.msgSender());
        }
        return null;
    }

    @Override
    protected void elabReply(IApplMessage req) {}
    @Override
    protected void elabEvent(IApplMessage ev) { 
        CommUtils.outmagenta(name + " | Evento percepito: " + ev.msgId()); 
    }

    @Override
    protected void proactiveJob() {
        // Ogni secondo, se il gioco corre, facciamo un passo avanti
        msgexecutor.scheduleAtFixedRate(() -> {
            if (running) {
                // life.nextGeneration();
                CommUtils.outyellow(name + " | Generazione calcolata");
                // Informiamo tutti i browser collegati tramite un evento
                IApplMessage updateEv = CommUtils.buildEvent(name, "update", "griglia_aggiornata");
                emitInfo(updateEv);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}