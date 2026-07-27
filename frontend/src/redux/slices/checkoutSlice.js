import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  activeStep: 0,
  shippingAddressId: null,
  billingAddressId: null,
  shippingMethod: null,
  paymentMethod: null,
  checkoutSession: null,
  order: null,
};

const checkoutSlice = createSlice({
  name: 'checkout',
  initialState,
  reducers: {
    setActiveStep: (state, action) => {
      state.activeStep = action.payload;
    },
    setShippingAddress: (state, action) => {
      state.shippingAddressId = action.payload;
    },
    setBillingAddress: (state, action) => {
      state.billingAddressId = action.payload;
    },
    setShippingMethod: (state, action) => {
      state.shippingMethod = action.payload;
    },
    setPaymentMethod: (state, action) => {
      state.paymentMethod = action.payload;
    },
    setCheckoutSession: (state, action) => {
      state.checkoutSession = action.payload;
    },
    setOrder: (state, action) => {
      state.order = action.payload;
    },
    resetCheckout: (state) => {
      return initialState;
    }
  },
});

export const {
  setActiveStep,
  setShippingAddress,
  setBillingAddress,
  setShippingMethod,
  setPaymentMethod,
  setCheckoutSession,
  setOrder,
  resetCheckout
} = checkoutSlice.actions;

export default checkoutSlice.reducer;
