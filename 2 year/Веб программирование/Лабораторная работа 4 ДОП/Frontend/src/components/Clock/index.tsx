import React, {useEffect, useState} from "react";
import "./clock.css"

function Clock({updateInterval}: {updateInterval: number}){
        const [value, setValue] = useState(new Date());
    useEffect(() => {
        const interval = setInterval(() => setValue(new Date()), 1000);
        return () => {
            clearInterval(interval);
        };
    }, []);

    const deg = 6;
    const day = value;
    const hh = day.getHours() * 30;
    const mm = day.getMinutes() * deg;
    const ss = day.getSeconds() * deg;

    return (
        <div className="clock h-96 w-96 ">
            <div className="hour h-36 w-36" style={{transform: `rotateZ(${hh + mm / 12}deg)`}}/>
            <div className="min h-56 w-56" style={{transform: `rotateZ(${mm}deg)`}}/>
            <div className="sec h-64 w-64" style={{transform: `rotateZ(${ss}deg)`}}/>
        </div>
    )
}

export default Clock;
