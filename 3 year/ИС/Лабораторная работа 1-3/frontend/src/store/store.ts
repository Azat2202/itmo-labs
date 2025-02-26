import { configureStore } from '@reduxjs/toolkit';
import authReducer from './authSlice';
import collectionReducer from './studyGroupSlice'
import { api } from "./baseApi";

const store = configureStore({
  reducer: {
    auth: authReducer,
    [api.reducerPath]: api.reducer,
    collection: collectionReducer
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({}).concat(api.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
