class Clock {
    constructor() {
        this.setClock();
        setInterval(this.setClock, 10000);
    }

    setClock(){
        const deg = 6;
        const day = new Date();
        const hh = day.getHours() * 30;
        const mm = day.getMinutes() * deg;
        const ss = day.getSeconds() * deg;

        document.querySelector(".hour").style.transform = `rotateZ(${hh + mm / 12}deg)`;
        document.querySelector(".min").style.transform = `rotateZ(${mm}deg)`;
        document.querySelector(".sec").style.transform = `rotateZ(${ss}deg)`;
    }
}