package Pokemons;

import Moves.PhysicalMoves.*;
import Moves.SpecialMoves.*;
import Moves.StatusMoves.*;
import ru.ifmo.se.pokemon.*;

public class Moltres extends Pokemon {
    public Moltres(String name, int lvl) {
        super(name, lvl);
        this.setType(Type.FIRE, Type.FLYING);
        this.setStats(90, 100, 90, 125, 85, 90);
        this.addMove(new FireBlast());
        this.addMove(new Hurricane());
        this.addMove(new Swagger());
        this.addMove(new Confide());
    }
}
