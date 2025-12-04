package main;
/**
 * {@summary <span class="alert-small">⛔🧪 </span> Clase de constantes del juego Space Invaders}
 * En esta clase se definen las constantes utilizadas en el juego, como las dimensiones del tablero,
 * las posiciones iniciales de los alienígenas, la velocidad de movimiento, entre otras.
 * <br><br><span class="alert">⛔🧪 No es necesario probar esta clase mediante pruebas unitarias. </span>
 */
public interface Commons {

    int BOARD_WIDTH = 358;
    int BOARD_HEIGHT = 350;
    int BORDER_RIGHT = 30;
    int BORDER_LEFT = 5;

    int GROUND = 290;
    int BOMB_HEIGHT = 5;

    int ALIEN_HEIGHT = 12;
    int ALIEN_WIDTH = 12;

    int ALIEN_ROWS = 4;
    int ALIEN_COLUMNS = 6;
    int ALIEN_SEPARATOR = 18;
    int ALIEN_INIT_X = 150;
    int ALIEN_INIT_Y = 5;

    int GO_DOWN = 15;
    int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    int CHANCE = 5;
    int DELAY = 17;
    int PLAYER_WIDTH = 15;
    int PLAYER_HEIGHT = 10;

    int SHOT_SPEED = 4;
    int BOMB_SPEED = 1;
}
