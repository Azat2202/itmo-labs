// Or from '@reduxjs/toolkit/query' if not using the auto-generated hooks


// initialize an empty api service that we'll inject endpoints into later as needed
import { BaseQueryApi, createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { RootState } from "./store";

const baseQuery = fetchBaseQuery({
  baseUrl: 'http://localhost:8080/',
  prepareHeaders: (headers, { getState }) => {
    const token = (getState() as RootState).auth.token; // Assuming you store the token in your auth slice
    if (token) {
      headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  },

});

// const baseQueryWithErrorHandling = async (args, api, extraOptions)=> {
//   const result = await baseQuery(args, api, extraOptions);
//
//   if (result.error) {
//     // Handle the error globally
//     console.error('Global error:', result.error);
//   }
//
//   return result;
// };

export const api = createApi({
  baseQuery: baseQuery,
  endpoints: () => ({}),

})