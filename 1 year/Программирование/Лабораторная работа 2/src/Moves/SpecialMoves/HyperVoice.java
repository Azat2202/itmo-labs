package Moves.SpecialMoves;

import ru.ifmo.se.pokemon.*;

public class HyperVoice extends SpecialMove {
    public HyperVoice(){
        super(Type.NORMAL, 90, 100);
    }
    @Override
    protected String describe(){
        return "применяет Swagger";
    }
}
