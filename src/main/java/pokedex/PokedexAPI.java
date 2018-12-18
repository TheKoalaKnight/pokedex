package pokedex;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

final public class PokedexAPI {
  static final String NAME_DATA_URL = "https://pokemondb.net/pokedex/national";
  static final String POKEMON_DATA_URL = "https://pokemondb.net/pokedex/";
  static final int NUMBER_OF_POKEMON = 649;

  private PokedexAPI() {}

  private static Pokemon parseData(String name) throws IOException {
    Document document = Jsoup.connect(POKEMON_DATA_URL + formatName(name)).get();
    
    int id = parseId(document);
    float weight = parseWeight(document);
    float height = parseHeight(document);
    String species = parseSpecies(document);
    String bio = parseBio(document);

    return new Pokemon(name, id, weight, height, species); // Fix
  }

  private static String parseSpecies(Document document) {
    return document.selectFirst("th:contains(Species) + td").text();
  }

  private static String parseBio(Document document) {
    Element bioDiv = document.selectFirst("div.resp-scroll");

    System.out.println(bioDiv.text());

    return "";
  }

  private static float parseWeight(Document document) {
    String string = document.selectFirst("th:contains(Weight) + td").text();

    return Float.parseFloat(string.substring(string.indexOf("(") + 1, string.indexOf("kg") - 1));
  }

  private static float parseHeight(Document document) {
    String string = document.selectFirst("th:contains(Height) + td").text();

    return Float.parseFloat(string.substring(string.indexOf("(") + 1, string.indexOf("m") - 1));
  }

  private static int parseId(Document document) {
    String string = document.selectFirst("th:contains(National) + td > strong").text();

    return Integer.parseInt(string);
  }

  static String formatName(String name) {
    return name.replace("\u2642", "-m").replace("\u2640", "-f").replace("'", "")
      .replace(". ", "-").replace(": ", "-").replace(".", "").replace(" ", "-").replace("\u00E9", "e");
  }

  private static String[] parseNames(Elements elements) {
    String[] names = new String[NUMBER_OF_POKEMON];
    int i = 0;
    for(Element element : elements) {
      String name = element.selectFirst("span[class='infocard-lg-data text-muted'] > a").text();
      names[i++] = name;
      if(i >= NUMBER_OF_POKEMON) {
        break;
      }
    }
    return names;
  }

  private static Elements getPokemonElements(Document document) {
    return document.select("div.infocard");
  }

  public static Pokemon[] run() throws IOException {
    Document document = Jsoup.connect(NAME_DATA_URL).get();
    
    Elements infoCards = getPokemonElements(document);
    String[] names = parseNames(infoCards);
    Pokemon[] pokemons = new Pokemon[names.length];
    int i = 0;
    for(String name : names) {
      pokemons[i++] = parseData(name);
    }
    return pokemons;
  }
}