import characters.*;
import places.*;
import tools.*;

public class Main {
    public static void main(String[] args) {
        Tool wire = new Tool("Проволочка", false);
        Belt belt = new Belt("Ремень");
        Belt belt2 = new Belt("Ремень");

        Malysh malysh = new Malysh("Малыш");
        Malysh malysh2 = new Malysh("Малыш");
        Karlson karlson = new Karlson("Карлсон");
        Julious julious = new Julious("дядя Юлиус");
        julious.setMummy(true);
        FrekenBok frekenBok = new FrekenBok("фрекен Бок");
        Mom mom = new Mom("Мамочка");

        Hall hall = new Hall("Зал", new Human[]{malysh, karlson, frekenBok, mom});
        Hallway hallway = new Hallway("Коридор", new Human[]{julious});
        Kitchen kitchen = new Kitchen("Кухня");
        kitchen.setFuses(false);
        PrivateRoom frekenBokRoom = new PrivateRoom("Комната фрекен бок");
        PrivateRoom juliousRoom = new PrivateRoom("Комната Юлиуса");


        Door doorFrekenBokRoomHallway = new Door("Дверь комнаты Фрекен Бок", DoorPosition.CLOSED);
        Door doorJuliousRoomHallway = new Door("Дверь комнаты Юлиуса", DoorPosition.CLOSED);
        Door doorHallHallway = new Door("Дверь в коридор", DoorPosition.LOCKED);


        wire.use(karlson, doorHallHallway);
        doorHallHallway.openDoor(DoorPosition.AJAR);
        hallway.movePersonTo(julious, hall);
        malysh.gasp();
        julious.makeSound(Sound.WISPER);
        julious.makeSound(Sound.STEPS);
        julious.makeSound(Sound.BOLT);
        mom.makeSound(Sound.SCREAM);
        frekenBok.makeSound(Sound.SCREAM);
        julious.setPosition(Position.LEAN);
        hall.setLight(Light.TORCH);
        hall.setLight(Light.DARKNESS);
        mom.makeSound(Sound.LOUD_SCREAMING);
        doorHallHallway.openDoor(DoorPosition.OPEN);

        // Малыш ничего не понимает
        julious.setMummy(false);
        hall.movePersonTo(julious, juliousRoom);
        hall.movePersonTo(frekenBok, frekenBokRoom);
        System.out.println("Проходит некоторое количество времени");
        // Малыш очнулся

        doorFrekenBokRoomHallway.openDoor();
        doorJuliousRoomHallway.openDoor();
        frekenBokRoom.movePersonTo(frekenBok, hallway);
        juliousRoom.movePersonTo(julious, hallway);
        frekenBok.makeSound(Sound.STEPS);
        julious.makeSound(Sound.STEPS);
        belt.use(mom);
        frekenBok.changeLight(kitchen, Light.MAIN_LIGHT);
        frekenBok.changeLight(kitchen, Light.MAIN_LIGHT);
        frekenBok.changeLight(kitchen, Light.MAIN_LIGHT);
        karlson.say("Проказничать лучше в темноте");
    }
}