package pokedex;

import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.awt.Color;

class InfoWindow {
  private static final Color BACKGROUND_COLOR = Color.WHITE;
  private static final Color HEADER_BACKGROUND_COLOR = new Color(230, 230, 230);
  static final Color TEXT_COLOR = new Color(100, 100, 100);
  static final Color TEXT_SHADOW_COLOR = new Color(200, 200, 200);
  Pokemon pokemon;
  int width;
  int height;
  int scale;

  InfoWindow(Pokemon pokemon, int scale) {
    this.scale = scale;
    this.pokemon = pokemon;
    width = scale * 15;
    height =  scale * 11;
  }

  // TODO: Draw pokemon type

  void drawText(int x, int y, Graphics2D graphics) {
    String id = String.format("%03d", pokemon.id);
    graphics.drawString(id, x + (int)(scale * 0.5) , y + (int)(scale * 1.5));

    String name = pokemon.name;
    graphics.drawString(name, x + 5 * scale, y + (int)(scale * 1.5));

    String species = pokemon.species;
    graphics.drawString(species, x + 3 * scale, y + scale * 4);

    DecimalFormat format = new DecimalFormat("#.##");

    String height = format.format(pokemon.height) +  " m";
    graphics.drawString("HT", x + 3 * scale, y + scale * 9);
    graphics.drawString(height, x + (int)(10 * scale), y + scale * 9);
    
    String weight = format.format(pokemon.weight) + " kg";
    graphics.drawString("WT", x + 3 * scale, y + (int)(scale * 10.5));
    graphics.drawString(weight, x + 10 * scale, y + (int)(scale * 10.5));
  }
  

  public void draw(int x, int y, Graphics2D graphics) {
    graphics.setColor(BACKGROUND_COLOR);
    graphics.fillRect(x, y, width, height);

    graphics.setColor(HEADER_BACKGROUND_COLOR);
    graphics.fillRect(x, y, width, scale * 2);

    graphics.setColor(TEXT_SHADOW_COLOR);
    drawText(x + (int)(scale / 12.5), y + (int)(scale / 12.5), graphics);
    graphics.setColor(TEXT_COLOR);
    drawText(x, y, graphics);
  }
}