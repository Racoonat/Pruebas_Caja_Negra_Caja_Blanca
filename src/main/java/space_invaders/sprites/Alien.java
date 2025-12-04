package space_invaders.sprites;

import main.Commons;

import javax.swing.ImageIcon;

/**
 * {@summary Clase que representa un alien en el juego Space Invaders.}
 * 
 * Cada alien tiene una posición (x, y) y puede lanzar una bomba.
 * Proporciona métodos para inicializar el alien, moverlo y acceder a su bomba. 
 * La clase {@link Bomb} es una clase interna que representa la bomba lanzada por el alien.
 */
public class Alien extends Sprite {

    private Bomb bomb;

    /**
     * Inicializa un nuevo alien
     * @param x coordenada X de la posición del nu
     * @param y coordenada Y de la posición del nuevo alien
     * */
    public Alien(int x, int y) {

        initAlien(x, y);
    }

    /**
     * {@summary Configura el estado inicial del alien estableciendo su posición en el tablero.}
     * 
     * Este método prepara el alien para participar en el juego, asignándole una ubicación
     * específica y todos los recursos necesarios para su funcionamiento. El alien quedará
     * completamente operativo tras la ejecución.
     * 
     * El método garantiza que el alien siempre tendrá una posición válida dentro del
     * área de juego, independientemente de los valores de entrada proporcionados. Si alguna de las coordenadas indicadas supera los márgenes de la pantalla, se ubicará en el máximo permitido.
     * Por ejemplo, si la coordenada X indicada supera el margen de la pantalla, se asignará a X el valor máximo posible, es decir, el ancho de la pantalla.
     * Si se introduce alguna coordenada negativa, se reemplazará por 0.
     * 
     * @param x coordenada horizontal donde se desea ubicar el alien
     * @param y coordenada vertical donde se desea ubicar el alien
     * 
     * <dl class="notes"><dt>Postcondición:</dt> <dd>El alien queda listo en la posición especificada
     * y su posición es válida dentro del tablero de juego.</dd></dl>
     */
     private void initAlien(int x, int y) {

        if (x> Commons.BOARD_WIDTH){
            this.x = Commons.BOARD_WIDTH;
        } if (x<0){
            this.x = 0;
        } if (y> Commons.BOARD_HEIGHT){
            this.y = Commons.BOARD_HEIGHT;
        } if (y<0){
            this.y=0;
        }
        else
        {
            this.x = x;
            this.y = y;
        }

        bomb = new Bomb(x, y);

        var alienImg = "src/main/resources/images/alien.png";
        var ii = new ImageIcon(alienImg);

        setImage(ii.getImage());
    }

    /**
     * {@summary Ejecuta una acción de movimiento del alien en la dirección especificada.}
     * 
     * <p>Este método actualiza la posición horizontal del alien según el parámetro
     * de dirección proporcionado. El alien se desplazará en el tablero de juego
     * manteniendo su posición vertical constante.</p>
     * 
     * @param direction valor que determina la dirección y magnitud del movimiento horizontal
     * 
     * <dl class="notes"><dt>Postcondición:</dt> <dd>La posición horizontal del alien se actualiza 
     * según la dirección especificada.</dd></dl>
     */
    public void act(int direction) {

        this.x -= direction;
    }

    /**
     * {@summary <span class="alert-small">⛔🧪</span> Devuelve el objeto explosión asociado al alien}
     * 
     * <br><br><span class="alert">⛔🧪 No es necesario probar el método mediante pruebas unitarias.</span>
     * @return bomb
     * */
    public Bomb getBomb() {

        return bomb;
    }

    /**
     * {@summary Clase interna que representa una bomba lanzada por un alien.}
     * 
     * La bomba tiene una posición (x, y) y un estado de destrucción.
     * Proporciona métodos para inicializar la bomba, verificar su estado
     * y modificar su estado de destrucción.
     */
    public class Bomb extends Sprite {
        
        private boolean destroyed;

        /**
         * {@summary Crea una nueva bomba en la posición indicada}
         * 
         * @param x coordenada X de la posición de la nueva explosión
         * @param y coordenada Y de la posición de la nueva explosión
         * */
        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        /**
         * {@summary Inicializa el nuevo objeto bomba y le asigna las coordenadas indicadas y la imagen correspondiente en la interfaz}
         * 
         * @param x coordenada X de la posición de la nueva bomba
         * @param y coordenada Y de la posición de la nueva bomba
         * Si el valor X o Y indicados superan el margen de la pantalla, se les asignará el valor máximo permitido.
         * Si se introduce algún valor negativo, será reemplazado por 0.
         * */
        private void initBomb(int x, int y) {

            setDestroyed(true);

            if (x<= Commons.BOARD_WIDTH && y<= Commons.BOARD_HEIGHT) {
                this.x += x;
                this.y += y;
            } else
            {
                this.x = Commons.BOARD_WIDTH;
                this.y = Commons.BOARD_HEIGHT;
            }

            var bombImg = "src/main/resources/images/bomb.png";
            var ii = new ImageIcon(bombImg);
            setImage(ii.getImage());
        }

        /**
         * {@summary <span class="alert-small">⛔🧪</span> Establece el estado de destrucción de la bomba.}
         * 
         * <br><br><span class="alert">⛔🧪 No es necesario probar este método mediante pruebas unitarias.</span>
         * @param destroyed valor booleano que indica si la bomba está destruida (true) o no (false)
         */
        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }
        
        /**
         * {@summary <span class="alert-small">⛔🧪</span> Indica si la bomba está destruida.}
         * 
         * <br><br><span class="alert">⛔🧪 No es necesario probar el método mediante pruebas unitarias.</span>
         * @return true si la bomba está destruida, false en caso contrario
         */
        public boolean isDestroyed() {

            return destroyed;
        }
    }
}
