// Or from '@reduxjs/toolkit/query' if not using the auto-generated hooks


// initialize an empty api service that we'll inject endpoints into later as needed
import {BaseQueryApi, createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react';
import {RootState} from "./store";
import {useAuth} from "react-oidc-context";
import {User} from "oidc-client-ts";
import {appConfig} from "../index";

function getUser() {
    const oidcStorage = localStorage.getItem(appConfig.localStoragePath);
    if (!oidcStorage) {
        return null;
    }

    return User.fromStorageString(oidcStorage);
}


const baseQuery = fetchBaseQuery({
    baseUrl: 'http://localhost:8080/',
    prepareHeaders: (headers, {}) => {
        const token = getUser()?.access_token
        if (token) {
            headers.set('Authorization', `Bearer ${token}`);
        }
        return headers;
    },

});

export const api = createApi({
    baseQuery: baseQuery,
    endpoints: () => ({}),
})