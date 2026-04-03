package protoactor26;
import io.javalin.Javalin;
import io.javalin.websocket.*;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProtoActorContext26 {
    private String name;
    private int port;
    private Javalin server;
    private Vector<WsConnectContext> allConns = new Vector<>();
    private Map<String, AbstractProtoactor26> protoactors = new ConcurrentHashMap<>();

    public ProtoActorContext26(String name, int port) {
        this.name = name;
        this.port = port;
        this.server = Javalin.create().start(port);
        configureWS();
    }

    public void register(AbstractProtoactor26 p) {
        protoactors.put(p.name, p);
        CommUtils.outgreen("Registered " + p.name + " in " + name);
    }

    private void configureWS() {
        server.ws("/eval", ws -> {
            ws.onConnect(ctx -> {
                allConns.add(ctx);
                CommUtils.outgreen(name + " | ws connected. Nconn=" + allConns.size());
            });
            ws.onMessage(ctx -> {
                IApplMessage am = readInput(ctx.message());
                CommUtils.outyellow("Context onMessage: " + am);
                IApplMessage answer = elabMsg(am, ctx);
                if (am.isRequest() && answer != null) ctx.send(answer.toString());
            });
        });
    }

    public IApplMessage elabMsg(IApplMessage am, WsMessageContext ctx) {
        AbstractProtoactor26 p = protoactors.get(am.msgReceiver());
        return (p != null) ? p.execMsg(am) : am;
    }

    public void emitInfo(IApplMessage ev) {
        allConns.forEach(c -> { if(c.session.isOpen()) c.send(ev.toString()); });
        protoactors.forEach((id, pa) -> pa.execMsg(ev));
    }

    private IApplMessage readInput(String m) {
        try { return new ApplMessage(m); } 
        catch (Exception e) { return ApplMessage.cvtJson(m); }
    }
}