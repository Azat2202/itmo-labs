package Moves.SpecialMoves;

import ru.ifmo.se.pokemon.*;

public class Hurricane extends SpecialMove{
    public Hurricane(){
        super(Type.FLYING, 110, 70);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        Effect eff = new Effect().chance(0.3);
        p.addEffect(eff);
        if(eff.success()) {
            p.confuse();
        }
    }
    @Override
    protected String describe(){
        return "применяет Hurricane";
    }
}
