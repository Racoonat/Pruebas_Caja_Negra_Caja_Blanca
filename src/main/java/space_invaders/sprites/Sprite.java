package space_invaders.sprites;

import java.awt.Image;

/** 
 * {@summary<span class="alert-small">⛔🧪</span> Clase base que representa de forma general 
 * los distintos componentes del juego Space Invaders.}
 * 
 * Cada componente del juego (jugador, alien, disparo, bomba) hereda de esta clase, que
 * sólo contiene getters y setters comunes a todos ellos.
 * 
 * <br><br><span class="alert">⛔🧪 No es necesario probar el método mediante pruebas unitarias.</span>
 */
public abstract class Sprite {

    private boolean visible;
    private Image image;
    private boolean dying;

    int x;
    int y;
    int dx;

    public Sprite() {

        visible = true;
    }

    public void die() {

        visible = false;
    }

    public boolean isVisible() {

        return visible;
    }

    protected void setVisible(boolean visible) {

        this.visible = visible;
    }

    public void setImage(Image image) {

        this.image = image;
    }

    public Image getImage() {

        return image;
    }

    public void setX(int x) {

        this.x = x;
    }

    public void setY(int y) {

        this.y = y;
    }

    public int getY() {

        return y;
    }

    public int getX() {

        return x;
    }

    public void setDying(boolean dying) {

        this.dying = dying;
    }

    public boolean isDying() {

        return this.dying;
    }
    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDx() {
        return this.dx;
    }
}
