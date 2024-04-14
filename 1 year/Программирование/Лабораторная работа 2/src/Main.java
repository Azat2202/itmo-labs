import Pokemons.*;
import ru.ifmo.se.pokemon.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();

        Moltres molt = new Moltres("Caddilac", 50);
        Sentret sent = new Sentret("El Problema", 50);
        Furret furr = new Furret("SHOW", 50);
        Litwick litw = new Litwick("Dinero", 50);
        Lampent lamp = new Lampent("Я КОГДА НИБУДЬ УЙДУ", 50);
        Chandelure chand = new Chandelure("NOMINALLO", 50);

        b.addAlly(molt);
        b.addAlly(lamp);
        b.addAlly(furr);

        b.addFoe(sent);
        b.addFoe(litw);
        b.addFoe(chand);

        b.go();
    }
}