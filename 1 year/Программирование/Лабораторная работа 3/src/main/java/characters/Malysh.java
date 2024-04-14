package characters;

public class Malysh extends  Human{
    public Malysh(String name) {
        super(name);
    }
    public Malysh(String name, Position position){
        super(name, position);
    }
    public void gasp(){
        super.makeSound(Sound.GASP);
    }
}
