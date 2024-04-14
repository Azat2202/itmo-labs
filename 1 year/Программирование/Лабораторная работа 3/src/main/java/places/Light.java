package places;

public enum Light {
    DARKNESS("DARKNESS"),
    TORCH("TORCH"),
    MAIN_LIGHT("MAIN_LIGHT");
    private final String light;
    Light(String illumination){
        this.light = illumination;
    }
    public String getIllumination(){
        return this.light;
    }
}
