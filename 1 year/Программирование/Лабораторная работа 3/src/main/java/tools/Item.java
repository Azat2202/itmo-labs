package tools;

public abstract class Item {
    private final String name;
    public Item(String n){
        name = n;
    }
    public String getName(){
        return name;
    }
    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return getName().equals(item.getName());
    }
    @Override
    public String toString(){
        return name;
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
