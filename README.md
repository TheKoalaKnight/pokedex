# Pokédex

A simple pokédex application, based on the pokédex from Pokémon Black & White 2. All the information about the pokemon comes from the website 'https://pokemondb.net/pokedex/national'. The program repository comes with a json file containing all the information about the pokémon, but if the file is deleted, the program will automatically restore it by parsing the website. This might, however, take a few minutes.

To run the program, all that is required is JDK 9. To run, simply execute
```
gradlew.bat run
```
on Windows, or
```
gradlew run
```
on MacOS or Linux.

On certain machines, some letters are rendered incorrectly. Currently, there is no known solution to this problem, but if you find one, please submit a pull request.
