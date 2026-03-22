package conwayGuiPageServer;
import conway.io.IoJavalin;
import conway26appl.LifeGameInteraction;
//import main.java.conway.devices.OutInWs;
//import main.java.conway.domain.*;
import unibo.basicomm23.utils.CommUtils;
public class MainConwayGui {

    public MainConwayGui() {
        CommUtils.outgreen("MainConway | STARTS");
        
        // 1. Avvio del server (Job1)
        new IoJavalin("guiserver");

        // 2. Avvio della logica e interconnessione (Job2)
        try {
            // Creo l'interazione che monta Life, Controller e OutDev
            new LifeGameInteraction("lifectrl");
        } catch (Exception e) {
            CommUtils.outred("MainConway | Errore durante l'inizializzazione della logica: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        var resource = MainConwayGui.class.getResource("/page");
        CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);

        new MainConwayGui();
        System.out.println("MainConway | ENDS");
    }
}