package places;

import characters.Human;

public class PrivateRoom extends Room{
    public PrivateRoom(String name){
        super(name);
    }
    public PrivateRoom(String name, Human[] crowd) {
        super(name, crowd);
    }
}