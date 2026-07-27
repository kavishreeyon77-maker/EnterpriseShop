import { useSelector, useDispatch } from 'react-redux';
import { Link } from 'react-router-dom';
import { Heart, ShoppingCart, Trash2, Package, ArrowRight } from 'lucide-react';
import { toggleWishlistItem } from '../../redux/slices/wishlistSlice';
import { cartStart, cartSuccess, cartFailure } from '../../redux/slices/cartSlice';
import api from '../../services/api';
import toast from 'react-hot-toast';
import { useState } from 'react';

const WishlistPage = () => {
  const { items } = useSelector(state => state.wishlist);
  const { isAuthenticated } = useSelector(state => state.auth);
  const dispatch = useDispatch();
  const [addingToCart, setAddingToCart] = useState({});

  const handleRemove = (product) => {
    dispatch(toggleWishlistItem(product));
    toast('Removed from wishlist', { icon: '💔' });
  };

  const handleAddToCart = async (item) => {
    if (!isAuthenticated) {
      toast.error('Please login to add to cart');
      return;
    }
    setAddingToCart(prev => ({ ...prev, [item.sku]: true }));
    dispatch(cartStart());
    try {
      const response = await api.post('/cart/items', { sku: item.sku, quantity: 1 });
      dispatch(cartSuccess(response.data.data));
      toast.success(`${item.name} added to cart! 🛒`);
    } catch (error) {
      dispatch(cartFailure('Failed'));
      toast.error(error.response?.data?.message || 'Could not add to cart');
    } finally {
      setAddingToCart(prev => ({ ...prev, [item.sku]: false }));
    }
  };

  if (items.length === 0) {
    return (
      <div className="min-h-screen bg-slate-50 dark:bg-slate-950 flex flex-col items-center justify-center px-4 py-20">
        <div className="text-center max-w-md">
          <div className="w-28 h-28 bg-red-50 dark:bg-red-900/20 rounded-full flex items-center justify-center mx-auto mb-8">
            <Heart size={56} className="text-red-400" strokeWidth={1.5} />
          </div>
          <h1 className="text-3xl font-black text-slate-900 dark:text-white mb-3">Your Wishlist is Empty</h1>
          <p className="text-slate-500 mb-8">Save your favourite products here by clicking the heart icon on any product card.</p>
          <Link to="/products" className="gradient-btn px-8 py-3.5 rounded-xl font-bold inline-flex items-center gap-2">
            Explore Products <ArrowRight size={18} />
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-slate-50 dark:bg-slate-950 min-h-screen pb-20">
      {/* Header */}
      <div className="bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 py-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex justify-between items-end">
          <div>
            <h1 className="text-4xl font-black text-slate-900 dark:text-white mb-1">My Wishlist</h1>
            <p className="text-slate-500">{items.length} saved {items.length === 1 ? 'item' : 'items'}</p>
          </div>
          <Link to="/products" className="hidden sm:flex items-center gap-2 text-primary font-bold hover:gap-3 transition-all">
            Continue Shopping <ArrowRight size={18} />
          </Link>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {items.map((item) => (
            <div key={item.sku} className="glass-card group overflow-hidden flex flex-col hover:-translate-y-1 transition-transform duration-300">
              {/* Image */}
              <div className="relative h-52 bg-slate-50 dark:bg-slate-900/50 overflow-hidden flex items-center justify-center p-4">
                {item.image ? (
                  <img
                    src={item.image}
                    alt={item.name}
                    className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
                    onError={(e) => { e.target.style.display = 'none'; }}
                  />
                ) : (
                  <div className="flex items-center justify-center text-slate-300">
                    <Package size={48} strokeWidth={1} />
                  </div>
                )}

                {/* Remove from wishlist */}
                <button
                  onClick={() => handleRemove(item)}
                  className="absolute top-3 right-3 w-9 h-9 bg-white dark:bg-slate-800 rounded-full flex items-center justify-center text-red-500 shadow-md hover:bg-red-500 hover:text-white transition-colors"
                >
                  <Trash2 size={16} />
                </button>

                {item.category && (
                  <span className="absolute top-3 left-3 bg-white/90 dark:bg-slate-800/90 text-xs font-bold px-2.5 py-1 rounded-full text-primary shadow-sm">
                    {item.category}
                  </span>
                )}
              </div>

              {/* Details */}
              <div className="p-5 flex flex-col flex-1 border-t border-slate-100 dark:border-slate-800/50 bg-white dark:bg-slate-900/80">
                <h3 className="font-bold text-slate-900 dark:text-white mb-1 line-clamp-2 group-hover:text-primary transition-colors">
                  {item.name}
                </h3>
                <p className="text-2xl font-black text-primary mt-auto mb-4">
                  ${parseFloat(item.price).toFixed(2)}
                </p>
                <button
                  onClick={() => handleAddToCart(item)}
                  disabled={addingToCart[item.sku]}
                  className={`w-full py-2.5 rounded-xl font-bold flex items-center justify-center gap-2 transition-all ${
                    addingToCart[item.sku]
                      ? 'bg-slate-100 dark:bg-slate-800 text-primary cursor-wait'
                      : 'gradient-btn shadow-[0_4px_12px_rgba(37,99,235,0.2)]'
                  }`}
                >
                  <ShoppingCart size={18} className={addingToCart[item.sku] ? 'animate-bounce' : ''} />
                  {addingToCart[item.sku] ? 'Adding...' : 'Add to Cart'}
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default WishlistPage;
