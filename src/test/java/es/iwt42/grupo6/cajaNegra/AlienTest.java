package es.iwt42.grupo6.cajaNegra;

import main.Commons;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import space_invaders.sprites.Alien;

import java.awt.Image;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.Arguments;

class AlienTest {

    // Atajos para constantes del tablero
    private static final int W = Commons.BOARD_WIDTH;   // 358 en el informe
    private static final int H = Commons.BOARD_HEIGHT;  // 350 en el informe

    // ---------------------------------------------------------------------
    // CP-A-00  (Constructor: new Alien(x,y) inicializa estado base)
    // ---------------------------------------------------------------------
    @ParameterizedTest(name = "CP-A-00: Constructor Alien({0},{1}) inicializa bomba y estado visible")
    @CsvSource({
            "100,200",
            "0,0",
            "150,10"
    })
    @DisplayName("CP-A-00: Inicialización del Alien y su bomba")
    void cp_a_00_constructorInitializesState(int x, int y) {

        Alien alien = new Alien(x, y);

        // 1) El alien existe
        assertNotNull(alien, "Alien no debería ser null");

        // 2) La bomba existe
        Alien.Bomb bomb = alien.getBomb();
        assertNotNull(bomb, "La bomba del Alien debería estar inicializada");

        // 3) La posición inicial observable coincide con la del constructor
        //    (esto es caja negra: le pedimos getX/getY públicos)
        assertEquals(x, alien.getX(), "X inicial del Alien no coincide con el valor pasado al constructor");
        assertEquals(y, alien.getY(), "Y inicial del Alien no coincide con el valor pasado al constructor");

        // 4) El alien tiene imagen
        Image img = alien.getImage();
        assertNotNull(img, "Alien debería tener imagen asignada al construirse");

        // 5) Bomba empieza destruida
        assertFalse(bomb.isDestroyed(), "La bomba debería iniciar activa (destroyed=false) para poder caer");

        // 6) Bomba toma las coordenadas iniciales del alien
        assertEquals(x, bomb.getX(), "X inicial de la bomba debería coincidir con el Alien");
        assertEquals(y, bomb.getY(), "Y inicial de la bomba debería coincidir con el Alien");
    }

    // ---------------------------------------------------------------------
    // CP-A-01 .. CP-A-07 (initAlien / constructor posicionando coordenadas)
    //
    // Casos:
    // CP-A-01: (300,200)   -> (300,200)
    // CP-A-02: (0,0)       -> (0,0)
    // CP-A-03: (W,H)       -> (W,H)
    // CP-A-04: (-10,100)   -> (0,100)
    // CP-A-05: (100,-10)   -> (100,0)
    // CP-A-06: (383,100)   -> (358,100)   // clamp X a W
    // CP-A-07: (100,375)   -> (100,350)   // clamp Y a H
    //
    // Nota: Los últimos 3 dependen de W y H, así que vamos a usar MethodSource
    // para poder pasar dinámicamente esos valores esperados.
    // ---------------------------------------------------------------------

    private static Stream<Arguments> cp_a_01_a_07_positionsProvider() {
        return Stream.of(
                // CP-A-01: Coordenadas válidas dentro de límites
                Arguments.of("CP-A-01: válidas dentro de límites",
                        300, 200,
                        300, 200),

                // CP-A-02: Borde exacto (0,0)
                Arguments.of("CP-A-02: borde exacto (0,0)",
                        0, 0,
                        0, 0),

                // CP-A-03: Borde exacto (W,H)
                Arguments.of("CP-A-03: borde exacto (W,H)",
                        W, H,
                        W, H),

                // CP-A-04: X negativa → clamp a 0
                Arguments.of("CP-A-04: X negativa → clamp a 0",
                        -10, 100,
                        0, 100),

                // CP-A-05: Y negativa → clamp a 0
                Arguments.of("CP-A-05: Y negativa → clamp a 0",
                        100, -10,
                        100, 0),

                // CP-A-06: X excede BOARD_WIDTH → clamp a W
                Arguments.of("CP-A-06: X excede BOARD_WIDTH",
                        W + 25, 100,
                        W, 100),

                // CP-A-07: Y excede BOARD_HEIGHT → clamp a H
                Arguments.of("CP-A-07: Y excede BOARD_HEIGHT",
                        100, H + 25,
                        100, H)
        );
    }

