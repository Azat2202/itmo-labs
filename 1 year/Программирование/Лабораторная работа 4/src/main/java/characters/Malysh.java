package characters;

public class Malysh extends Human{
    public Malysh(String name) {
        super(name);
    }
    public Malysh(String name, Position position){
        super(name, position);
    }

    @Override
    public void think(String text) throws OutOfMemoryError{
        class OutOfHumanMemoryError extends Error{
            public OutOfHumanMemoryError() {
                super();
            }

            public OutOfHumanMemoryError(String message) {
                super(message);
            }

            public OutOfHumanMemoryError(String message, Throwable cause) {
                super(message, cause);
            }

            public OutOfHumanMemoryError(Throwable cause) {
                super(cause);
            }

            protected OutOfHumanMemoryError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
                super(message, cause, enableSuppression, writableStackTrace);
            }
        }
        if (text.length() > 128) {
            throw new OutOfHumanMemoryError("У малыша нет столько памяти((((((((");
        }
        super.think(text);
    }

    public void gasp(){
        super.makeSound(Sound.GASP);
    }
}
