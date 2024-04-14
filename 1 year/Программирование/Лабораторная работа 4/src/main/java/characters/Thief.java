package characters;

import tools.CanBeOpened;

public class Thief extends Human implements Burglar{
    public Thief(String name) {
        super(name);
    }

    public Thief(String name, Position position) {
        super(name, position);
    }
    @Override
    public void openThing(CanBeOpened thing) {
        System.out.println(this.getName() + " открыл " + thing.toString());
        thing.open();
    }
}
