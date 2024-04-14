package Pokemons;

import Moves.SpecialMoves.NightShade;

public class Lampent extends Litwick{
    public Lampent(String name, int lvl) {
        super(name, lvl);
        super.setStats(60,40, 60, 95, 60, 55);
        super.addMove(new NightShade());
    }
}
