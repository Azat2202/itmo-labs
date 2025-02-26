import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { PageStudyGroupResponse } from "./types.generated";



const initialState: PageStudyGroupResponse = {
};

const collectionSlice = createSlice({
  name: 'collection',
  initialState,
  reducers: {
    setStudyGroups: (state, action: PayloadAction<PageStudyGroupResponse>) => {
      state = action.payload;
    }
  },
});

export const { setStudyGroups } = collectionSlice.actions;
export default collectionSlice.reducer;