package pokedex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

class Data {
  static final String DATA_PATH = "data.json";
  private Pokemon[] pokemons;
  private final String ID_IDENTIFIER = "id";
  private final String NAME_IDENTIFIER = "name";
  private final String WEIGHT_IDENTIFIER = "weight";
  private final String HEIGHT_IDENTIFIER = "height";

  Data() {
    pokemons = null;
  }

  JSONObject pokemonToJSON(Pokemon pokemon) {
    JSONObject object = new JSONObject();
    object.put(NAME_IDENTIFIER, pokemon.name);
    object.put(WEIGHT_IDENTIFIER, pokemon.weight);
    object.put(HEIGHT_IDENTIFIER, pokemon.height);
    object.put(ID_IDENTIFIER, pokemon.id);
    return object;
  }

  String readRawContent() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(DATA_PATH));
    String content = "";
    String line = reader.readLine();
    while(line != null) {
      content += line;
      line = reader.readLine();
    }
    reader.close();
    return content;
  }

  Pokemon pokemonFromJSON(JSONObject object){
    String name = object.getString(NAME_IDENTIFIER);
    int id = object.getInt(ID_IDENTIFIER);
    float weight = object.getFloat(WEIGHT_IDENTIFIER);
    float height = object.getFloat(HEIGHT_IDENTIFIER);
    return new Pokemon(name, id, height, weight);
  }

  void readFile() throws IOException {
    String rawContent = readRawContent();
    JSONArray array =  new JSONObject(rawContent).getJSONArray("data");
    pokemons = new Pokemon[array.length()];
    for(int i = 0 ; i < array.length() ; i++) {
      JSONObject object = array.getJSONObject(i);
      pokemons[i] = pokemonFromJSON(object);
    }
  }

  void writeData() {
    try {
      pokemons = PokedexAPI.run();
    } catch(IOException e) {
      System.out.println("Something went wrong with the API");
      return;
    }

    System.out.println("The data has been gathered, please wait while we write the data to a file");
    
    JSONArray array = new JSONArray();
    for(Pokemon pokemon : pokemons) {
      array.put(pokemonToJSON(pokemon));
    }
    JSONObject object = new JSONObject();
    object.put("data", array);

    try {
      writeObject(object);
    } catch(IOException e) {
      System.out.println("Something went wrong with the file");
    }
  }

  private void writeObject(JSONObject object) throws IOException {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(DATA_PATH));
    bufferedWriter.write(object.toString());
    bufferedWriter.close();
  }

  boolean dataAvailable() {
    File file = new File(DATA_PATH);
    return file.exists() && !file.isDirectory();
  }

  public static void main(String[] args) {
    Data data = new Data();
    if(!data.dataAvailable()) {
      System.out.println("A data file was not available, this might take a few minutes. Please wait.");
      data.writeData();
      System.out.println("The file has now been written!");
    } else {
      System.out.println("A data file was found!");
      try {
        data.readFile();
      } catch(IOException e) {
        System.out.println("The file could not be read!");
        return;
      }
    }
    for(Pokemon pokemon : data.pokemons) {
      System.out.println("Name: " + pokemon.name);
      System.out.println("Id: " + pokemon.id);
      System.out.println("Height: " + pokemon.height);
      System.out.println("Weight: " + pokemon.weight + "\n");
    }
  }

}

