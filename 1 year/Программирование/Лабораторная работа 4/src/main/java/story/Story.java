package story;

import characters.*;
import places.*;
import tools.Belt;
import tools.Tool;
import tools.Weapon;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

/*
    Checked исключение: Room.movePersonTo();
    Unchecked исключение: Malysh.think();
    Локальный класс: Исключения
    Вложенный static: Story.Time
    Вложенный non-static: Hall.MailBox
    Анонимный класс: Охапка
*/

public class Story {
    private final Tool wire;
    private final Belt belt;
    private final Malysh malysh;
    private final Karlson karlson;
    private final Julious julious;
    private final FrekenBok frekenBok;
    private final Mom mom;
    private final Thief fille;
    private final Thief rulle;
    private final Hall hall;
    private final Hall.Mailbox mailBox;
    private final Hallway hallway;
    private final Kitchen kitchen;
    private final PrivateRoom frekenBokRoom;
    private final PrivateRoom juliousRoom;
    private final Door doorFrekenBokRoomHallway;
    private final Door doorJuliousRoomHallway;
    private final Door doorHallHallway;
    private final Time time;

    private static class Time {
        private Calendar calendar;

        public Time() {
            this.calendar = new GregorianCalendar();
        }

        public void timeGone(int hours) {
            switch (hours) {
                case 1, 21 -> System.out.println("Прошел " + hours + " час");
                case 2, 3, 4, 22, 23, 24 -> System.out.println("Прошло " + hours + " часа");
                default -> System.out.println("Прошло " + hours + " часов");
            }
            calendar.add(Calendar.HOUR, hours);
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public void setCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

        @Override
        public String
        toString() {
            return calendar.getTime().toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Time time)) return false;
            return Objects.equals(getCalendar(), time.getCalendar());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCalendar());
        }
    }

    public Story() {
        this.wire = new Tool("Проволочка", false);
        this.belt = new Belt("Ремень");
        this.malysh = new Malysh("Малыш");
        this.karlson = new Karlson("Карлсон");
        this.julious = new Julious("дядя Юлиус");
        julious.setMummy(true);
        this.frekenBok = new FrekenBok("фрекен Бок");
        this.mom = new Mom("Мамочка");
        this.fille = new Thief("Филле");
        this.rulle = new Thief("Рулле");

        this.hall = new Hall("Зал", new Human[]{malysh, karlson, frekenBok, mom});
        this.mailBox = hall.new Mailbox();
        this.hallway = new Hallway("Коридор");
        this.kitchen = new Kitchen("Кухня");
        kitchen.setFuses(false);
        this.frekenBokRoom = new PrivateRoom("Комната фрекен бок");
        this.juliousRoom = new PrivateRoom("Комната Юлиуса");
        this.doorFrekenBokRoomHallway = new Door("Дверь комнаты Фрекен Бок", DoorPosition.CLOSED);
        this.doorJuliousRoomHallway = new Door("Дверь комнаты Юлиуса", DoorPosition.CLOSED);
        this.doorHallHallway = new Door("Дверь в коридор", DoorPosition.LOCKED);
        this.time = new Time();
    }

    public void go() {
        new Weapon(){
            @Override
            public void use(Human human) {
                human.setPosition(Position.LIE);
            }
        }.use(this.julious);
        juliousRoom.movePersonTo(julious, hallway);
        mailBox.open();
        hallway.setLight(Light.DARKNESS);
        malysh.setPosition(Position.SQUATTING);
        karlson.setPosition(Position.SQUATTING);
        malysh.setSleep(true);
        time.timeGone(1);
        fille.makeSound(Sound.CLAPPING);
        rulle.makeSound(Sound.GRINDING);
        malysh.setSleep(false);
        wire.use(karlson, doorHallHallway);
        doorHallHallway.open(DoorPosition.AJAR);
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
        doorHallHallway.open(DoorPosition.OPEN);

        // Малыш ничего не понимает
        julious.setMummy(false);
        hall.movePersonTo(julious, juliousRoom);
        hall.movePersonTo(frekenBok, frekenBokRoom);
        time.timeGone(1);
        // Малыш очнулся

        doorFrekenBokRoomHallway.open();
        doorJuliousRoomHallway.open();
        frekenBokRoom.movePersonTo(frekenBok, hallway);
        juliousRoom.movePersonTo(julious, hallway);
        frekenBok.makeSound(Sound.STEPS);
        julious.makeSound(Sound.STEPS);
        belt.use(mom);
        frekenBok.changeLight(kitchen, Light.MAIN_LIGHT);
        frekenBok.changeLight(kitchen, Light.MAIN_LIGHT);
        frekenBok.changeLight(kitchen, Light.MAIN_LIGHT);
        karlson.say("Проказничать лучше в темноте");
        frekenBok.setPosition(Position.STAND);
        julious.setPosition(Position.STAND);
        frekenBok.say("Свет наверняка не включается, потому что на улице идет гром!");
        julious.say("Сказочные существа", true);
        malysh.think("Он ведь остался без зубов");
        malysh.think("Где Филле и Рулле?");
        malysh.think("Они.....убежали?....");
        malysh.think("""
                Я в своем познании настолько преисполнился, что я как будто бы уже

                сто триллионов миллиардов лет проживаю на триллионах и

                триллионах таких же планет, как эта Земля, мне этот мир абсолютно

                понятен, и я здесь ищу только одного - покоя, умиротворения и

                вот этой гармонии, от слияния с бесконечно вечным, от созерцания

                великого фрактального подобия и от вот этого замечательного всеединства

                существа, бесконечно вечного, куда ни посмотри, хоть вглубь - бесконечно

                малое, хоть ввысь - бесконечное большое, понимаешь? А ты мне опять со

                своим вот этим, иди суетись дальше, это твоё распределение, это

                твой путь и твой горизонт познания и ощущения твоей природы, он

                несоизмеримо мелок по сравнению с моим, понимаешь? Я как будто бы уже

                давно глубокий старец, бессмертный, ну или там уже почти бессмертный,

                который на этой планете от её самого зарождения, ещё когда только Солнце

                только-только сформировалось как звезда, и вот это газопылевое облако,

                вот, после взрыва, Солнца, когда оно вспыхнуло, как звезда, начало

                формировать вот эти коацерваты, планеты, понимаешь, я на этой Земле уже

                как будто почти пять миллиардов лет живу и знаю её вдоль и поперёк

                этот весь мир, а ты мне какие-то... мне не важно на твои тачки, на твои

                яхты, на твои квартиры, там, на твоё благо. Я был на этой

                планете бесконечным множеством, и круче Цезаря, и круче Гитлера, и круче

                всех великих, понимаешь, был, а где-то был конченым говном, ещё хуже,

                чем здесь. Я множество этих состояний чувствую. Где-то я был больше

                подобен растению, где-то я больше был подобен птице, там, червю, где-то

                был просто сгусток камня, это всё есть душа, понимаешь? Она имеет грани

                подобия совершенно многообразные, бесконечное множество. Но тебе этого

                не понять, поэтому ты езжай себе , мы в этом мире как бы живем

                разными ощущениями и разными стремлениями, соответственно, разное наше и

                место, разное и наше распределение. Тебе я желаю все самые крутые тачки

                чтоб были у тебя, и все самые лучше самки, если мало идей, обращайся ко мне,
                 
                я тебе на каждую твою идею предложу сотню триллионов, как всё делать. Ну а я всё,
                 
                я иду как глубокий старец,узревший вечное, прикоснувшийся к Божественному, сам
                 
                стал богоподобен и устремлен в это бесконечное, и который в умиротворении,
                        
                покое, гармонии, благодати, в этом сокровенном блаженстве пребывает,
                  
                вовлеченный во всё и во вся, понимаешь, вот и всё, в этом наша разница. 
                  
                Так что я иду любоваться мирозданием, а ты идёшь преисполняться в ГРАНЯХ 
                
                каких-то, вот и вся разница, понимаешь, ты не зришь это вечное бесконечное,
                
                оно тебе не нужно. Ну зато ты, так сказать, более активен, как вот этот дятел
                
                долбящий, или муравей, который очень активен в своей стезе, поэтому давай,
                
                наши пути здесь, конечно, имеют грани подобия, потому что всё едино, но я-то
                
                тебя прекрасно понимаю, а вот ты меня - вряд ли, потому что я как бы тебя в себе
                
                содержу, всю твою природу, она составляет одну маленькую там песчиночку, от того
                
                что есть во мне, вот и всё, поэтому давай, ступай, езжай, а я пошел наслаждаться
                
                прекрасным осенним закатом на берегу теплой южной реки. Всё, ступай, и я пойду.""");
    }
}
