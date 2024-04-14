package characters;

public enum Position{
    STAND("STAMD"),
    LIE("LIE"),
    LEAN("LEAN");
    private final String pos;
    Position(String color){
        this.pos = color;
    }
    public String getPosition(){ return pos;}
}
