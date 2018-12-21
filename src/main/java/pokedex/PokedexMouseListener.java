package pokedex;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


class PokedexMouseListener implements MouseListener {
  Pokedex pokedex;

  PokedexMouseListener(Pokedex pokedex)  {
    this.pokedex = pokedex;
  }

  public void mousePressed(MouseEvent event) {
    int x = event.getX();
    int y = event.getY();
    for(DrawableSubWindow subWindow : pokedex.subWindows) {
      if(subWindow.isInside(x, y)) {
        subWindow.handleMouseClick(x, y, pokedex);
      }
    }
  }

  public void mouseReleased(MouseEvent event) {
    int x = event.getX();
    int y = event.getY();
    for(DrawableSubWindow subWindow : pokedex.subWindows) {
      subWindow.handleMouseRelease(x, y, pokedex);
    }
  }

  public void mouseEntered(MouseEvent event) {}

  public void mouseExited(MouseEvent event) {}

  public void mouseClicked(MouseEvent event) {}
}