package entities;

import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

public class Gamelevel {
    private List<Gridposition> gridpositionList;
    private List<Guest> guests;
    private String name;
    private int money;
    private Walls walls;

    public void generateWalls(World world) {
        this.walls = new Walls(world);
    }

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

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

}

