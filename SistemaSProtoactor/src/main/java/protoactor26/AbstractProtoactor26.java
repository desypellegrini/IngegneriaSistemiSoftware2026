package protoactor26; 
import java.util.concurrent.*;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public abstract class AbstractProtoactor26 {
    protected String name;
    protected ProtoActorContext26 context;
    protected ScheduledExecutorService msgexecutor = Executors.newSingleThreadScheduledExecutor();

    public AbstractProtoactor26(String name, ProtoActorContext26 ctx) {
        this.name = name;
        this.context = ctx;
        ctx.register(this); 
        proactiveJob();
    }

    protected abstract void elabDispatch(IApplMessage m);    
    protected abstract IApplMessage elabRequest(IApplMessage req);
    protected abstract void elabReply(IApplMessage req);
    protected abstract void elabEvent(IApplMessage ev);
    protected abstract void proactiveJob();

    public IApplMessage execMsg(IApplMessage am) {
        if (am.isEvent()) {
            msgexecutor.execute(() -> elabEvent(am));
        } else if (am.isDispatch()) {
            msgexecutor.execute(() -> elabDispatch(am));
        } else if (am.isRequest()) { 
            return dorequestSynch(am);
        } else if (am.isReply()) {
            msgexecutor.execute(() -> elabReply(am));
        }
        return null;
    }

    protected IApplMessage dorequestSynch(IApplMessage am) {
        try {
            Future<IApplMessage> res = msgexecutor.submit(() -> elabRequest(am));  
            return res.get(); 
        } catch (Exception e) { return null; }              
    }

    protected void forward(IApplMessage msg) { context.elabMsg(msg, null); }
    protected void emitInfo(IApplMessage ev) { context.emitInfo(ev); }
}