package es.iwt42.grupo6.sistema;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.Board;
import main.Commons;
import space_invaders.sprites.Player;

import java.awt.event.KeyEvent;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerSteps {

    private Board board;
    private Player player;
    private int posicionXInicial;

    // GIVEN

    @Given("Dado el tablero del juego Space Invaders")
    public void dado_el_tablero_del_juego_space_invaders() {
        board = new Board();
        board.setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);




        player = board.getPlayer();
    }

    @Given("no estoy en la posición más a la izquierda del tablero")
    public void no_estoy_en_la_posicion_mas_a_la_izquierda() {
        player.setX(Commons.BOARD_WIDTH / 2);
        posicionXInicial = player.getX();
    }

    @Given("estoy en la posición más a la izquierda del tablero")
    public void estoy_en_la_posicion_mas_a_la_izquierda() {
        player.setX(Commons.BORDER_LEFT);
        posicionXInicial = player.getX();
    }

    @Given("no estoy en la posición más a la derecha del tablero")
    public void no_estoy_en_la_posicion_mas_a_la_derecha() {
        player.setX(Commons.BOARD_WIDTH / 2);
        posicionXInicial = player.getX();
    }

    @Given("estoy en la posición más a la derecha del tablero")
    public void estoy_en_la_posicion_mas_a_la_derecha() {
        // Calculamos el límite derecho exacto
        int maxDerecha = Commons.BOARD_WIDTH - Commons.BORDER_RIGHT - player.getImage().getWidth(null);
        player.setX(maxDerecha);
        posicionXInicial = player.getX();
    }

    // WHEN

    @When("pulso la flecha a la izquierda")
    public void pulso_la_flecha_a_la_izquierda() {
        simularTecla(KeyEvent.VK_LEFT);
    }

    @When("pulso la flecha a la derecha")
    public void pulso_la_flecha_a_la_derecha() {
        simularTecla(KeyEvent.VK_RIGHT);
    }

    @When("pulso cualquier tecla que no es la flecha derecha o la flecha a la izquierda")
    public void pulso_cualquier_tecla_random() {
        simularTecla(KeyEvent.VK_A);
    }

    private void simularTecla(int keyCode) {
        KeyEvent keyEvent = new KeyEvent(board, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
        board.getKeyListeners()[0].keyPressed(keyEvent);
        // Recuerda que el método update() debe ser PUBLIC en Board.java
        board.update();
    }

    //  THEN

    @Then("la nave se mueve hacia la izquierda")
    public void la_nave_se_mueve_hacia_la_izquierda() {
        assertTrue(player.getX() < posicionXInicial, "La nave debería moverse a la izquierda");
    }

    @Then("la nave NO se mueve hacia la izquierda")
    public void la_nave_no_se_mueve_hacia_la_izquierda() {
        assertEquals(posicionXInicial, player.getX(), "La nave no debería moverse (tope izquierdo)");
    }

    @Then("la nave se mueve hacia la derecha")
    public void la_nave_se_mueve_hacia_la_derecha() {
        assertTrue(player.getX() > posicionXInicial, "La nave debería moverse a la derecha");
    }

    @Then("la nave NO se mueve hacia la derecha")
    public void la_nave_no_se_mueve_hacia_la_derecha() {
        assertEquals(posicionXInicial, player.getX(), "La nave no debería moverse (tope derecho)");
    }

    @Then("la nave NO se mueve")
    public void la_nave_no_se_mueve() {
        assertEquals(posicionXInicial, player.getX(), "La nave no debería moverse con tecla inválida");
    }
}