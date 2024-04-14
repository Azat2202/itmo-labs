package tools;

import characters.Human;
import places.Door;

public class Tool extends Item implements DoorOpener {
    protected boolean canOpenDoor = false;
    public Tool(String n){
        super(n);
    }
    public Tool(String name, boolean ability){
        super(name);
        this.canOpenDoor = ability;
    }
    public void use(Human human, Door door){
        door.unlock(this);
        System.out.println(this.getName() + " используется "+ human.getName() + " на " + door.getName());
    }
    public boolean isOpener(){
        return canOpenDoor;
    }
}
