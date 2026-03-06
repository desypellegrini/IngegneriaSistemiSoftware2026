package main.java.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.conway.domain.Life;
import main.java.conway.domain.LifeInterface;
 

public class LifeTest {
private LifeInterface lifeModel;
private final int nRows = 5;
private final int nCols = 5;

	@Before
	public void setup() {
		System.out.println("GridTest | setup");	
		lifeModel = Life.CreateLife(nRows, nCols);
	}
	@After
	public void down() {
		System.out.println("GridTest | down");
	}
	
	/**
     * TEST 1: Configurazione conosciuta (Blinker)
     * Verifica che una linea orizzontale di 3 celle diventi verticale
     */
	@Test
    public void testBlinkerEvolution() {
        System.out.println("LifeTest | testBlinkerEvolution");
        // Imposto configurazione orizzontale conosciuta
        lifeModel.setCell(2, 1, true);
        lifeModel.setCell(2, 2, true);
        lifeModel.setCell(2, 3, true);

        lifeModel.nextGeneration();

        // Controllo che segua le regole (diventa verticale)
        assertTrue("Cella centrale deve restare viva", lifeModel.isAlive(2, 2));
        assertTrue("Cella sopra deve nascere", lifeModel.isAlive(1, 2));
        assertTrue("Cella sotto deve nascere", lifeModel.isAlive(3, 2));
        assertFalse("Cella sinistra deve morire", lifeModel.isAlive(2, 1));
        assertFalse("Cella destra deve morire", lifeModel.isAlive(2, 3));
    }
	
	/**
     * TEST 2: Reset e pulizia
     * Imposto una cella, resetto e verifico che tutto sia morto
     */
	@Test
    public void testResetBehavior() {
        System.out.println("LifeTest | testResetBehavior");
        // Imposto una cella viva
        lifeModel.setCell(1, 1, true);
        assertTrue(lifeModel.isAlive(1, 1));

        // Eseguo il reset
        lifeModel.resetGrids();

        // Verifico che sia morta
        assertFalse("Dopo il reset la cella deve essere morta", lifeModel.isAlive(1, 1));
        
        // Controllo aggiuntivo: dopo nextGeneration su griglia vuota, resta vuota
        lifeModel.nextGeneration();
        assertFalse("Dopo nextGeneration su griglia vuota, non deve nascere nulla", lifeModel.isAlive(1, 1));
    }

}
