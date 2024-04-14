import {Action, configureStore, ThunkAction} from "@reduxjs/toolkit";
import {loginReducer} from "./login";
import {passwordReducer} from "./password";

export const AuthorizationStore = configureStore({
    reducer: {
        login: loginReducer,
        password: passwordReducer
    },
});

export type AppDispatch = typeof AuthorizationStore.dispatch;
export type RootState = ReturnType<typeof AuthorizationStore.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<
    ReturnType,
    RootState,
    unknown,
    Action<string>
>