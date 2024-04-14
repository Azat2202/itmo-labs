package places;

import characters.Human;

public class Hall extends Room{
    public Hall(String name){
        super(name);
    }
    public Hall(String name, Human[] crowd) {
        super(name, crowd);
    }
}
