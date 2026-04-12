package conway26appl.protoactor0;

import protoactor26.AbstractProtoactor26;
import protoactor26.ProtoActorContextInterface;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class LifeSidecar extends AbstractProtoactor26 {
    protected Interaction connToGui;

    public LifeSidecar(String name, ProtoActorContextInterface ctx) {
        super(name, ctx);
        connToGui = ConnectionFactory.createClientSupport(ProtocolType.tcp, "localhost", "8050");
        CommUtils.outmagenta(name + " | Connesso al GuiServer via TCP");
    }

    @Override
    protected void elabDispatch(IApplMessage m) {
        // Quando il Controller gli manda un dispatch (es. display), lui lo inoltra
        try {
            CommUtils.outcyan(name + " | Sidecar inoltra Dispatch: " + m.msgId());
            connToGui.forward(m);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    protected IApplMessage elabRequest(IApplMessage req) {
        // Se il Controller dovesse fare richieste esterne, passano di qui
        try {
            return connToGui.request(req);
        } catch (Exception e) { return null; }
    }

    @Override protected void elabReply(IApplMessage m) { }
    @Override protected void elabEvent(IApplMessage ev) { }
    @Override protected void proactiveJob() { }
}
