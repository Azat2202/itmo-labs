import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./authorizationStore";

export interface Login{
    value: string
}

const initialState: Login = {
    value: ""
};

export const loginSlice = createSlice({
    name: 'login',
    initialState,
    reducers: {
        setLogin: (state, action: PayloadAction<string>) => {
            state.value = action.payload
        },
        resetLogin: (state) => {
            state.value = ""
        }
    }
})


export const {setLogin, resetLogin} = loginSlice.actions;

export const getLogin = (state: RootState) => state.login.value;

export const loginReducer = loginSlice.reducer;
