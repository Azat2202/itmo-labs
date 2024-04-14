import React, {ReactNode, useState} from "react";

export interface Coordinates {
    x: number,
    y: string | number,
    r: number,
    status?: boolean,
    scriptTime?: number,
    time?: string,
    ownerLogin?: string
}


export interface CoordinatesStore{
    getX: number,
    getY: string | number,
    getR: number,
    getDots: Array<Coordinates>,
    setX: (x: number) => void,
    setY: (y: string) => void,
    setR: (r: number) => void,
    addDot: (dot: Coordinates) => void,
    setDots: (dots: Array<Coordinates>) => void
}
export const DotsFormContext = React.createContext<CoordinatesStore | null>(null)

export const CoordinatesProvider = ({children}: {children: ReactNode}) => {
    const [getCoordinates, setCoordinates] = useState<Coordinates>({
        x: -2,
        y: "-2",
        r: 1
    });

    const [getDots, setDots] = useState<Array<Coordinates>>([])

    const updateX = (newX: number) => {
        setCoordinates((prevCoordinates) => ({
            ...prevCoordinates,
            x: newX,
        }));
    };

    const updateY = (newY: string) => {
        setCoordinates((prevCoordinates) => ({
            ...prevCoordinates,
            y: newY,
        }));
    };

    const updateR = (newR: number) => {
        setCoordinates((prevCoordinates) => ({
            ...prevCoordinates,
            r: newR,
        }));
    };

    const addDot = (dot: Coordinates) => {
        setDots((prevState) => [...prevState, dot]);
    };

    const store = {
        getX: getCoordinates.x,
        getY: getCoordinates.y,
        getR: getCoordinates.r,
        getDots: getDots,
        setDots: setDots,
        setX: updateX,
        setY: updateY,
        setR: updateR,
        addDot: addDot
    }

    return (
        <DotsFormContext.Provider value={store}>
            {children}
        </DotsFormContext.Provider>
    )
}