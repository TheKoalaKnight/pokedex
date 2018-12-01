package pokedex;

class Pokemon {
  String name;
  // String bio;
  int id;
  float height;
  float weight;

  Pokemon(String name, int id, float height, float weight) {
    this.name = name;
    this.id = id;
    this.height = height;
    this.weight = weight;
  }

  String getFormattedName() {
    return PokedexAPI.formatName(name);
  }
}