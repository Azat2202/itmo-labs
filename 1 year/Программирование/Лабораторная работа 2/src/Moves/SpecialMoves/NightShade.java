package Moves.SpecialMoves;

import ru.ifmo.se.pokemon.*;

public class NightShade extends SpecialMove {
    public NightShade(){
        super(Type.GHOST, 0, 100);
    }
    @Override
    protected double calcBaseDamage(Pokemon att, Pokemon def){
        return att.getLevel();
    }
    @Override
    protected String describe(){
        return "применяет NightShade";
    }
}
