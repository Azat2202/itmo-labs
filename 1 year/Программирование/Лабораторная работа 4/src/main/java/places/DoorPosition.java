package places;

public enum DoorPosition {
    LOCKED("LOCKED"),
    OPEN("OPEN"),
    CLOSED("CLOSED"),
    AJAR("AJAR");
    private final String position;
    DoorPosition(String pos){
        this.position = pos;
    }
    public String getPosition(){
        return this.position;
    }
}
