package Pokemons;

import Moves.StatusMoves.*;

public class Furret extends Sentret{
    public Furret(String name, int lvl) {
        super(name, lvl);
        this.setStats(85, 76, 64, 45, 55, 90);
        this.addMove(new Agility());
    }
}
