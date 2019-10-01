package handlers;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Gamelevel;
import entities.Gridposition;
import entities.Guest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class JsonLevelReader {
    private List<Gridposition> gridpositions = new ArrayList<>();
    private List<Guest> guestList = new ArrayList<>();

    public Gamelevel generateLevel(String filepath) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("../../level1")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            Gamelevel level = new Gamelevel();
            String name = jsonObject.get("name").toString();
            level.setName(name);
            JSONArray positions = (JSONArray) jsonObject.get("positions");
            JSONArray guests = (JSONArray) jsonObject.get("guests");
            for (Object o : positions) {
                Gridposition gridposition = convertJsonToGridPosition(o);
                gridpositions.add(gridposition);
            }
            int index = 0;
            JSONObject res;
            for (Object o : guests) {
                res = (JSONObject) guests.get(index);
                guestList.add(new Guest(
                        (long) res.get("spawntime"),
                        (long) res.get("happiness"),
                        (long) res.get("patience"),
                        (long) res.get("wealth")
                ));
                index++;
            }
            level.setGridpositionList(gridpositions);
            level.setGuests(guestList);
            Gdx.app.log("INFO: ", "level successfully read from JSON configuration file");
            return level;
        } catch (IOException | ParseException e) {
            Gdx.app.error("ERROR: ", "reading from JSON File failed", e);
        }
        return null;
    }

    private Gridposition convertJsonToGridPosition(Object s) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (objectMapper.readValue(s.toString(), Gridposition.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

