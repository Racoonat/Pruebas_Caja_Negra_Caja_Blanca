package space_invaders.sprites;

import main.Commons;

import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;

/**
 * {@summary Clase que representa al jugador en el juego Space Invaders.}
 */
public class Player extends Sprite {

    private int width;

    /**
     * {@summary Inicializa un nuevo jugador.}
     * 
     * El constructor crea una instancia del jugador y configura su estado inicial con {@link #initPlayer()}.
     */
    public Player() {

        initPlayer();
    }

    /**
     * {@summary Configura el estado inicial del jugador.}
     * 
     * Este método prepara al jugador para participar en el juego, asignándole una ubicación
     * en el centro de la pantalla ({@link main.Commons#BOARD_WIDTH}) y 10 pixeles por encima 
     * del suelo ({@link main.Commons#GROUND}).
     */
    private void initPlayer() {

        var playerImg = "src/main/resources/images/player.png";
        var ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        int START_X = 179;
        setX(START_X);

        int START_Y = 280;
        setY(START_Y);
    }

    /**
     * {@summary Actualiza la posición del jugador en el tablero de juego.}
     * 
     * <p>Este método modifica la posición horizontal del jugador según su estado actual
     * de movimiento. El jugador siempre permanecerá dentro de los límites válidos
     * del área de juego después de la actualización.</p>
     * 
     * <dl class="notes"><dt>Nótese:</dt> <dd>dx puede tomar los valores -2, 0 o 2.</dd></dl>
     * 
     * <dl class="notes"><dt>Postcondición:</dt> <dd>La posición del jugador se actualiza
     * y permanece dentro de los límites del tablero de juego.</dd></dl>
     */
    public void act() {

        x += dx;

        if (x <= 2) {

            x = 2;
        }

        if (x >= Commons.BOARD_WIDTH - 2 * width) {

            x = Commons.BOARD_WIDTH + 2 * width;
        }
    }

    /**
     * {@summary Maneja los eventos de pulsación de teclas para controlar el movimiento del jugador.}
     * 
     * - Si se presiona la tecla izquierda, el jugador se moverá hacia la izquierda.
     * - Si se presiona la tecla derecha, el jugador se moverá hacia la derecha.
     * 
     * Cualquier otra tecla de movimiento no produce acción
     * 
     * @param e el evento de teclado que contiene información sobre la tecla presionada
     * 
     * <dl class="Nótese"><dt>Nota:</dt> <dd>Las variaciones son de 2 en 2.</dd></dl>
    */
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 2;
        }
    }

    /**
     * {@summary Comprueba si una de las teclas de movimiento ya no está pulsada.}
     * 
     * - Si se suelta la tecla flecha a la izquierda, reinicia el desplazamiento de la izquierda a 0
     * - Si se suelta la tecla flecha a la derecha, reinicia el desplazamiento de la derecha a 0
     * Cualquier otra tecla de movimiento no produce acción
     * @param e tecla presionada
     * */
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }
}
