package pokedex;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

class InfoWindow extends DrawableSubWindow {
  private static final Color BACKGROUND_COLOR = Color.WHITE;
  private static final Color HEADER_BACKGROUND_COLOR = new Color(230, 230, 230);
  static final Color TEXT_COLOR = new Color(100, 100, 100);
  static final Color TEXT_SHADOW_COLOR = new Color(200, 200, 200);
  int typeHeight;
  int typeWidth;
  int scale;
  private Font font;

  InfoWindow(int x, int y, int scale, Font font) {
    this.scale = scale;
    this.width = scale * 15;
    this.height =  scale * 11;
    this.x = x;
    this.y = y;
    this.font = font;
    typeHeight = (int)(scale * 1.5);
    typeWidth = 3 * scale;
  }

  private void drawText(Pokemon pokemon, int x, int y, Graphics2D graphics) {
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

  public void drawTypeText(Graphics2D graphics, String text, int rectX, int rectY, Font font) {
    Rectangle rect = new Rectangle(rectX, rectY, typeWidth, typeHeight);
    FontMetrics metrics = graphics.getFontMetrics(font);
    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
    graphics.setFont(font);
    graphics.drawString(text, x, y);
}

  private void drawType(PokemonType type, int x, int y, Graphics2D graphics) {
    Font font = this.font.deriveFont(scale * 1.0f).deriveFont(Font.BOLD);
    graphics.setFont(font);
    graphics.setColor(type.getAppropriateColor());
    graphics.fillRect(x, y, typeWidth, typeHeight);
    graphics.setStroke(new BasicStroke((int)(scale / 12.5)));
    graphics.setColor(new Color(0, 0, 0, 0.1f));
    graphics.drawRect(x, y, typeWidth, typeHeight);
    graphics.setColor(Color.WHITE);
    drawTypeText(graphics, type.type, x, y, font);
    graphics.setColor(new Color(240, 240, 240));
    drawTypeText(graphics, type.type, (int)(x + scale / 25), (int)(y + scale / 25), font);
  } 

  private void drawTypes(Pokemon pokemon, int x, int y, Graphics2D graphics) {
    for(int i = 0 ; i < pokemon.types.length ; i++) {
      drawType(pokemon.types[i], x + i * (typeWidth + scale / 2), y, graphics);
    }
    graphics.setFont(font);
  }
  
  @Override
  public void draw(Pokemon pokemon, Graphics2D graphics) {
    graphics.setColor(BACKGROUND_COLOR);
    graphics.fillRect(x, y, width, height);

    graphics.setColor(HEADER_BACKGROUND_COLOR);
    graphics.fillRect(x, y, width, scale * 2);

    graphics.setColor(TEXT_SHADOW_COLOR);
    drawText(pokemon, x + (int)(scale / 12.5), y + (int)(scale / 12.5), graphics);
    graphics.setColor(TEXT_COLOR);
    drawText(pokemon, x, y, graphics);

    drawTypes(pokemon, (int)(x + 3 * scale), (int)(y + 5.3 * scale), graphics);
  }

  @Override
  public void handleMouseClick(int x, int y, Pokedex pokedex) {
    
  }

  @Override
  public void handleMouseRelease(int x, int y, Pokedex pokedex) {

  }
}