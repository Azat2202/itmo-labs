import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./authorizationStore";

export interface Password {
    value: string
}

const initialState: Password = {
    value: ""
};


export const passwordSlice = createSlice({
    name: 'password',
    initialState,
    reducers: {
        setPassword: (state, action: PayloadAction<string>) => {
            state.value = action.payload;
        },
        resetPassword: (state) => {
            state.value = ""
        }
    }
})


export const {setPassword, resetPassword} = passwordSlice.actions;

export const getPassword = (state: RootState) => state.password.value;

export const passwordReducer = passwordSlice.reducer;
