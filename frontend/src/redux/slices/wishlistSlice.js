import { createSlice } from '@reduxjs/toolkit';

const loadWishlist = () => {
  try { return JSON.parse(localStorage.getItem('wishlist') || '[]'); } catch { return []; }
};

const wishlistSlice = createSlice({
  name: 'wishlist',
  initialState: {
    items: loadWishlist(),
  },
  reducers: {
    toggleWishlistItem: (state, action) => {
      const product = action.payload;
      const index = state.items.findIndex(p => p.sku === product.sku);
      if (index >= 0) {
        state.items.splice(index, 1);
      } else {
        state.items.push({
          sku: product.sku,
          name: product.name,
          price: product.discountPrice || product.price,
          image: product.images?.[0] || null,
          category: product.category,
        });
      }
      localStorage.setItem('wishlist', JSON.stringify(state.items));
    },
    clearWishlist: (state) => {
      state.items = [];
      localStorage.removeItem('wishlist');
    },
  },
});

export const { toggleWishlistItem, clearWishlist } = wishlistSlice.actions;
export default wishlistSlice.reducer;
