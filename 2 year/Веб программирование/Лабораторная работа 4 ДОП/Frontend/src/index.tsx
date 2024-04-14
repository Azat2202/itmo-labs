import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './utils/reportWebVitals';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Dots from "./views/Dots";
import ErrorPage from "./views/ErrorPage";
import {Toaster} from "react-hot-toast";
import { Provider } from 'react-redux'
import { AuthorizationStore } from './redux/authorizationStore';
import PrivateRoute from "./containers/PrivatePath";
import {AuthProvider} from "react-oidc-context";
import LoginPage from "./views/LoginPage";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const oidcConfig = {
    authority: "http://localhost:8180/realms/Lab4",
    client_id: "ReactClient",
    redirect_uri: "http://localhost:3000/dots",
    onSigninCallback: () => {
        window.history.pushState({}, "", "/dots");
    }
}

root.render(
    <AuthProvider {...oidcConfig}>
      <Provider store={AuthorizationStore}>
          <Toaster
              position="top-right"
              reverseOrder={false}
          />
          <BrowserRouter>
              <Routes>
                  <Route path={"/"} element={<LoginPage/>}/>
                  <Route path={"/dots"} element={<PrivateRoute><Dots/></PrivateRoute>}/>
                  <Route path={"/*"} element={<ErrorPage/>}/>
              </Routes>
          </BrowserRouter>
      </Provider>
    </AuthProvider>
);

// Report website speed
reportWebVitals();
