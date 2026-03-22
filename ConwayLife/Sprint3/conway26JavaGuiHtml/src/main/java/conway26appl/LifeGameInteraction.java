package conway26appl;
import unibo.basicomm23.utils.CommUtils;
import main.java.conway.domain.GameController;
import main.java.conway.domain.IOutDev;
import main.java.conway.domain.LifeInterface;
import main.java.conway.domain.Life;

public class LifeGameInteraction {  
    private String name;

    public LifeGameInteraction( String name ) {
        this.name = name;
        try {
            setUp();
        } catch (Exception e) {
            CommUtils.outred(name + " | Errore durante il setUp: " + e.getMessage());
        }
    }

    protected void setUp() throws Exception {
        // 1. Creazione del Modello (JAR Sprint 1)
        LifeInterface life = new Life( 20, 20 );           
        
        // 2. Creazione dell'Output Device (Adattatore WS)
        IOutDev iodevgui = new OutInGuiInteraction();        
        
        // 3. Creazione del Controller
        GameController cc = new LifeControllerAdhoc(life, iodevgui);   
        
        // 4. Iniezione del controller (Back-injection)
        ((OutInGuiInteraction) iodevgui).setController(cc);
        
        CommUtils.outgreen(name + " | Configurazione completata con successo.");
    }

    public static void main(String[] args) {
        System.out.println("LifeGameInteraction Java.version=" + System.getProperty("java.version"));
        //new LifeGameInteraction("lifectrl");
    }
}