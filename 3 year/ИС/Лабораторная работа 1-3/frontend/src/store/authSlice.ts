import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface AuthState {
  login: string;
  password: string;
  token: string | null;
}

const initialState: AuthState = {
  login: "",
  password: "",
  token: null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setLogin(state, action: PayloadAction<string>) {
      state.login = action.payload;
    },
    setPassword(state, action: PayloadAction<string>) {
      state.password = action.payload;
    },
    setToken(state, action: PayloadAction<string>) {
      state.token = action.payload;
    },
    clearToken(state) {
      state.token = null;
    }
  },
});

export const { setToken, clearToken, setLogin } = authSlice.actions;
export default authSlice.reducer;