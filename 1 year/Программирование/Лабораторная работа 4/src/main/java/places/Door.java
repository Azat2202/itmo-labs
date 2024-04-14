package places;

import tools.CanBeOpened;
import tools.Tool;

public class Door implements CanBeOpened{
    private final String name;
    DoorPosition doorPosition = DoorPosition.CLOSED;

    Door(String n){
        name = n;
    }
    public Door(String n, DoorPosition cond){
        name = n;
        doorPosition = cond;
    }

    public void unlock(Tool tool){
        if (tool.isOpener()) {
            setPosition(DoorPosition.CLOSED);
        }
    }
    public void open(){
        if (doorPosition != DoorPosition.LOCKED){
            setPosition(DoorPosition.OPEN);
            System.out.println(getName() + " " + DoorPosition.OPEN.name());
        }
    }
    public String getName(){
        return name;
    }
    public void open(DoorPosition cond){
        if (doorPosition != DoorPosition.LOCKED){
            setPosition(cond);
            System.out.println(this.getName() + cond.name());
        }
    }
    private void setPosition(DoorPosition cond){
        doorPosition = cond;

    }
    public DoorPosition getPosition(){
        return doorPosition;
    }
}
