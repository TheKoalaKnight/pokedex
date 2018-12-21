package pokedex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

class Data {
  private static final String RESOURCE_PATH = "resources/";
  private static final String DATA_PATH = RESOURCE_PATH + "data.json";
  private static final String ID_KEY = "id";
  private static final String NAME_KEY = "name";
  private static final String WEIGHT_KEY = "weight";
  private static final String HEIGHT_KEY = "height";
  private static final String SPECIES_KEY = "species";
  private static final String BIO_KEY = "bio";
  private static final String TYPES_KEY = "types";
  private static final String ARRAY_KEY = "data";
  private static final List<String> UNPERMITTED_TYPES = List.of("Fairy"); 
  private Pokemon[] pokemons;

  Data() {
    pokemons = null;
  }

  private JSONObject pokemonToJSON(Pokemon pokemon) {
    JSONObject object = new JSONObject();
    object.put(NAME_KEY, pokemon.name);
    object.put(WEIGHT_KEY, pokemon.weight);
    object.put(HEIGHT_KEY, pokemon.height);
    object.put(ID_KEY, pokemon.id);
    object.put(SPECIES_KEY, pokemon.species);
    object.put(BIO_KEY, pokemon.bio);
    object.put(TYPES_KEY, new JSONArray(pokemon.types));
    return object;
  }

  private String readRawContent() throws IOException {
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

  private String[] parseTypes(JSONObject object) {
    JSONArray typeArray = object.getJSONArray(TYPES_KEY);
    int numberOfPermittedTypes = 0;
    for(int i = 0 ; i < typeArray.length() ; i++) {
      if(!UNPERMITTED_TYPES.contains(typeArray.getString(i))) {
        numberOfPermittedTypes++;
      }
    }

    String types[] = new String[numberOfPermittedTypes];

    int i = 0;
    for(Object arrayObject : typeArray) {
      String type = (String)arrayObject;
      if(UNPERMITTED_TYPES.contains(type)) {
        continue;
      }

      types[i++] = type;
    }
    
    return types;
  }

  private Pokemon pokemonFromJSON(JSONObject object){
    String name = object.getString(NAME_KEY);
    int id = object.getInt(ID_KEY);
    float weight = object.getFloat(WEIGHT_KEY);
    float height = object.getFloat(HEIGHT_KEY);
    String species = object.getString(SPECIES_KEY);
    String bio = object.getString(BIO_KEY);
    String[] types = parseTypes(object);
    return new Pokemon(name, id, height, weight, species, bio, types);
  }

  private void readFile() throws IOException {
    String rawContent = readRawContent();
    JSONArray array =  new JSONObject(rawContent).getJSONArray(ARRAY_KEY);
    pokemons = new Pokemon[array.length()];
    for(int i = 0 ; i < array.length() ; i++) {
      JSONObject object = array.getJSONObject(i);
      pokemons[i] = pokemonFromJSON(object);
    }
  }

  private JSONObject dataToJSON(){
    JSONArray array = new JSONArray();
    for(Pokemon pokemon : pokemons) {
      array.put(pokemonToJSON(pokemon));
    }
    JSONObject object = new JSONObject();
    object.put(ARRAY_KEY, array);
    
    return object;
  }

  private void writeData() {
    try {
      pokemons = PokedexAPI.run();
    } catch(IOException e) {
      System.out.println("Something went wrong with the API");
      return;
    }
    
    JSONObject object = dataToJSON();

    try {
      writeObject(object);
    } catch(IOException e) {
      System.out.println("Something went wrong with the file");
    }
  }

  private void writeObject(JSONObject object) throws IOException {
    File resourceDirectory = new File(RESOURCE_PATH);
    if(!(resourceDirectory.exists() && resourceDirectory.isDirectory() )) {
      resourceDirectory.mkdir();
    }


    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(DATA_PATH));
    bufferedWriter.write(object.toString());
    bufferedWriter.close();
  }

  public boolean isDataLoaded() {
    return pokemons != null;
  }

  public Pokemon[] getData() throws IOException {
    if(!dataAvailable()) {
      writeData();
    }
    if(!isDataLoaded()) {
      readFile();
    }

    return pokemons;
  }

  public boolean dataAvailable() {
    File file = new File(DATA_PATH);
    return file.exists() && !file.isDirectory();
  }

  public void printData() {
    System.out.println(dataToJSON().getJSONArray(ARRAY_KEY).toString(4));
  }
}