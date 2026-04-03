package protoactor26;

public class MainSistemaSProto {
    public static void main(String[] args) {
        // Usiamo la porta 8050 
        ProtoActorContext26 ctx = new ProtoActorContext26("ctx8050", 8050);
        
        // Creiamo l'attore di calcolo "sistemaS"
        new ActorEval("sistemaS", ctx);
        
        System.out.println("SISTEMA PRONTO SULLA PORTA 8050");
    }
}