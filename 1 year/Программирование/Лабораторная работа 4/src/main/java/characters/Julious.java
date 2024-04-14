package characters;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void say(String text, boolean lisping) {
        if (lisping) {
            super.say(Pattern.compile("([сщзчЧЗСЩ])").matcher(text).replaceAll("ш"));
        } else {
            super.say(text);
        }

    }
}
