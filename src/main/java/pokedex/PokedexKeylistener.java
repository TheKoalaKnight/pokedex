package pokedex;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class PokedexKeylistener implements KeyListener {
  Pokedex pokedex;

  PokedexKeylistener(Pokedex pokedex) {
    this.pokedex = pokedex;
  }

  @Override
  public void keyPressed(KeyEvent event) {
    if(event.getKeyCode() == KeyEvent.VK_LEFT) {
      pokedex.setPreviousPokemon();
    } else if(event.getKeyCode() == KeyEvent.VK_RIGHT){
      pokedex.setNextPokemon();
    }
  }

  @Override
  public void keyTyped(KeyEvent event) {

  }

  @Override
  public void keyReleased(KeyEvent event) {
    
  }
}