# Parser for https://db-engines.com/

This program parses the page db-engines.com.

## How to Use
Build an run the Application

``` bash
mvn package
java -jar target/DBEngines-0.0.1-SNAPSHOT.jar
```
The program collects all URLs from https://db-engines.com/de/systems.

Then all system sites are parsed.

The output of the program is a single CSV with all parsed systems.