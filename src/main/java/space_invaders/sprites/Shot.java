package space_invaders.sprites;

import javax.swing.ImageIcon;

/**
 * {@summary Clase que representa un disparo en el juego Space Invaders.}
 */
public class Shot extends Sprite {
    final int V_SPACE = 1;
    final int H_SPACE = 6;

    /**
     * {@summary Inicializa un nuevo disparo.}
     * 
     * El constructor crea una instancia del disparo sin configurar su estado inicial.
     * Se debe utilizar {@link #initShot(int, int)} para establecer su posición.
     */
    public Shot() {
    }

    /**
     * {@summary Inicializa un nuevo disparo.}
     * 
     * El constructor crea una instancia del disparo y configura su estado inicial con {@link #initShot(int, int)}.
     * @param x coordenada X de la posición del nuevo disparo
     * @param y coordenada Y de la posición del nuevo disparo
     */
    public Shot(int x, int y) {

        initShot(x, y);
    }

    /**
     * {@summary Configura el estado inicial del disparo estableciendo su posición en el tablero.}
     * 
     * Inicializa un nuevo objeto disparo en las coordenadas indicadas y le asigna la imagen 
     * correspondiente en la interfaz
     * 
     * El disparo sale del jugador, y le asigna las coordenadas sumando el valor {@link #H_SPACE} 
     * a la coordenada X, y restando el valor {@link #V_SPACE} a la coordenada Y.
     * 
     * @param x coordenada horizontal donde se desea ubicar el disparo
     * @param y coordenada vertical donde se desea ubicar el disparo
     * 
     * <dl class="notes"><dt>Postcondición:</dt> <dd>El disparo queda listo en la posición especificada
     * y su posición es limítrofe con la imagen de la nave del jugador.</dd></dl>
     */
    private void initShot(int x, int y) {

        var shotImg = "src/main/resources/images/shot.png";
        var ii = new ImageIcon(shotImg);
        setImage(ii.getImage());

        setX(x + H_SPACE);

        setY(y - V_SPACE);
    }
}

