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

/**
 * Nivel 4: Pruebas de Integración
 * Estrategia: Top-Down usando Mockito.
 * Objetivo: Verificar MM-Paths entre Board y sus dependencias (Alien, Shot, Player).
 */
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
        when(alienMock.getBomb()).thenReturn(bombMock);

        // Inyección de mocks en el tablero
        board.setShot(shotMock);
        board.setPlayer(playerMock);

        aliens = new ArrayList<>();
        aliens.add(alienMock);
        board.setAliens(aliens);

        // Configuraciones por defecto (Happy Path)
        when(shotMock.isVisible()).thenReturn(false);
        when(playerMock.isVisible()).thenReturn(true);
        when(alienMock.isVisible()).thenReturn(true);
        when(bombMock.isDestroyed()).thenReturn(true);

        // Valores por defecto para evitar NullPointers en cálculos matemáticos
        when(shotMock.getX()).thenReturn(0);
        when(shotMock.getY()).thenReturn(0);
        when(alienMock.getX()).thenReturn(0);
        when(alienMock.getY()).thenReturn(0);
    }

    // -------------------------------------------------------------------------
    // MM-PATH 1: Board -> Player (Delegación de acción)
    // -------------------------------------------------------------------------

    /**
     * Verifica que el ciclo de actualización del tablero invoca siempre
     * al método act() del jugador.
     */
    @Test
    public void testUpdate_PlayerActs() {
        // WHEN
        board.update();

        // THEN
        verify(playerMock, times(1)).act();
    }

    // -------------------------------------------------------------------------
    // MM-PATH 2: Board -> Shot (Lógica de Movimiento y Colisión)
    // -------------------------------------------------------------------------

    /**
     * Verifica la interacción Board -> Shot cuando NO hay colisión.
     * El tablero debe calcular la nueva Y y actualizar el disparo.
     */
    @Test
    public void testUpdateShots_Movement() {
        // GIVEN: Disparo visible en posición (100, 100)
        when(shotMock.isVisible()).thenReturn(true);
        int initialY = 100;
        when(shotMock.getY()).thenReturn(initialY);
        when(shotMock.getX()).thenReturn(100);

        // WHEN
        board.update();

        // THEN
        // Board debe obtener Y, restar SHOT_SPEED y establecer la nueva Y
        // Verificamos que se llama a setY con el valor esperado
        int expectedY = initialY - Commons.SHOT_SPEED;
        verify(shotMock).setY(expectedY);
        // Aseguramos que NO muere si no hay colisión ni sale de pantalla
        verify(shotMock, never()).die();
    }

    @Test
    public void testUpdateShots_Collision() {
        // GIVEN: Disparo y Alien visibles colisionando
        when(shotMock.isVisible()).thenReturn(true);
        int x = 100; int y = 100;
        when(shotMock.getX()).thenReturn(x);
        when(shotMock.getY()).thenReturn(y);
        when(alienMock.getX()).thenReturn(x);
        when(alienMock.getY()).thenReturn(y);

        // WHEN
        board.update();

        // THEN
        verify(alienMock).setDying(true);
        verify(shotMock).die();
    }

    // -------------------------------------------------------------------------
    // MM-PATH 3: Board -> Bomb (Gestión de gravedad)
    // -------------------------------------------------------------------------

    /**
     * Verifica que Board mueve la bomba hacia abajo si está activa.
     */
    @Test
    public void testUpdateBomb_Movement() {
        // GIVEN: Bomba activa
        when(bombMock.isDestroyed()).thenReturn(false);
        int initialY = 50;
        when(bombMock.getY()).thenReturn(initialY);
        when(bombMock.getX()).thenReturn(50);

        // Posicionamos al jugador lejos para evitar colisión en este test
        when(playerMock.getX()).thenReturn(500);
        when(playerMock.getY()).thenReturn(500);

        // WHEN
        board.update();

        // THEN
        // La bomba debe bajar (sumar BOMB_SPEED a Y)
        int expectedY = initialY + Commons.BOMB_SPEED;
        verify(bombMock).setY(expectedY);
    }

    @Test
    public void testUpdateBomb_PlayerCollision() {
        // GIVEN: Bomba activa sobre el jugador
        when(bombMock.isDestroyed()).thenReturn(false);
        int playerX = 200; int playerY = 280;
        when(playerMock.getX()).thenReturn(playerX);
        when(playerMock.getY()).thenReturn(playerY);
        when(bombMock.getX()).thenReturn(playerX);
        when(bombMock.getY()).thenReturn(playerY);

        // WHEN
        board.update();

        // THEN
        verify(playerMock).setDying(true);
        verify(bombMock).setDestroyed(true);
    }

    // -------------------------------------------------------------------------
    // MM-PATH 4: Board -> Alien (Coordinación de grupo)
    // -------------------------------------------------------------------------

    @Test
    public void testUpdateAliens_BorderCollision() {
        // GIVEN: Alien en el borde izquierdo
        // Board inicia con direction = -1 (Izquierda)
        int bordeIzquierdo = Commons.BORDER_LEFT;
        when(alienMock.getX()).thenReturn(bordeIzquierdo);

        // WHEN
        board.update();

        // THEN
        // Verificar cambio de dirección en Board (debe ser 1 -> Derecha)
        assertEquals(1, board.getDirection(), "La dirección debe cambiar a derecha al tocar borde izquierdo");
        // Verificar que se ordena bajar a los aliens
        verify(alienMock, atLeastOnce()).setY(anyInt());
    }

    @Test
    public void testUpdateAliens_Invasion() {
        // GIVEN: Alien invadiendo (tocando suelo)
        int alturaInvasion = Commons.GROUND + Commons.ALIEN_HEIGHT + 1;
        when(alienMock.getY()).thenReturn(alturaInvasion);

        // WHEN
        board.update();

        // THEN
        assertFalse(board.isInGame());
        assertEquals("Invasion!", board.getMessage());
    }

    @Test
    public void testUpdate_WinCondition() {
        // GIVEN: Todos los aliens destruidos
        board.setDeaths(Commons.NUMBER_OF_ALIENS_TO_DESTROY);

        // WHEN
        board.update();

        // THEN
        assertFalse(board.isInGame());
        assertEquals("Game won!", board.getMessage());
    }
}