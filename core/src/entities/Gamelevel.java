package entities;

import java.util.List;

public class Gamelevel {
    private List<Gridposition> gridpositionList;
    private List<Guest> guests;
    private String name;


    public List<Gridposition> getGridpositionList() {
        return gridpositionList;
    }

    public void setGridpositionList(List<Gridposition> gridpositionList) {
        this.gridpositionList = gridpositionList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }
}

