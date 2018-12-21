package pokedex;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.List;

abstract class DrawableSubWindow {
  protected int x;
  protected int y;
  protected int width;
  protected int height;

  public int getX() {
    return x;
  };
  public int getY() {
    return y;
  };
  public int getHeight() {
    return height;
  };
  public int getWidth() {
    return width;
  };

  public boolean isInside(int x, int y) {
    return x >= getX() && x <= getX() + getWidth() && y >= getY() && y <= getY() + getHeight();  
  }
  

  public abstract void draw(Pokemon pokemon, Graphics2D graphics);
  public abstract void handleMouseClick(int x, int y, Pokedex pokedex);
  public abstract void handleMouseRelease(int x, int y, Pokedex pokedex);
}

class BottomBar extends DrawableSubWindow {
  private final static Color BACKGROUND_COLOR = new Color(30, 30, 30);
  private final static Color DARKER_ARROW_COLOR = new Color(100, 100, 100);
  private final static Color LIGHTER_ARROW_COLOR = new Color(190, 190, 190);
  private final static Color PRESSED_KEY_COLOR = new Color(50, 50, 50);
  private int upArrowX;
  private int downArrowX;
  private int upArrowY;
  private int downArrowY;
  private int scale;
  private int arrowWidth;
  private int arrowsHeight;
  boolean upArrowPressed = false;
  boolean downArrowPressed = false;

  private enum ArrowDirection {
    UP, DOWN
  }
  
  BottomBar(int x, int y, int scale) {
    this.scale = scale;
    this.width = scale * Pokedex.WIDTH_SCALE_RATIO;
    this.height = scale * 2;
    this.x = x;
    this.y = y;
    upArrowX = x + scale / 2;
    upArrowY = y + scale / 2;
    downArrowX = (int)(x + 5 * scale / 2);
    downArrowY = y + scale / 2;
    arrowWidth = scale;
    arrowsHeight = y + scale + scale / 6;
  }

  public void handleMouseClick(int x, int y, Pokedex pokedex)  {
    if(x >= upArrowX && x <= upArrowX + arrowWidth && y >= upArrowY && y <= upArrowY + arrowsHeight) { // If the user has clicked on the up arrow
      pokedex.setNextPokemon();
      upArrowPressed = true;
    }
    else if(x >= downArrowX && x <= downArrowX + arrowWidth && y >= downArrowY && y <= downArrowY + arrowsHeight) { // If the user has clicked on the down arrow
      pokedex.setPreviousPokemon();
      downArrowPressed = true;
    }
  }

  @Override
  public void handleMouseRelease(int x, int y, Pokedex pokedex) {
    upArrowPressed = false;
    downArrowPressed = false;
  }

  private Polygon getScrollTriangle(int x, int y, ArrowDirection direction) { 
    if(direction == ArrowDirection.UP) { // Don't ask why this code works, but it does, and I spent quite a lot of time with it
      
      return new Polygon(new int[] {x + scale / 6, x, x + scale / 2,
          x + scale, x + scale - scale / 6, x + scale / 2}, 
        new int[] {y + scale / 2 + scale / 6, y + scale / 2, y, y + scale / 2,
          y + scale / 2 + scale / 6, y + scale / 3},
        6);

    } else if(direction == ArrowDirection.DOWN) {

      return new Polygon(new int[] {x + scale / 6, x, x + scale / 2,
          x + scale, x + scale - scale / 6, x + scale / 2},
        new int[] {y, y + scale / 6, y + scale / 2 + scale / 6, y + scale / 6,
          y, y + scale / 3}, 
        6);

    }

    return null;
  }

  private void drawUpArrows(int x, int y, Graphics2D graphics) {
    graphics.setColor(upArrowPressed ? PRESSED_KEY_COLOR : LIGHTER_ARROW_COLOR);
    graphics.fillPolygon(getScrollTriangle(x, y, ArrowDirection.UP));
    graphics.setColor(upArrowPressed ? PRESSED_KEY_COLOR : DARKER_ARROW_COLOR);
    graphics.fillPolygon(getScrollTriangle(x, y + scale / 2, ArrowDirection.UP));
  }

  private void drawDownArrows(int x, int y, Graphics2D graphics) {
    graphics.setColor(downArrowPressed ? PRESSED_KEY_COLOR : DARKER_ARROW_COLOR);
    graphics.fillPolygon(getScrollTriangle(x, y, ArrowDirection.DOWN));
    graphics.setColor(downArrowPressed ? PRESSED_KEY_COLOR : LIGHTER_ARROW_COLOR);
    graphics.fillPolygon(getScrollTriangle(x, y + scale / 2, ArrowDirection.DOWN));
  }

  @Override
  public void draw(Pokemon pokemon, Graphics2D graphics) {
    graphics.setColor(BACKGROUND_COLOR);
    graphics.fillRect(x, y, width, height);

    graphics.setColor(Color.WHITE);
    drawUpArrows(upArrowX, upArrowY, graphics);
    drawDownArrows(downArrowX, downArrowY, graphics);
  }
}


class BioWindow extends DrawableSubWindow {
  private static final Color BACKGROUND_COLOR = new Color(55, 55, 55);
  private static final Color TEXT_COLOR = new Color(255, 255, 255);
  int scale;
  Font font;

  BioWindow(int x, int y, int scale, Font font) {
    this.scale = scale;
    width = 32 * scale;
    height = (int)(4.5 * scale);
    this.font = font;
    this.x = x;
    this.y = y;
  }

  @Override
  public void draw(Pokemon pokemon, Graphics2D graphics) {
    graphics.setColor(BACKGROUND_COLOR);
    graphics.fillRect(x, y, width, height);

    FontMetrics fontMetrics = graphics.getFontMetrics(font);
    List<String> strings = StringUtils.wrap(pokemon.bio, fontMetrics, width - 2 * scale);
  
    graphics.setColor(TEXT_COLOR);
    for(int i = 0 ; i < strings.size() ; i++) {
      graphics.drawString((String)strings.get(i), x + scale, y + (int)(1.5 * scale) + fontMetrics.getHeight() * i);
    }
  }

  @Override
  public void handleMouseClick(int x, int y, Pokedex pokedex) {
    
  }

  @Override
  public void handleMouseRelease(int x, int y, Pokedex pokedex) {

  }
}