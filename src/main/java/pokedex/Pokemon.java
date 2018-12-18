package pokedex;

class Pokemon {
  public String name;
  public int id;
  public float height;
  public float weight;
  public String species;
  public String bio;

  public Pokemon(String name, int id, float height, float weight, String species) {
    this.name = name;
    this.id = id;
    this.height = height;
    this.weight = weight;
    this.species = species;
    this.bio = "They raise their heads to intimidate opponents, but only give it their all when fighting a powerful opponent.";
  }

  String getFormattedName() {
    return PokedexAPI.formatName(name);
  }
}