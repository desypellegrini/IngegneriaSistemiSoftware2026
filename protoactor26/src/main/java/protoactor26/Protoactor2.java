package protoactor26;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;
 

public class Protoactor2 extends AbstractProtoactor26{
	private IApplMessage ev    = CommUtils.buildEvent(name, "info",  "pa2_working" );
	
	public Protoactor2(String name, ProtoActorContext26 ctx) {
		super(name,ctx);
	}
 
	
	@Override
	protected void elabDispatch(IApplMessage m) {
		CommUtils.outblue(name + " | elabDispatch:" + m);
		emitInfo( ev );
	}

	@Override
	protected IApplMessage elabRequest(IApplMessage req) {
		CommUtils.outblue(name + " | elabRequest:" + req);
		if( req.msgId().equals("eval")){
			double x      = Double.parseDouble(req.msgContent());
			double result = eval(x);
			IApplMessage replyMsg =
						CommUtils.buildReply(name,req.msgId(),""+result,req.msgSender());
			return replyMsg;
		}else{
			IApplMessage replyMsg =
			CommUtils.buildReply(name,req.msgId(),"requestUnkown",req.msgSender());
			return replyMsg;
		}
	}

	@Override
	protected void elabReply(IApplMessage r) {
		CommUtils.outblue(name + " | elabReply:" + r);
		
	}

	@Override
	protected void elabEvent(IApplMessage ev) {
		CommUtils.outblack(name + " | elabEvent:" + ev);
		
	}

	protected double eval(double x) {
		//CommUtils.outblue(name + " | eval: " + x);
		if (x > 4.0) {
			CommUtils.outmagenta(name + " | Simulo ritardo per x="+x);
			CommUtils.delay(10000);
		}
		return Math.sin(x) + Math.cos( Math.sqrt(3)*x);
	}

	protected void proactiveJob(  ) {
		
	}

}
