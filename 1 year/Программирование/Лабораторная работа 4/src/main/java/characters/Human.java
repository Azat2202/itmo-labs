package characters;

import places.Light;
import places.Room;
import tools.Item;

import java.util.Arrays;
import java.util.LinkedList;

public abstract class Human {
    private final String name;
    private Position position = Position.STAND;
    private final LinkedList<Item> items = new LinkedList<>();
    private boolean sleep = false;
    public Human(String name) {
        this.name = name;
    }

    public Human(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public void addItem(Item thing) {
        items.add(thing);
    }

    public Object[] getItems() {
        return items.toArray();
    }

    public void makeSound(Sound sound) {
        System.out.println(name + " " + sound.getSound());
    }

    public void setSleep(boolean isSleeping) {
        this.sleep = isSleeping;
    }
    public boolean getSleep() {
        return sleep;
    }

    public void setPosition(Position pos) {
        position = pos;
        makeSound(Sound.BOLT);
    }

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return this.name;
    }

    public void think(String text) {
        System.out.println(this.getName() + " думает " + "\"" + text + "\"");
    }
    public void say(String text) {
        System.out.println(this.getName() + " говорит: \"" + text + '"');
    }
    public void changeLight(Room room, Light light) {
        if (this.getSleep()){
            return;
        }
        if (room.setLight(light)) {
            System.out.println(getName() + " переключает свет в " + room.getName() + " на " + light.name());
        } else {
            System.out.println(getName() + " не может переключить свет");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Human human = (Human) o;
        if (!this.getPosition().equals(human.getPosition())) {
            return false;
        }
        if (!Arrays.equals(getItems(), ((Human) o).getItems())) {
            return false;
        }
        return getName().equals(human.getName());
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = result + position.hashCode();
        result = result + items.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}

