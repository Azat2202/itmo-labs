import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from './reportWebVitals';
import { App } from "./app";
import { api } from "./store/baseApi";
import { ApiProvider } from '@reduxjs/toolkit/query/react';
import { BrowserRouter } from "react-router-dom";
import './index.css';
import { Provider } from "react-redux";
import store from "./store/store";
import {Toaster} from "react-hot-toast";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <React.StrictMode>
  <Toaster />
    <ApiProvider api={ api }>
      <Provider store={ store }>
        <BrowserRouter>
          <App/>
        </BrowserRouter>
      </Provider>
    </ApiProvider>
  </React.StrictMode>
);

reportWebVitals();
