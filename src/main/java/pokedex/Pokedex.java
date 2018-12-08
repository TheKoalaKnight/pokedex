package pokedex;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.IOException;
import java.text.DecimalFormat;

import screen.DrawableScreen;

class InfoWindow {
  Pokemon pokemon;
  InfoWindow(Pokemon pokemon) {
    this.pokemon = pokemon;
  }

  public void draw(int x, int y, Graphics2D graphics, int scale) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(x, y, scale * 15, scale * 11);
    graphics.setColor(Color.BLACK);

    String id = String.format("%03d", pokemon.id);
    graphics.drawString(id, x + scale, y + scale);

    String name = pokemon.name;
    graphics.drawString(name, x + 5 * scale, y + scale);

    String species = pokemon.species;
    graphics.drawString(species, x + 5 * scale, y + scale * 2);

    DecimalFormat format = new DecimalFormat("#.##");

    String height = format.format(pokemon.height) + " m";
    graphics.drawString("HT", x + 5 * scale, y + scale * 8);
    graphics.drawString(height, x + 10 * scale, y + scale * 8);
    
    String weight = format.format(pokemon.weight) + " kg";
    graphics.drawString("WT", x + 5 * scale, y + scale * 9);
    graphics.drawString(weight, x + 10 * scale, y + scale * 9);
  }
}

public class Pokedex extends DrawableScreen {
  private static final String WINDOW_TITLE = "Pok\u00E9dex";
  private static final int WINDOW_WIDTH = 800;
  private int scale = WINDOW_WIDTH / 32;
  private int strokeThickness = WINDOW_WIDTH / 400;
  private static final Color BACKGROUND_RED = new Color(205, 0, 0);
  private static final Color BACKGROUND_LINE_RED = new Color(125, 0, 0);
  private static final Color BACKGROUND_GREY = new Color(170, 170, 170);
  private Pokemon[] pokemons;
  private int currentID;

  @Override
  protected void setState() {
    Data data = new Data();
    currentID = 1;
    if(!data.dataAvailable()) {
      System.out.println("Before continuing, we will have to gather the data from our database. This might take a few minutes!");
    }
    try {
      pokemons = data.getData();
    } catch(IOException e) {
      System.out.println("An error occured during the gathering of data. Please restart the program!");
      return;
    }
    System.out.println("Done!");
  }

  private Pokemon getCurrentPokemon() {
    return pokemons[currentID - 1];
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

  @Override
  protected void prepareFrame() {}

  @Override
  protected void drawFrame(Graphics g) {
    Graphics2D graphics = (Graphics2D) g;
    drawBackground(graphics); 

    new InfoWindow(getCurrentPokemon()).draw(scale * 15, scale, graphics, scale);
  }

  public Pokedex(int width) {
    super(WINDOW_TITLE, width, width * 5 / 8);
    scale = width / 32;
    strokeThickness = width / 400;
  }

  public static void main(String[] args) {
    Pokedex pokedex = new Pokedex(WINDOW_WIDTH);
    pokedex.start();
  }
} 
