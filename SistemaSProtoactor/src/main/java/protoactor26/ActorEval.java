package protoactor26;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class ActorEval extends AbstractProtoactor26 {

    public ActorEval(String name, ProtoActorContext26 ctx) {
        super(name, ctx);
    }

    @Override
    protected void elabDispatch(IApplMessage m) {
        CommUtils.outgreen(name + " | elabDispatch from " + m.msgSender());
    }

    @Override
    protected IApplMessage elabRequest(IApplMessage req) {
        CommUtils.outblue(name + " | elabRequest: " + req);
        if (req.msgId().equals("eval")) {
            double x = Double.parseDouble(req.msgContent());
            // La funzione del prof
            double result = Math.sin(x) + Math.cos(Math.sqrt(3) * x);
            
            if (x > 4.0) {
                CommUtils.outmagenta(name + " | Simulo ritardo per x=" + x);
                CommUtils.delay(3000);
            }
            
            String resMsg = "f(" + x + "," + result + ")";
            return CommUtils.buildReply(name, req.msgId(), resMsg, req.msgSender());
        }
        return CommUtils.buildReply(name, req.msgId(), "requestUnknown", req.msgSender());
    }

    @Override 
    protected void elabReply(IApplMessage req) {}
    
    @Override 
    protected void elabEvent(IApplMessage ev) { CommUtils.outblue(name + " | perceives: " + ev); }

    @Override
    protected void proactiveJob() {
        CommUtils.outgreen(name + " | proactiveTask started");
        // Emette un evento ogni 2 secondi 
        msgexecutor.scheduleAtFixedRate(() -> {
            String time = java.time.LocalTime.now().toString();
            IApplMessage evtime = CommUtils.buildEvent(name, "info", time);
            emitInfo(evtime);
        }, 2, 2, java.util.concurrent.TimeUnit.SECONDS);
    }
}