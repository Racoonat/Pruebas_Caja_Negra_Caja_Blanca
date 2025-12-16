Feature: Mover jugador

  Como Jugador
  Quiero poder mover la nave horizontalmente
  Para poder esquivar las balas y matar marcianos

  Rule: Movimientos a la izquierda

    Scenario: Movimiento a la izquierda
      Given Dado el tablero del juego Space Invaders
      And   no estoy en la posición más a la izquierda del tablero
      When  pulso la flecha a la izquierda
      Then  la nave se mueve hacia la izquierda

    Scenario: Tratar de mover a la izquierda en el extremo izquierdo
      Given Dado el tablero del juego Space Invaders
      And   estoy en la posición más a la izquierda del tablero
      When  pulso la flecha a la izquierda
      Then  la nave NO se mueve hacia la izquierda

  Rule: Movimientos a la derecha

    Scenario: Movimiento a la derecha
      Given Dado el tablero del juego Space Invaders
      And   no estoy en la posición más a la derecha del tablero
      When  pulso la flecha a la derecha
      Then  la nave se mueve hacia la derecha

    Scenario: Tratar de mover a la derecha en el extremo derecho
      Given Dado el tablero del juego Space Invaders
      And   estoy en la posición más a la derecha del tablero
      When  pulso la flecha a la derecha
      Then  la nave NO se mueve hacia la derecha

  Rule: Sin movimiento

    Scenario: Pulsar otra tecla
      Given Dado el tablero del juego Space Invaders
      When  pulso cualquier tecla que no es la flecha derecha o la flecha a la izquierda
      Then  la nave NO se mueve