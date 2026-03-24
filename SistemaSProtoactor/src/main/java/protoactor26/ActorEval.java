package protoactor26;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class ActorEval extends AbstractProtoactor26{

	public ActorEval(String name, ProtoActorContext26 ctx) {
        super(name, ctx);
    }

	@Override
	protected void elabDispatch(IApplMessage m) {
		
	}

	@Override
    protected IApplMessage elabRequest(IApplMessage req) {
        CommUtils.outblue(name + " | elabora richiesta: " + req.msgContent());
        try {
            double x = Double.parseDouble(req.msgContent());
            double res = Math.sin(x) + Math.cos(Math.sqrt(3) * x);
            if (x > 4.0) 
            	CommUtils.delay(5000); // Ritardo simulato
            return CommUtils.buildReply(name, req.msgId(), "" + res, req.msgSender());
        } catch (Exception e) {
            return CommUtils.buildReply(name, req.msgId(), "errore", req.msgSender());
        }
    }

	@Override
	protected void elabReply(IApplMessage req) {
		
	}

	@Override
	protected void elabEvent(IApplMessage ev) {
		
	}

	@Override
	protected void proactiveJob() {
		
	}

}
