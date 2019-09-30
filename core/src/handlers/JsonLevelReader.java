package handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Gamelevel;
import entities.Gridposition;
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

    public Gamelevel generateLevel(String filepath) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(filepath)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            Gamelevel level = new Gamelevel();
            String name = jsonObject.get("name").toString();
            Long numberOfGuests = (Long) jsonObject.get("numberOfGuests");
            level.setName(name);
            level.setNumberOfGuests(numberOfGuests);
            JSONArray positions = (JSONArray) jsonObject.get("positions");
            for (Object o : positions) {
                Gridposition gridposition = convertJsonToObject(o);
                gridpositions.add(gridposition);
            }
            level.setGridpositionList(gridpositions);
            return level;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Gridposition convertJsonToObject(Object s) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (objectMapper.readValue(s.toString(), Gridposition.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

