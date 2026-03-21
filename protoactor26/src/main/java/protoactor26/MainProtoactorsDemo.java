package protoactor26;

public class MainProtoactorsDemo {

    public static void main(String[] args) {
    	ProtoActorContext26 ctx  = new ProtoActorContext26("ctx8070",8070);
    	AbstractProtoactor26 pa2 = new Protoactor2("pa2",ctx);
    	AbstractProtoactor26 pa1 = new Protoactor1("pa1",ctx);
     }
}
