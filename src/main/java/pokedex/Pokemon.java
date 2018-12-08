package pokedex;

class Pokemon {
  public String name;
  public int id;
  public float height;
  public float weight;
  public String species;
  // String bio;

  public Pokemon(String name, int id, float height, float weight, String species) {
    this.name = name;
    this.id = id;
    this.height = height;
    this.weight = weight;
    this.species = species;
  }

  String getFormattedName() {
    return PokedexAPI.formatName(name);
  }
}