package entities;

import java.util.List;

public class Gamelevel {
    private List<Gridposition> gridpositionList;
    private Long numberOfGuests;
    private String name;


    public List<Gridposition> getGridpositionList() {
        return gridpositionList;
    }

    public void setGridpositionList(List<Gridposition> gridpositionList) {
        this.gridpositionList = gridpositionList;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
