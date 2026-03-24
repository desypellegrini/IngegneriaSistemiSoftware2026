package protoactor26;

public class MainSistemaSProto {
	public static void main(String[] args) {
        // Avvio il contesto (che include il server Javalin) sulla porta 8070
        ProtoActorContext26 ctx = new ProtoActorContext26("ctxCalcolo", 8070);
        
        // Creo l'attore e lo registro col nome "pacalculator"
        new ActorEval("pacalculator", ctx);
        
        System.out.println("SISTEMA A PROTOATTORI PRONTO SULLA PORTA 8070");
    }
}
