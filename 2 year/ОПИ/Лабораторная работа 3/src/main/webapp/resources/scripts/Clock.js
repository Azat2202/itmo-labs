class Clock {
    constructor() {
        this.setClock();
        setInterval(this.setClock, 13000);
    }

    setClock() {
        let now = new Date();
        let sec = now.getSeconds(),
            min = now.getMinutes(),
            h = now.getHours(),
            mo = now.getMonth(),
            dy = now.getDate(),
            yr = now.getFullYear();

        let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"];
        let tags = ["d", "mon", "y", "h", "m", "s"],
            corr = [
                dy,
                months[mo],
                yr,
                h.toString().padStart(2,0),
                min.toString().padStart(2,0),
                sec.toString().padStart(2,0)];

        for (let i = 0; i < tags.length; i++)
            document.getElementById(tags[i]).innerText = corr[i];

    }
}