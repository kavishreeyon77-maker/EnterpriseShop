import { configureStore, combineReducers } from '@reduxjs/toolkit';
import { persistStore, persistReducer } from 'redux-persist';
// Custom storage for redux-persist to avoid Vite/CommonJS export issues
const storage = {
  getItem(key) {
    return Promise.resolve(window.localStorage.getItem(key));
  },
  setItem(key, value) {
    window.localStorage.setItem(key, value);
    return Promise.resolve();
  },
  removeItem(key) {
    window.localStorage.removeItem(key);
    return Promise.resolve();
  },
};

import authReducer from './slices/authSlice';
import cartReducer from './slices/cartSlice';
import checkoutReducer from './slices/checkoutSlice';
import themeReducer from './slices/themeSlice';
import wishlistReducer from './slices/wishlistSlice';

const rootReducer = combineReducers({
  auth: authReducer,
  cart: cartReducer,
  checkout: checkoutReducer,
  theme: themeReducer,
  wishlist: wishlistReducer,
});

const persistConfig = {
  key: 'cartcheckout-root',
  storage,
  whitelist: ['auth', 'cart', 'theme', 'wishlist'],
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['persist/PERSIST', 'persist/REHYDRATE', 'persist/REGISTER'],
      },
    }),
});

export const persistor = persistStore(store);
