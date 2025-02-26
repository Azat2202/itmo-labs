import React from "react";
import { Route, Routes } from "react-router-dom";
import { LoginPage } from "./views/LoginPage";
import PrivateRoute from "./containers/PrivateRoute";
import { MainPage } from "./views/MainPage";

export const App: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={ <LoginPage/> }/>
      <Route path="/main" element={ <PrivateRoute><MainPage/></PrivateRoute> }/>
      <Route path="*" element={ <LoginPage/> }/>
    </Routes>
  );
};