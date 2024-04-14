package characters;

public enum Position{
    STAND("STAMD"),
    LIE("LIE"),
    SQUATTING("SQUATIING"),
    LEAN("LEAN");
    private final String pos;
    Position(String color){
        this.pos = color;
    }
    public String getPosition(){ return pos;}
}
