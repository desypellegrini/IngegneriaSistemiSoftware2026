/*
outarea.js
*/
const list = document.getElementById("msgslist")

    function addItem(item){
		//var list = document.getElementById("msgslist")	
	    const li = document.createElement('li');
        li.appendChild(document.createTextNode(item))
		list.appendChild(li);			 
    }
	
	function clearOutArea(){
		const lista = document.getElementById('msgslist');  
		lista.innerHTML = ''; // Rimuovi tutti i nodi figli
	}

	
	
	// Funzione per aggiornare l'area di testo nella pagina
	function updateOutputArea(message) {
	    const area = document.getElementById("outArea"); 
	    if (area) {
	        area.value += message + "\n";
	        area.scrollTop = area.scrollHeight; 
	    }
	}

	// Test 
	setInterval(() => {
	    const logMsg = "--- Test automatico: " + new Date().toLocaleTimeString();
	    console.log(logMsg); // Stampa nella console del browser (F12)
	    updateOutputArea(logMsg); // Stampa nella textarea della pagina
	}, 5000);