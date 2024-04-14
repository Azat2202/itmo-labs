package Pokemons;

import Moves.PhysicalMoves.*;
import Moves.SpecialMoves.*;
import Moves.StatusMoves.*;
import ru.ifmo.se.pokemon.*;

public class Sentret extends Pokemon {
    public Sentret(String name, int lvl) {
        super(name, lvl);
        this.setType(Type.NORMAL);
        this.setStats(35, 46, 34, 35, 45, 20);
        this.addMove(new HyperVoice());
        this.addMove(new Amnesia());
        this.addMove(new Confide());
    }
}
