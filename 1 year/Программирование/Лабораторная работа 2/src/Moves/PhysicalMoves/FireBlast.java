package Moves.PhysicalMoves;

import ru.ifmo.se.pokemon.*;

public class FireBlast extends SpecialMove {
    public FireBlast(){
        super(Type.FIRE, 110, 85);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        Effect eff = new Effect().chance(0.1).condition(Status.BURN);
        p.addEffect(eff);
    }
    @Override
    protected String describe(){
        return "применяет Fireblast";
    }
}
