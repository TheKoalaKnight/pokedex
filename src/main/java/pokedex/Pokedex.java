package pokedex;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import screen.DrawableScreen;

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
    graphics.drawString("WT", x + 3 * scale, y + scale * 10);
    graphics.drawString(weight, x + 10 * scale, y + scale * 10);
  }
  

  public void draw(int x, int y, Graphics2D graphics) {
    graphics.setColor(BACKGROUND_COLOR);
    graphics.fillRect(x, y, width, height);

    graphics.setColor(HEADER_BACKGROUND_COLOR);
    graphics.fillRect(x, y, width, scale * 2);

    // graphics.fillRect(x + scale / 2, y + (int)(scale * 8.4), width - scale, scale / 3);
    // graphics.fillRect(x + scale / 2, y + (int)(scale * 9.4), width - scale, scale / 3);

    graphics.setColor(TEXT_SHADOW_COLOR);
    drawText(x + 2, y + 2, graphics);
    graphics.setColor(TEXT_COLOR);
    drawText(x, y, graphics);
  }
}

public class Pokedex extends DrawableScreen {
  private static final String WINDOW_TITLE = "Pok\u00E9dex";
  private static final String FONT_PATH = "PressStart2P-Regular.ttf";
  private static final String IMAGE_URL_TEMPLATE = "https://img.pokemondb.net/sprites/black-white/normal/%s.png";
  private static final int WINDOW_WIDTH = 800;
  private static final Color BACKGROUND_RED = new Color(205, 0, 0);
  private static final Color BACKGROUND_LINE_RED = new Color(125, 0, 0);
  private static final Color BACKGROUND_GREY = new Color(150, 150, 150);
  private int scale;
  private int strokeThickness;
  private Font font;
  private Pokemon[] pokemons;
  private int currentID;
  private Image image;

  @Override
  protected void setState() {
    scale = width / 32;
    strokeThickness = width / 400;
    Data data = new Data();
    currentID = 1;

    if(!data.dataAvailable()) {
      System.out.println("Before continuing, we will have to gather the data from our database. This might take a few minutes!");
    }
    try {
      pokemons = data.getData();
      font = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_PATH)).deriveFont(20.0f);
      image = getCurrentImage();
    } catch(IOException e) {
      System.out.println("An error occured during the gathering of data. Please restart the program!");
      return;
    } catch(FontFormatException e) {
      System.out.println("An error occured while loading the font. Please check the integrity of the font file.");
      return;
    }
    System.out.println("Done!");
  }

  @Override
  protected void prepareFrame() {}

  @Override
  protected void drawFrame(Graphics g) {
    Graphics2D graphics = (Graphics2D) g;
    graphics.setFont(font);
    drawBackground(graphics); 
    InfoWindow infoWindow = new InfoWindow(getCurrentPokemon(), scale);
    infoWindow.draw(scale * 15, (int)(scale * 1.5), graphics);
    drawPokemonImage(graphics);
  }

  Image getCurrentImage() throws IOException {
    BufferedImage img = ImageIO.read(new URL(String.format(IMAGE_URL_TEMPLATE, getCurrentPokemon().getFormattedName().toLowerCase()))); // "https://img.pokemondb.net/sprites/black-white/normal/" + getCurrentPokemon().getFormattedName().toLowerCase() + ".png"
    return img.getScaledInstance(8 * scale, 8 * scale, Image.SCALE_DEFAULT);
  }

  private Pokemon getCurrentPokemon() {
    return pokemons[currentID - 1];
  }

  void drawPokemonImage(Graphics2D graphics) {
    graphics.drawImage(image, 2 * scale, 2 * scale, null);
  }

  void drawBackground(Graphics2D graphics) {
    graphics.setColor(BACKGROUND_RED);
    graphics.fillRect(0, 0, width, height);

    graphics.setColor(BACKGROUND_LINE_RED);
    graphics.setStroke(new BasicStroke(strokeThickness));
    for(int i = 0 ; i < width ; i += scale) {
      graphics.drawLine(0, i, width, i);
      graphics.drawLine(i, 0, i, height);
    }
    
    graphics.setColor(BACKGROUND_GREY);
    graphics.fillRect(0, scale, scale * 8, scale * 8);
    graphics.fillRect(scale * 8, scale * 2, scale * 24, scale * 8);

    graphics.fillPolygon(new Polygon(new int[] {scale * 8, scale * 8, scale * 9}, new int[] {scale, scale * 2, scale * 2}, 3));
    graphics.fillPolygon(new Polygon(new int[] {scale * 7, scale * 8, scale * 8}, new int[] {scale * 9, scale * 9, scale * 10}, 3));
  }

  public Pokedex(int width) {
    super(WINDOW_TITLE, width, width * 5 / 8);
  }

  public static void main(String[] args) {
    Pokedex pokedex = new Pokedex(WINDOW_WIDTH);
    pokedex.start();
  }
} 
