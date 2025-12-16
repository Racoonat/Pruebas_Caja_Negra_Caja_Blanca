package es.iwt42.grupo6.integración;

import main.Board;
import main.Commons;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;
import space_invaders.sprites.Shot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class BoardIntegrationTest {

    private Board board;
    private Shot shotMock;
    private Alien alienMock;
    private Player playerMock;
    private Alien.Bomb bombMock;
    private List<Alien> aliens;

    @BeforeEach
    public void setUp() {
        // 1. Configuración común para todos los tests de integración
        board = new Board();

        // Mocks de las dependencias
        shotMock = mock(Shot.class);
        playerMock = mock(Player.class);
        alienMock = mock(Alien.class);
        bombMock = mock(Alien.Bomb.class);

        // Configuración de relaciones entre mocks
        // Cuando al alien se le pida su bomba, devolverá nuestro bombMock
        when(alienMock.getBomb()).thenReturn(bombMock);

        // Inyección de mocks en el tablero
        board.setShot(shotMock);
        board.setPlayer(playerMock);

        aliens = new ArrayList<>();
        aliens.add(alienMock);
        board.setAliens(aliens);

        // Configuraciones por defecto para evitar NullPointerExceptions durante update()
        // Por defecto, asumimos que no hay colisiones ni muertes salvo que el test diga lo contrario
        when(shotMock.isVisible()).thenReturn(false);
        when(playerMock.isVisible()).thenReturn(true);
        when(alienMock.isVisible()).thenReturn(true);
        when(bombMock.isDestroyed()).thenReturn(true); // Por defecto bomba inactiva
    }

    /**
     * Test de Integración para update_shots()
     * Verifica la interacción Board -> Shot/Alien cuando hay colisión.
     */
    @Test
    public void testUpdateShots_Collision() {
        // GIVEN: Disparo y Alien visibles y en la misma posición
        when(shotMock.isVisible()).thenReturn(true);

        int x = 100;
        int y = 100;
        when(shotMock.getX()).thenReturn(x);
        when(shotMock.getY()).thenReturn(y);
        when(alienMock.getX()).thenReturn(x);
        when(alienMock.getY()).thenReturn(y);

        // WHEN: Ejecutamos el ciclo
        board.update();

        // THEN: Verificar muerte del alien y del disparo
        verify(alienMock).setDying(true);
        verify(shotMock).die();
    }

    /**
     * Test de Integración para update_aliens() - Cambio de Dirección
     * Verifica que Board detecta el borde y ordena a los aliens bajar y cambiar dirección.
     */
    @Test
    public void testUpdateAliens_BorderCollision() {
        // GIVEN: Alien situado en el borde derecho
        // Posición que activa la lógica: x >= BOARD_WIDTH - BORDER_RIGHT
        int bordeDerecho = Commons.BOARD_WIDTH - Commons.BORDER_RIGHT;
        when(alienMock.getX()).thenReturn(bordeDerecho);
        when(alienMock.getY()).thenReturn(50); // Altura cualquiera

        // Dirección actual inicial (hacia la derecha) para forzar el choque
        // *Nota: Asumimos que la dirección inicial en Board podría ser -1 o 1.
        // Para asegurar que entra en el if (direction != -1), forzamos dirección inicial si tuviéramos setter,
        // o asumimos el estado por defecto. En Board.java direction inicia en -1 (izq),
        // pero para probar el rebote a la derecha, el alien debe estar a la derecha.
        // Vamos a probar el rebote izquierdo que es más natural con direction=-1.

        // Probamos REBOTE IZQUIERDO (x <= BORDER_LEFT)
        int bordeIzquierdo = Commons.BORDER_LEFT;
        when(alienMock.getX()).thenReturn(bordeIzquierdo);
        // Board se inicia con direction = -1, así que al tocar izquierda debería cambiar a 1

        // WHEN
        board.update();

        // THEN
        // 1. Verificar que se llama a setY para bajar al alien
        // El valor exacto depende de la lógica, pero verificamos que se llamó al setter
        verify(alienMock, atLeastOnce()).setY(anyInt());

        // 2. Verificar que la dirección del tablero ha cambiado a 1 (Derecha)
        assertEquals(1, board.getDirection(), "La dirección debería cambiar a derecha (1) tras tocar borde izquierdo");
    }

    /**
     * Test de Integración para update_aliens() - Invasión
     * Verifica que si un alien toca el suelo, el juego termina.
     */
    @Test
    public void testUpdateAliens_Invasion() {
        // GIVEN: Alien situado a la altura del suelo
        // y > GROUND + ALIEN_HEIGHT
        int alturaInvasion = Commons.GROUND + Commons.ALIEN_HEIGHT + 1;
        when(alienMock.getY()).thenReturn(alturaInvasion);

        // WHEN
        board.update();

        // THEN
        assertFalse(board.isInGame(), "El juego debería terminar (inGame=false) si un alien invade");
        assertEquals("Invasion!", board.getMessage(), "El mensaje debería ser de Invasión");
    }

    /**
     * Test de Integración para update_bomb()
     * Verifica que si una bomba activa toca al jugador, este muere.
     */
    @Test
    public void testUpdateBomb_PlayerCollision() {
        // GIVEN: Bomba activa (no destruida) en la posición del jugador
        when(bombMock.isDestroyed()).thenReturn(false);

        int playerX = 200;
        int playerY = 280;

        when(playerMock.getX()).thenReturn(playerX);
        when(playerMock.getY()).thenReturn(playerY);

        // La bomba está en las mismas coordenadas
        when(bombMock.getX()).thenReturn(playerX);
        when(bombMock.getY()).thenReturn(playerY);

        // WHEN
        board.update();

        // THEN
        verify(playerMock).setDying(true);  // El jugador debe morir
        verify(bombMock).setDestroyed(true); // La bomba se destruye al impactar
    }

    /**
     * Test de Integración para update() - Condición de Victoria
     * Verifica que Board detiene el juego cuando se matan todos los aliens.
     */
    @Test
    public void testUpdate_WinCondition() {
        // GIVEN: Contador de muertes igual al total de aliens
        board.setDeaths(Commons.NUMBER_OF_ALIENS_TO_DESTROY);

        // WHEN
        board.update();

        // THEN
        assertFalse(board.isInGame(), "El juego debería terminar si se eliminan todos los aliens");
        assertEquals("Game won!", board.getMessage());
    }
}