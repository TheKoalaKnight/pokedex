package pokedex;

import java.awt.Color;
import java.util.Map;

class PokemonType {
  private static Map<String, Color> TYPE_COLOR_MAP = Map.ofEntries(
    Map.entry("Normal", new Color(168,168,120)),
    Map.entry("Fighting", new Color(192,48,40)),
    Map.entry("Flying", new Color(168,144,240)),
    Map.entry("Poison", new Color(160,64,160)),
    Map.entry("Ground", new Color(224,192,104)),
    Map.entry("Rock", new Color(184,160,56)),
    Map.entry("Bug", new Color(168,184,32)),
    Map.entry("Ghost", new Color(112,88,152)),
    Map.entry("Steel", new Color(184,184,208)),
    Map.entry("Fire", new Color(240,128,48)),
    Map.entry("Water", new Color(104,144,240)),
    Map.entry("Grass", new Color(120,200,80)),
    Map.entry("Electric", new Color(248,208,48)),
    Map.entry("Psychic", new Color(248,88,136)),
    Map.entry("Ice", new Color(152,216,216)),
    Map.entry("Dragon", new Color(112,56,248)),
    Map.entry("Dark", new Color(112,88,72))
  );


  String type;
  PokemonType(String type) {
    this.type = type;
  }

  public Color getAppropriateColor() {
    if(!TYPE_COLOR_MAP.containsKey(type)) {
      return Color.BLACK;  
    }

    return TYPE_COLOR_MAP.get(type);
  }
}



class Pokemon {
  public String name;
  public int id;
  public float height;
  public float weight;
  public String species;
  public String bio;
  public PokemonType[] types;

  public Pokemon(String name, int id, float height, float weight, String species, String bio, String[] types) {
    this.name = name;
    this.id = id;
    this.height = height;
    this.weight = weight;
    this.species = species;
    this.bio = bio;
    this.types = new PokemonType[types.length];
    for(int i = 0 ; i < types.length ; i++){
      this.types[i] = new PokemonType(types[i]);
    }
  }

  String getFormattedName() {
    return PokedexAPI.formatName(name);
  }
}