    @ParameterizedTest(name = "{0}: Alien({1},{2}) ⇒ ({3},{4})")
    @MethodSource("cp_a_01_a_07_positionsProvider")
    @DisplayName("CP-A-01 .. CP-A-07: initAlien / constructor posiciona dentro de los límites del tablero")
    void cp_a_01_a_07_initAlien_coordinatesAreClampedAsExpected(
            String label,
            int givenX,
            int givenY,
            int expectedX,
            int expectedY
    ) {
        Alien alien = new Alien(givenX, givenY);

        assertEquals(expectedX, alien.getX(),
                label + " → X final incorrecta (esperada " + expectedX + ")");
        assertEquals(expectedY, alien.getY(),
                label + " → Y final incorrecta (esperada " + expectedY + ")");
    }

    // ---------------------------------------------------------------------
    // CP-A-08 .. CP-A-10 (act(int direction) con UNA sola llamada)
    //
    // CP-A-08: direction = 0,   start(100,200) → (100,200)
    // CP-A-09: direction = +20, start(100,200) → (120,200)
    // CP-A-10: direction = -10, start(100,200) → (90,200)
    //
    // Estas son variaciones del mismo patrón, así que usamos CsvSource
    // ---------------------------------------------------------------------

    @ParameterizedTest(
            name = "{0}: Alien({1},{2}).act({3}) ⇒ ({4},{5})"
    )
    @CsvSource({
            // label,       startX, startY, direction, expectedX, expectedY
            "'CP-A-08: sin movimiento',           100, 200, 0,    100, 200",
            "'CP-A-09: movimiento a la derecha',  100, 200, 20,   120, 200",
            "'CP-A-10: movimiento a la izquierda',100, 200, -10,  90,  200"
    })
    @DisplayName("CP-A-08 .. CP-A-10: act(direction) con una sola invocación")
    void cp_a_08_a_10_act_singleStep(
            String label,
            int startX,
            int startY,
            int direction,
            int expectedX,
            int expectedY
    ) {
        Alien alien = new Alien(startX, startY);

        alien.act(direction);

        assertEquals(expectedX, alien.getX(),
                label + " → X final incorrecta tras act(" + direction + ")");
        assertEquals(expectedY, alien.getY(),
                label + " → Y final incorrecta tras act(" + direction + ")");
    }

    // ---------------------------------------------------------------------
    // CP-A-11 (Secuencia de movimientos)
    //
    // CP-A-11: start(70,120), act(+3) luego act(-5) → (68,120)
    //
    // Es conceptualmente distinto porque hay dos llamadas consecutivas,
    // así que le damos su propio MethodSource de un único caso para
    // seguir cumpliendo "parametrizado".
    // ---------------------------------------------------------------------

    private static Stream<Arguments> cp_a_11_sequenceProvider() {
        return Stream.of(
                Arguments.of(
                        "CP-A-11: Secuencia (+3, luego -5)",
                        70, 120,    // startX, startY
                        3, -5,      // firstDir, secondDir
                        68, 120     // expectedX, expectedY
                )
        );
    }

    @ParameterizedTest(name = "{0}: start({1},{2}) -> act({3}) luego act({4}) ⇒ ({5},{6})")
    @MethodSource("cp_a_11_sequenceProvider")
    @DisplayName("CP-A-11: Secuencia de movimientos (+3, luego -5)")
    void cp_a_11_act_sequence(
            String label,
            int startX,
            int startY,
            int firstDir,
            int secondDir,
            int expectedX,
            int expectedY
    ) {
        Alien alien = new Alien(startX, startY);

        alien.act(firstDir);   // e.g. +3
        alien.act(secondDir);  // e.g. -5

        assertEquals(expectedX, alien.getX(),
                label + " → X final incorrecta tras la secuencia");
        assertEquals(expectedY, alien.getY(),
                label + " → Y final incorrecta tras la secuencia");
    }

    /*Test para bomb()*/
    @ParameterizedTest
    @CsvSource(value = {
    "-2,175", "0,175", "2,175", "179,175", "356,175", "358,175", "360,175",
    "179,-1", "179,0", "179,1", "179,349", "179,350", "179,351"
    })
    void bombTest(int x, int y) {
        Alien.Bomb bomb = new Alien(Commons.BOARD_WIDTH/2,Commons.BOARD_HEIGHT/2).new Bomb(x,y);
        boolean insideWidthRange = bomb.getX() >= 0 && bomb.getX() <= Commons.BOARD_WIDTH;
        boolean insideHeightRange = bomb.getY() >= 0 && bomb.getY() <= Commons.BOARD_HEIGHT;
        assertTrue(insideWidthRange && insideHeightRange);
    }
}
