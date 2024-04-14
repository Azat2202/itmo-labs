package places;

import characters.Human;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Room{
    private final LinkedList<Human> people = new LinkedList<Human>();
    private final String name;
    private Light light = Light.DARKNESS;
    Room(String n) {
        name = n;
    }
    Room(String n, Human[] crowd){
        name = n;
        people.addAll(List.of(crowd));
    }
    public String getName(){
        return name;
    }
    protected void addPerson(Human human){
        people.add(human);
        System.out.println(human.getName() + " заходит в " + getName());
    }
    protected void addPersons(Human[] crowd){
        people.addAll(List.of(crowd));
    }
    public boolean setLight(Light illumination){
        System.out.println("В " + getName() + " включается " + illumination.name());
        this.light = illumination;
        return onChangeLight();
    }
    public Light getLight(){
        return this.light;
    }
    public Object[] getPeople(){
        return people.toArray();
    }
    public void movePersonTo(Human human, Room destination){
        this.delPerson(human);
        destination.addPerson(human);
    }
    protected void delPerson(Human human){
        people.remove(human);
    }
    protected boolean onChangeLight(){
        if (light == Light.TORCH){
            System.out.println("Дядя Юлиус скалит зубы");
            return true;
        }
        return false;
    }
    @Override
    public boolean equals(Object o ){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        if(!Arrays.equals(this.getPeople(), room.getPeople())){
            return false;
        }
        if(!this.getLight().equals(room.getLight())){
            return false;
        }
        return room.getName().equals(this.getName());
    }
    @Override
    public String toString(){
        return name;
    }
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = result + people.hashCode();
        result = result + light.hashCode();
        return result;
    }
}
