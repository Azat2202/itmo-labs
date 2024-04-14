package tools;

import characters.Human;
import characters.Position;

public class Belt extends Item implements Weapon{
    protected boolean isDeadly = false;
    public Belt(String n){
        super(n);
    }
    public Belt(String n, Boolean deadliness){
        super(n);
        isDeadly = deadliness;
    }
    @Override
    public void use(Human human) {
        if(!isDeadly) {
            human.setPosition(Position.LIE);
            addToPerson(human);
        }
        else{
            System.out.println("DONT KILL ANYBODY ITS CHILD STORY");
        }
    }
    private void addToPerson(Human human){
        human.addItem(this);
        System.out.println(this.getName() + " используется на " + human.getName());
    }
}
