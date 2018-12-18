package pokedex;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import screen.DrawableScreen;

class BioWindow {
  private static final Color BACKGROUND_COLOR = new Color(55, 55, 55);
  private static final Color TEXT_COLOR = new Color(255, 255, 255);
  Pokemon pokemon;
  int scale;
  int width;
  int height;
  Font font;

  BioWindow(Pokemon pokemon, int scale, Font font) {
    this.pokemon = pokemon;
    this.scale = scale;
    width = 32 * scale;
    height = 5 * scale;
    this.font = font;
  }

  public void draw(int x, int y, Graphics2D graphics) {
    graphics.setColor(BACKGROUND_COLOR);
    graphics.fillRect(x, y, width, height);

    FontMetrics fontMetrics = graphics.getFontMetrics(font);
    List<String> strings = StringUtils.wrap(pokemon.bio, fontMetrics, width - 2 * scale);
  
    graphics.setColor(TEXT_COLOR);
    for(int i = 0 ; i < strings.size() ; i++) {
      graphics.drawString((String)strings.get(i), x + scale, y + (int)(1.5 * scale) + fontMetrics.getHeight() * i);
    }
  }
}

public class Pokedex extends DrawableScreen {
  private static final String WINDOW_TITLE = "Pok\u00E9dex";
  private static final String FONT_PATH = "pokemon-b-w.ttf";
  private static final String IMAGE_URL_TEMPLATE = "https://img.pokemondb.net/sprites/black-white/normal/%s.png";
  private static final String MISSING_IMAGE_PATH = "unknownSprite.png";
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
  private Image missingImage;

  // TODO: Draw bottom bar
  // TODO: Draw string if image is missing

  @Override
  protected void setState() {
    scale = width / 32;
    strokeThickness = width / 400;
    Data data = new Data();
    currentID = 1;

    addKeyListener(new PokedexKeylistener(this));

    if(!data.dataAvailable()) {
      System.out.println("Before continuing, we will have to gather the data from our database. This might take a few minutes!");
    }
    try {
      pokemons = data.getData();
      font = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_PATH)).deriveFont(scale * 1.6f);
      image = getCurrentImage();
      missingImage = loadMissingImage();
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
    BioWindow bioWindow = new BioWindow(getCurrentPokemon(), scale, font);
    bioWindow.draw(0, (int)(scale * 13.2), graphics);
  }

  void setNextPokemon() {
    if(currentID >= pokemons.length) {
      return;
    }

    currentID++;
    try {
      image = getCurrentImage();
    } catch(IOException e) {
      System.err.println("For some reason, we couldn't get the image. Please check your internet connection!");
      image = missingImage;
    }
  }

  void setPreviousPokemon() {
    if(currentID <= 1) {
      return;
    }

    currentID--;
    try {
      image = getCurrentImage();
    } catch(IOException e) {
      System.err.println("For some reason, we couldn't get the image. Please check your internet connection!");
      image = missingImage;
    }
  }

  Image loadMissingImage() throws IOException {
    BufferedImage img = ImageIO.read(new File(MISSING_IMAGE_PATH)); // "https://img.pokemondb.net/sprites/black-white/normal/" + getCurrentPokemon().getFormattedName().toLowerCase() + ".png"
    return img.getScaledInstance(8 * scale, 8 * scale, Image.SCALE_DEFAULT);
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