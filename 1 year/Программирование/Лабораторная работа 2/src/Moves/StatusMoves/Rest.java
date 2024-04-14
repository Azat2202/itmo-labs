package Moves.StatusMoves;

import ru.ifmo.se.pokemon.*;

public class Rest extends StatusMove{
    public Rest(){
        super(Type.PSYCHIC, 0, 0);
    }
    @Override
    protected boolean checkAccuracy(Pokemon att, Pokemon def){
        return true;
    }
    @Override
    protected void applySelfEffects(Pokemon p) {
        Effect eff = new Effect().condition(Status.SLEEP).turns(2);
        p.restore();
        p.addEffect(eff);
    }
    @Override
    protected String describe(){
        return "применяет Rest";
    }
}
