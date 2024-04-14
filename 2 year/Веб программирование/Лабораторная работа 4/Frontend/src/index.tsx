import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './utils/reportWebVitals';
import LoginPage from "./views/LoginPage";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Dots from "./views/Dots";
import ErrorPage from "./views/ErrorPage";
import {Toaster} from "react-hot-toast";
import { Provider } from 'react-redux'
import { AuthorizationStore } from './redux/authorizationStore';

const router = createBrowserRouter(
    [
        {
            path: "/",
            element: <LoginPage/>
        },
        {
            path: "/dots",
            element: <Dots/>
        },
        {
            path: "/*",
            element: <ErrorPage/>
        }
    ]
)

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <React.StrictMode>
      <Provider store={AuthorizationStore}>
          <Toaster
              position="top-right"
              reverseOrder={false}
          />
          <RouterProvider router={router}/>
      </Provider>
  </React.StrictMode>
);

// Report website speed
reportWebVitals();
