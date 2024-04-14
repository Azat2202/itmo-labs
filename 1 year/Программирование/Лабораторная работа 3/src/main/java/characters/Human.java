package characters;

import places.Light;
import places.Room;
import tools.Item;

import java.util.Arrays;
import java.util.LinkedList;

public abstract class Human {
    protected String name;
    protected Position position = Position.STAND;
    protected LinkedList<Item> items = new LinkedList<Item>();
    public Human(String name){
        this.name = name;
    }
    public Human(String name, Position position){
        this.name = name;
        this.position = position;
    }
    public void addItem(Item thing){
        items.add(thing);
    }
    public Object[] getItems(){
        return items.toArray();
    }
    public void makeSound(Sound sound){
        System.out.println(name + " " + sound.getSound());
    }
    public void setPosition(Position pos) {
        position = pos;
        makeSound(Sound.BOLT);
    }
    public Position getPosition(){
        return position;
    }
    public String getName() {
        return this.name;
    }
    public void say(String text){
        System.out.println(this.getName() + " говорит: \"" + text + '"');
    }
    public void changeLight(Room room, Light light){
        if(room.setLight(light)) {
            System.out.println(getName() + " переключает свет в " + room.getName() + " на " + light.name());
        }
        else{
            System.out.println(getName() + " не может переключить свет");
        }
    }
    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Human human = (Human) o;
        if(! this.getPosition().equals(human.getPosition())){
            return false;
        }
        if(! Arrays.equals(getItems(), ((Human) o).getItems())){
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
    public String toString(){
        return name;
    }
}

