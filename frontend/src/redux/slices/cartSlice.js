import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  cart: null,
  loading: false,
  error: null,
};

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    cartStart: (state) => {
      state.loading = true;
      state.error = null;
    },
    cartSuccess: (state, action) => {
      state.loading = false;
      state.cart = action.payload;
    },
    cartFailure: (state, action) => {
      state.loading = false;
      state.error = action.payload;
    },
    clearCartState: (state) => {
      state.cart = null;
      state.loading = false;
      state.error = null;
    }
  },
});

export const { cartStart, cartSuccess, cartFailure, clearCartState } = cartSlice.actions;

export default cartSlice.reducer;
