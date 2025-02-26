import React from "react";
import {Route, Routes} from "react-router-dom";
import {LandingPage} from "./views/LandingPage";
import {ErrorPage} from "./views/ErrorPage";
import PrivateRoute from "./containers/PrivateRoute";
import {MainPage} from "./views/MainPage";
import {RoomPage} from "./views/Room";

export const App: React.FC = () => {
    return (
        <Routes>
            <Route path={"/"} element={<LandingPage/>}/>
            <Route path={"/main/*"} element={<PrivateRoute><MainPage/></PrivateRoute>}/>
            <Route path={"/room/:roomId"} element={<PrivateRoute><RoomPage/></PrivateRoute>}/>
            <Route path={"/*"} element={<ErrorPage/>}/>
        </Routes>
    );
};