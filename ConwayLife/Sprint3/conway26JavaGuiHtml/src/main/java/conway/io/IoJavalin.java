package conway.io;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;

public class IoJavalin {
	
	private static AtomicInteger pageCounter = new AtomicInteger(0);
	private WsMessageContext pageCtx, lifeCtrlCtx ;
	private String name;
	private String firstCaller        = null;
	//private WsConnectContext ownerctx = null;
	protected Vector<WsConnectContext> allConns = new Vector<WsConnectContext>();

	public IoJavalin(String name) {
		this.name = name;
        var app = Javalin.create(config -> {
        	// Configurazione globale del timeout per le connessioni (dalla versione 6.x in avanti)
            //config.http.asyncTimeout = 300000L; // 5 minuti in millisecondi
        	config.jetty.modifyWebSocketServletFactory(factory -> {
                // Imposta il timeout (ad esempio 5 minuti)
                factory.setIdleTimeout(Duration.ofMinutes(30));
            });
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
        	//var inputStream = getClass().getResourceAsStream("/page/ConwayInOutPage.html");  
        	var inputStream = getClass().getResourceAsStream("/page/LifeIInOutCanvas.html");     
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
//            String pname = ctx.pathParam("name");
//            ctx.result("Hello, " + pname + "!");
//        }); //http://localhost:8080/greet/Alice
//        
//        app.get("/api/users", ctx -> {
//            Map<String, Object> user = Map.of("id", 1, "name", "Bob");
//            ctx.json(user); // Auto-converts to JSON
//        });
        
        /*
         * Javalin v5+: Si passa solo la "promessa" (il Supplier del Future). 
         * Javalin è diventato più intelligente: se il Future restituisce una Stringa, 
         * lui fa ctx.result(stringa). Se restituisce un oggetto, lui fa ctx.json(oggetto).
         * 
         */
//        app.get("/async", ctx -> {
//        	ctx.future(() -> {
//	        	// Creiamo il future
//	            CompletableFuture<String> future = new CompletableFuture<>();
//	            
//	            // Eseguiamo il lavoro in un altro thread
//	            new Thread(() -> { 
//	                try {
//	                    Thread.sleep(2000); // Simulazione calcolo pesante
//	                    future.complete(name + " | Risultato calcolato asincronamente");
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
//                return name + " | Risultato calcolato con supplyAsync";
//            }));
//        });
/*
 * --------------------------------------------
 * Parte Websocket
 * --------------------------------------------
 */      
        app.ws("/eval", ws -> {
        	ws.onConnect(ctx -> {
        		allConns.add(ctx);
        	    CommUtils.outmagenta("Nuova connessione WebSocket stabilita");
        	    // Non assegniamo l'owner qui, aspettiamo che la pagina si dichiari pronta
        	});
             
        	ws.onMessage(ctx -> {
        	    String message = ctx.message();
        	    try {
        	        // 1. Gestione REGISTRAZIONE OWNER (Interconnessione B)
        	        // La pagina invia "canvasready" dentro un IApplMessage o stringa pura
        	        if (message.contains("canvasready")) {
        	            if (firstCaller == null) {
        	                firstCaller = "caller1"; // Il primo browser che carica diventa Owner
        	                pageCtx = ctx; 
        	                sendsafe(ctx, "ID:caller1");
        	                CommUtils.outgreen(name + " | OWNER assegnato al Browser (caller1)");
        	            } else {
        	                int id = pageCounter.incrementAndGet();
        	                sendsafe(ctx, "ID:caller" + id);
        	                CommUtils.outmagenta(name + " | OSSERVATORE collegato (caller" + id + ")");
        	            }
        	            return; // Messaggio gestito, usciamo
        	        }

        	        // 2. Gestione PROTOCOLLO IApplMessage (Interconnessione A)
        	        IApplMessage m = new ApplMessage(message);
        	        
        	        // Se ricevo la griglia (Safe JSON con ;) la mando a tutti
        	        if (m.msgId().equals("gridUpdate")) {
        	            // Inoltra il messaggio a TUTTI i browser connessi
        	            for (WsConnectContext conn : allConns) {
        	                if (conn.session.isOpen()) {
        	                    sendsafe(conn, m.toString());
        	                }
        	            }
        	        }
        	        // Se ricevo comandi dall'Owner (caller1), li mando al Controller
        	        else if (m.msgSender().equals("caller1")) {
        	            if (lifeCtrlCtx != null) sendsafe(lifeCtrlCtx, m.toString());
        	        }
        	        // Se è il controller che si registra
        	        else if (m.msgId().equals("setcontroller")) {
        	            lifeCtrlCtx = ctx;
        	            CommUtils.outblue(name + " | Controller logico agganciato.");
        	        }

        	    } catch (Exception e) {
        	        // Se non è un IApplMessage e non è canvasready, logghiamo l'errore
        	        CommUtils.outred(name + " | Errore parsing messaggio: " + message);
        	    }
        	});
        });        
	}
	
    protected void sendsafe(WsContext ctx, String msg) {
    	synchronized (ctx.session) {  
            if (ctx.session.isOpen()) {
                ctx.send(msg);
            }
        }
    }
	

	
	public static void main(String[] args) {
		var resource = IoJavalin.class.getResource("/pages");
		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);
		new IoJavalin("guiserver");
	}

}
