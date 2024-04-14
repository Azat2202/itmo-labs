package Pokemons;

import Moves.PhysicalMoves.FireBlast;
import ru.ifmo.se.pokemon.*;

public class Chandelure extends Lampent{
    public Chandelure(String name, int lvl) {
        super(name, lvl);
        super.setStats(60, 55, 90, 145, 90, 80);
        super.addMove(new FireBlast());
    }
}
