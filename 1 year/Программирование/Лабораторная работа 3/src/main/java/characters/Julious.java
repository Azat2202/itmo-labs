package characters;

public class Julious extends Human{
    private boolean being_mummy = false;
    public Julious(String name) {
        super(name);
    }
    public Julious(String name, Position position){
        super(name, position);
    }
    public void setMummy(boolean state){
        being_mummy = state;
    }
    public boolean isMummy(){
        return being_mummy;
    }
}
