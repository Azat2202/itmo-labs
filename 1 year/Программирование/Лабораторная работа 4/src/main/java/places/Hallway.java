package places;

import characters.Human;

public class Hallway extends Room{
    public Hallway(String name){
        super(name);
    }
    public Hallway(String name, Human[] crowd) {
        super(name, crowd);
    }
}