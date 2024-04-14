package places;

import characters.Human;
import tools.CanBeOpened;

public class Hall extends Room{
    public class Mailbox implements CanBeOpened {
        private boolean state = false;
        public Mailbox() {
        }

        public Mailbox(boolean state) {
            this.state = state;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        @Override
        public void open() {
            this.state = true;
        }
    }
    public Hall(String name){
        super(name);
    }
    public Hall(String name, Human[] crowd) {
        super(name, crowd);
    }
}
