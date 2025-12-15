
package es.iwt42.grupo6.cajaBlanca;


import main.Commons;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Alien;

import static org.junit.jupiter.api.Assertions.*;

public class AlienTest {

    // método act

    @Test
    @DisplayName("CB_ALIEN_ACT_01: (Path 1) act() ejecuta this.x -= direction")
    void testActMovesAlienHorizontally_Path1() {
        Alien alien = new Alien(50, 200);

        alien.act(5);

        assertEquals(45, alien.getX(), "Path 1 (A->B->C): Se esperaba x=45");
        assertEquals(200, alien.getY(), "Path 1 (A->B->C): 'act' no debe modificar Y");
    }


    //método initAlien

    @Test
    @DisplayName("CB_INIT_PATH_1 (A-C-D-F-G-I-J-L-M): x e y válidos (Camino Base)")
    void testInitAlien_Path1_Valid() {
        // Entrada: x=100, y=100
        // Caminos: if(x>358) F, if(x<0) F, if(y>350) F, if(y<0) F -> else T
        Alien alien = new Alien(100, 100);

        // Salida esperada (del código): el 'else' (Nodo L) asigna this.x=100, this.y=100
        assertEquals(100, alien.getX(), "Path 1: X debería ser 100");
        assertEquals(100, alien.getY(), "Path 1: Y debería ser 100");
    }

    @Test
    @DisplayName("CB_INIT_PATH_2 (A-B-D-F-G-I-J-L-M): x por encima del límite (>358)")
    void testInitAlien_Path2_X_Above() {
        // Entrada: x=400, y=100
        // Caminos: if(x>358) T, if(x<0) F, if(y>350) F, if(y<0) F -> else T
        Alien alien = new Alien(400, 100);

        // Salida esperada (del código):
        // Nodo B (if) -> this.x = 358
        // Nodo L (else) -> this.x = 400 (Sobrescribe)
        assertEquals(400, alien.getX(), "Path 2: X debería ser 400");
        assertEquals(100, alien.getY(), "Path 2: Y debería ser 100");
    }

    @Test
    @DisplayName("CB_INIT_PATH_3 (A-C-D-E-G-I-J-L-M): x por debajo del límite (<0)")
    void testInitAlien_Path3_X_Below() {
        // Entrada: x=-50, y=100
        // Caminos: if(x>358) F, if(x<0) T, if(y>350) F, if(y<0) F -> else T
        Alien alien = new Alien(-50, 100);

        // Salida esperada (del código):
        // Nodo E (if) -> this.x = 0
        // Nodo L (else) -> this.x = -50 (Sobrescribe)
        assertEquals(-50, alien.getX(), "Path 3: X debería ser -50");
        assertEquals(100, alien.getY(), "Path 3: Y debería ser 100");
    }

    @Test
    @DisplayName("CB_INIT_PATH_4 (A-C-D-F-G-H-J-L-M): y por encima del límite (>350)")
    void testInitAlien_Path4_Y_Above() {
        Alien alien = new Alien(100, 400);


        assertEquals(100, alien.getX(), "Path 4: X debería ser 100");
        assertEquals(400, alien.getY(), "Path 4: Y debería ser 400");
    }

    @Test
    @DisplayName("CB_INIT_PATH_5 (A-C-D-F-G-I-J-K-M): y por debajo del límite (<0)")
    void testInitAlien_Path5_Y_Below() {
        Alien alien = new Alien(100, -50);
        
        assertEquals(0, alien.getX(), "Path 5: X debería ser 0");
        assertEquals(0, alien.getY(), "Path 5: Y debería ser 0");
    }



    //método bomb

        @Test
        @DisplayName("CB_GETBOMB_01: getBomb() devuelve la misma instancia y permite cambiar 'destroyed'")
        void testGetBombAndDestroyedFlag() {
            Alien alien = new Alien(10, 10);
            Alien.Bomb bomb = alien.getBomb();

            // Debe empezar como destroyed = true (ya comprobado arriba, lo reafirmamos)
            assertTrue(bomb.isDestroyed(), "Estado inicial esperado: destroyed = true");

            // Cambiamos el flag y verificamos
            bomb.setDestroyed(false);
            assertFalse(bomb.isDestroyed(), "setDestroyed(false) no actualizó el estado de la bomba");

            bomb.setDestroyed(true);
            assertTrue(bomb.isDestroyed(), "setDestroyed(true) no actualizó el estado de la bomba");

            // La referencia devuelta debe ser estable (misma instancia)
            assertSame(bomb, alien.getBomb(), "getBomb() debería devolver siempre la misma instancia de Bomb");
        }

    @ParameterizedTest
    @CsvSource(value = {
        "179,175", "360,175", "179,351"
    })
    void bombTest(int x, int y) {
        Alien.Bomb bomb = new Alien(Commons.BOARD_WIDTH/2,Commons.BOARD_HEIGHT/2).new Bomb(x,y);
        boolean insideWidthRange = bomb.getX() >= 0 && bomb.getX() <= Commons.BOARD_WIDTH;
        boolean insideHeightRange = bomb.getY() >= 0 && bomb.getY() <= Commons.BOARD_HEIGHT;
        assertTrue(insideWidthRange && insideHeightRange);
    }
}

