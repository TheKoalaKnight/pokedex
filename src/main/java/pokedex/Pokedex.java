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

import javax.imageio.ImageIO;

import screen.DrawableScreen;


public class Pokedex extends DrawableScreen {
  private static final String WINDOW_TITLE = "Pok\u00E9dex";
  private static final String RESOURCE_DIR = "resources/";
  private static final String FONT_PATH = RESOURCE_DIR + "pokemon-b-w.ttf";
  private static final String IMAGE_URL_TEMPLATE = "https://img.pokemondb.net/sprites/black-white/normal/%s.png";
  private static final String MISSING_IMAGE_PATH = RESOURCE_DIR + "unknownSprite.png";
  private static final Color BACKGROUND_RED = new Color(205, 0, 0);
  private static final Color BACKGROUND_LINE_RED = new Color(125, 0, 0);
  private static final Color BACKGROUND_GREY = new Color(150, 150, 150);
  public static final int WINDOW_WIDTH = 800;
  public static final int WIDTH_SCALE_RATIO = 32;
  private int scale;
  private int strokeThickness;
  private Font font;
  private Pokemon[] pokemons;
  private int currentID;
  private Image image;
  private Image missingImage;
  DrawableSubWindow[] subWindows;

  @Override
  protected void setState() {
    scale = (int)(width / WIDTH_SCALE_RATIO);
    strokeThickness = width / 400;
    Data data = new Data();
    currentID = 1;

    addKeyListener(new PokedexKeylistener(this));
    addMouseListener(new PokedexMouseListener(this));

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
    

    InfoWindow infoWindow = new InfoWindow(scale * 15, (int)(scale * 1.5), scale, font);
    BioWindow bioWindow = new BioWindow(0, (int)(scale * 13.0), scale, font);
    BottomBar bottomBar = new BottomBar(0, height - scale * 2, scale);

    subWindows = new DrawableSubWindow[] { infoWindow, bioWindow, bottomBar };
  }

  @Override
  protected void prepareFrame() {}

  @Override
  protected void drawFrame(Graphics g) {
    Graphics2D graphics = (Graphics2D) g;
    

    graphics.setFont(font);
    drawBackground(graphics);
    drawPokemonImage(graphics);
    for(DrawableSubWindow subWindow : subWindows) {
      subWindow.draw(getCurrentPokemon(), graphics);
    }
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