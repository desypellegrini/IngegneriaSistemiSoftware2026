package conway.io;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsMessageContext;
import main.java.conway.domain.GameController;
import main.java.conway.domain.IGrid;
import main.java.conway.domain.IOutDev;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;

public class OutInWs implements IOutDev{
	
	private WsMessageContext owner = null ;
	private Set<WsMessageContext> spettatori = new HashSet<WsMessageContext>() ;
	private GameController cc;
	
	public OutInWs() {
        var app = Javalin.create(config -> {
			config.staticFiles.add(staticFiles -> {
				staticFiles.directory = "/page";
				staticFiles.location = Location.CLASSPATH; // Cerca dentro il JAR/Classpath
				/*
				 * i file sono "impacchettati" con il codice, non cercati sul disco rigido esterno.
				 */
		    });
		}).start(8080);
 
/*
 * --------------------------------------------
 * Parte HTTP        
 * --------------------------------------------
 */
        app.get("/", ctx -> {
    		//Path path = Path.of("./src/main/resources/page/ConwayInOutPage.html");    		    
        	/*
        	 * Java cercherà il file all'interno del Classpath 
        	 * (dentro il JAR o nelle cartelle dei sorgenti di Eclipse), 
        	 * rendendo il codice universale
         	 */
        	var inputStream = getClass().getResourceAsStream("/page/ConwayInOutPage.html");       	
        	if (inputStream != null) {
        		// Trasformiamo l'inputStream in stringa (o lo mandiamo come stream)
        	    String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        	    ctx.html(content);
        	} else {
		        ctx.status(404).result("File non trovato nel file system");
		    }
		    //ctx.result("Hello from Java!"));  //la forma più semplice di risposta
        }); 
        
//        app.get("/greet/{name}", ctx -> {
//            String name = ctx.pathParam("name");
//            ctx.result("Hello, " + name + "!");
//        }); //http://localhost:8080/greet/Alice
//        
//        app.get("/api/users", ctx -> {
//            Map<String, Object> user = Map.of("id", 1, "name", "Bob");
//            ctx.json(user); // Auto-converts to JSON
//        });
//        
//        /*
//         * Javalin v5+: Si passa solo la "promessa" (il Supplier del Future). 
//         * Javalin è diventato più intelligente: se il Future restituisce una Stringa, 
//         * lui fa ctx.result(stringa). Se restituisce un oggetto, lui fa ctx.json(oggetto).
//         * 
//         */
//        app.get("/async", ctx -> {
//        	ctx.future(() -> {
//	        	// Creiamo il future
//	            CompletableFuture<String> future = new CompletableFuture<>();
//	            
//	            // Eseguiamo il lavoro in un altro thread
//	            new Thread(() -> { 
//	                try {
//	                    Thread.sleep(2000); // Simulazione calcolo pesante
//	                    future.complete("IoJavalin | Risultato calcolato asincronamente");
//	                } catch (Exception e) {
//	                    future.completeExceptionally(e);
//	                }
//	            });
//	            
//	            return future; // Restituiamo il future a Javalin
//        	});
//        });
//        
//        app.get("/async1", ctx -> {
//            ctx.future(() -> CompletableFuture.supplyAsync(() -> {
//                // Simuliamo l'operazione lenta
//                try {
//                    Thread.sleep(2000); 
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return "IoJavalin | Risultato calcolato con supplyAsync";
//            }));
//        });
/*
 * --------------------------------------------
 * Parte Websocket
 * --------------------------------------------
 */
        
//        app.ws("/chat", ws -> {
//            ws.onConnect(ctx -> CommUtils.outgreen("Client connected chat!"));
//            ws.onMessage(ctx -> {
//                String message = ctx.message();
//                CommUtils.outcyan("IoJavalin |  riceve:" + message);
//                ctx.send("Echo: " + message);
//            });
//        }); 
        
        app.ws("/eval", ws -> {
            ws.onConnect(ctx -> CommUtils.outgreen("IoJavalin | Client connected eval"));
            ws.onMessage(ctx -> {
                String message = ctx.message();     
                CommUtils.outblue("IoJavalin |  eval receives:" + message );
                try {
                	IApplMessage m = new ApplMessage(message);
                    CommUtils.outblue("IoJavalin |  eval:" + m.msgContent() );
                    
                    // ricevo un messaggio dal client: in base al contenuto discrimino il comportamento
                    if( m.msgContent().equals("ready")) { 
                    	if (owner == null)
                    		owner = ctx;  //memorizzo la prima connession pagina
                    	else {
                    		spettatori.add(ctx);
                    		this.displayGrid(null);
                    	}
                    }
                    else if (ctx.equals(owner)){
                    	if (m.msgContent().equals("start")){
                    		// Il client ha premuto il pulsante START, allora faccio si che il GameController esegua onStart()
                    		cc.onStart();
                    	}
                    	else if (m.msgContent().equals("stop")) {
                    		// Il client ha premuto il pulsante STOP, allora faccio si che il GameController esegua onStop()
                    		cc.onStop();
                    	}
                    	else if (m.msgContent().equals("clear")) {
                    		// Il client ha premuto il pulsante CLEAR, allora faccio si che il GameController esegua onClear()
                    		cc.onClear();
                    	}
                    	else if (m.msgContent().equals("exit")){
                    		// Il client ha premuto il pulsante EXIT, allora eseguo close() del IOutDev
                    		CommUtils.outred("IoJavalin | Ricevuto comando EXIT. Chiusura in corso...");
                    		cc.onStop(); // Ferma il gioco
                    		this.close();
                    	}
                    	else if( m.msgContent().contains("cell(")) { 
                    		// Il client ha premuto una cella, vado ad estrarre quale dal messaggio
                    		decodeAndForwardCell(m.msgContent());
                    	}
                    
                    }
                    else {
                    	ctx.send("Non sei l'owner!");
                    }
                    	
                }catch(Exception e) {
                	CommUtils.outred("IoJavalin |  error:" + e.getMessage());
                }               
            });
        });        
	}
	
	
	public static void main(String[] args) {
		var resource = OutInWs.class.getResource("/pages");
		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);
		new OutInWs();
	}

	// funzione per iniettare il GameController in IoJavalin
	public void setController(GameController cc) {
		this.cc = cc;
	}
	
	// funzione di appoggio per estrarre riga e colonna della cella e andare a cambiarle stato
	private void decodeAndForwardCell(String content) {
	    try {
	        String data = content.replace("cell(", "").replace(")", "");
	        
	        String[] parts = data.split(",");
	        
	        if (parts.length >= 2) {
	            int x = Integer.parseInt(parts[0].trim());
	            int y = Integer.parseInt(parts[1].trim());

	            CommUtils.outblue("IoJavalin | Comando cella decodificato: x=" + x + ", y=" + y);
	            
	         // questo metodo si occupa di comunicarlo al cliente, invocando displayCell()
	            cc.switchCellState(x, y); 
	        }
	    } catch (NumberFormatException e) {
	        CommUtils.outred("IoJavalin | Errore nel formato della cella: " + content);
	    }
	}


	@Override
	public void display(String msg) {
		if (owner != null && owner.session.isOpen()) {
			owner.send(msg);
	    }
		spettatori.forEach(session -> {
	        if (session.session.isOpen()) {
	            session.send(msg);
	        }
	    });
	}



	@Override
	public void displayCell(IGrid grid, int x, int y) {
		if (owner != null && owner.session.isOpen()) {
	        int state = grid.getCellValue(x, y) ? 1 : 0; 
	        owner.send("cell(" + x + "," + y + "," + state + ")");
	    }
		spettatori.forEach(session -> {
	        if (session.session.isOpen()) {
	        	int state = grid.getCellValue(x, y) ? 1 : 0; 
		        session.send("cell(" + x + "," + y + "," + state + ")");
	        }
	    });
		
	}



	@Override
	public void close() {
        if (owner != null && owner.session.isOpen()) {
        	owner.session.close();
        }
        spettatori.forEach(session -> {
	        if (session.session.isOpen()) {
	        	session.session.close();
	        }
	    });
	}



	@Override
	public void displayGrid(IGrid grid) {
		for (int r = 0; r < grid.getRows(); r++) {
	        for (int c = 0; c < grid.getCols(); c++) {
	            displayCell(grid, r, c);
	        }
	    }		
	}

}
