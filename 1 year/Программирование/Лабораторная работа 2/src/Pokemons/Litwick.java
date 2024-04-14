package Pokemons;

import Moves.PhysicalMoves.*;
import Moves.SpecialMoves.*;
import Moves.StatusMoves.*;
import ru.ifmo.se.pokemon.*;

public class Litwick extends Pokemon{
    public Litwick(String name, int lvl){
        super(name, lvl);
        this.setType(Type.GHOST, Type.FIRE);
        this.setStats(50, 30, 55, 65, 55, 20);
        this.addMove(new WillOWisp());
        this.addMove(new Rest());
    }
}
