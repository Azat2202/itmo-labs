import {createSlice, PayloadAction} from '@reduxjs/toolkit';

interface RoomSlice {
    roomId: number;
}

const initialState: RoomSlice = {
    roomId: -1
};

const roomSlice = createSlice({
    name: 'room',
    initialState,
    reducers: {
        setRoomId(state, action: PayloadAction<number>) {
            state.roomId = action.payload
        },
    },
});

export const {setRoomId} = roomSlice.actions;
export default roomSlice.reducer;