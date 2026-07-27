import { X, Trash2, Plus, Minus, ArrowRight, ShoppingBag, ShieldCheck } from 'lucide-react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, Link } from 'react-router-dom';
import { cartStart, cartSuccess, cartFailure } from '../../redux/slices/cartSlice';
import api from '../../services/api';
import toast from 'react-hot-toast';

const CartDrawer = ({ isOpen, onClose }) => {
  const { cart, loading } = useSelector((state) => state.cart);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const updateQuantity = async (sku, newQuantity) => {
    if (newQuantity < 1) return;
    dispatch(cartStart());
    try {
      const response = await api.put(`/cart/items/${sku}`, { quantity: newQuantity });
      dispatch(cartSuccess(response.data.data));
    } catch (error) {
      dispatch(cartFailure(error.response?.data?.message || 'Update failed'));
      toast.error('Failed to update quantity');
    }
  };

  const removeItem = async (sku) => {
    dispatch(cartStart());
    try {
      const response = await api.delete(`/cart/items/${sku}`);
      dispatch(cartSuccess(response.data.data));
      toast.success('Item removed');
    } catch (error) {
      dispatch(cartFailure(error.response?.data?.message || 'Remove failed'));
      toast.error('Failed to remove item');
    }
  };

  const handleCheckout = () => {
    onClose();
    navigate('/checkout');
  };

  if (!isOpen) return null;

  return (
    <>
      {/* Backdrop */}
      <div 
        className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 animate-in fade-in transition-opacity"
        onClick={onClose}
      />
      
      {/* Drawer */}
      <div className="fixed inset-y-0 right-0 w-full sm:w-[400px] bg-white dark:bg-slate-900 shadow-2xl z-50 flex flex-col slide-in-right border-l border-slate-200 dark:border-slate-800">
        
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-slate-100 dark:border-slate-800">
          <div className="flex items-center gap-3">
            <ShoppingBag size={24} className="text-primary" />
            <h2 className="text-xl font-bold">Your Cart</h2>
            <span className="bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300 text-xs font-bold px-2.5 py-1 rounded-full">
              {cart?.items?.length || 0}
            </span>
          </div>
          <button 
            onClick={onClose}
            className="p-2 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 text-slate-400 hover:text-slate-900 dark:hover:text-white transition-colors"
          >
            <X size={20} />
          </button>
        </div>

        {/* Cart Items */}
        <div className="flex-1 overflow-y-auto p-6 space-y-6">
          {!cart?.items || cart.items.length === 0 ? (
            <div className="h-full flex flex-col items-center justify-center text-center opacity-50 space-y-4">
              <ShoppingBag size={64} className="mb-4" />
              <p className="text-lg font-medium">Your cart is empty</p>
              <p className="text-sm">Looks like you haven't added anything yet.</p>
              <button 
                onClick={onClose}
                className="mt-6 px-6 py-2.5 bg-slate-900 dark:bg-white text-white dark:text-slate-900 font-medium rounded-full hover:scale-105 transition-transform"
              >
                Start Shopping
              </button>
            </div>
          ) : (
            cart.items.map((item) => (
              <div key={item.sku} className="flex gap-4 group">
                {/* Image */}
                <div className="w-24 h-24 rounded-xl bg-slate-50 dark:bg-slate-800 border border-slate-100 dark:border-slate-700 overflow-hidden flex-shrink-0 relative">
                  {item.image ? (
                    <img src={item.image} alt={item.productName} className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-500" />
                  ) : (
                    <div className="w-full h-full flex items-center justify-center text-slate-400">
                      <ShoppingBag size={24} />
                    </div>
                  )}
                </div>

                {/* Details */}
                <div className="flex-1 flex flex-col justify-between">
                  <div className="flex justify-between items-start gap-2">
                    <h3 className="font-semibold text-sm leading-snug line-clamp-2">
                      <Link to={`/products/${item.sku}`} onClick={onClose} className="hover:text-primary transition-colors">
                        {item.productName}
                      </Link>
                    </h3>
                    <button 
                      onClick={() => removeItem(item.sku)}
                      className="text-slate-400 hover:text-red-500 transition-colors p-1"
                      disabled={loading}
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                  
                  <div className="flex items-center justify-between mt-auto">
                    <div className="font-bold text-primary">
                      ${parseFloat(item.price).toFixed(2)}
                    </div>
                    
                    {/* Quantity Controls */}
                    <div className="flex items-center bg-slate-100 dark:bg-slate-800 rounded-lg p-1 border border-slate-200 dark:border-slate-700">
                      <button 
                        onClick={() => updateQuantity(item.sku, item.quantity - 1)}
                        disabled={loading || item.quantity <= 1}
                        className="p-1.5 rounded-md hover:bg-white dark:hover:bg-slate-700 disabled:opacity-50 transition-colors"
                      >
                        <Minus size={14} />
                      </button>
                      <span className="w-8 text-center text-sm font-semibold">{item.quantity}</span>
                      <button 
                        onClick={() => updateQuantity(item.sku, item.quantity + 1)}
                        disabled={loading}
                        className="p-1.5 rounded-md hover:bg-white dark:hover:bg-slate-700 transition-colors"
                      >
                        <Plus size={14} />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>

        {/* Footer / Summary */}
        {cart?.items && cart.items.length > 0 && (
          <div className="p-6 border-t border-slate-100 dark:border-slate-800 bg-slate-50 dark:bg-slate-900/50">
            <div className="space-y-3 mb-6 text-sm">
              <div className="flex justify-between text-slate-500 dark:text-slate-400">
                <span>Subtotal</span>
                <span className="font-medium text-slate-900 dark:text-white">${parseFloat(cart.subTotal || 0).toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-slate-500 dark:text-slate-400">
                <span>Taxes & Shipping</span>
                <span className="text-xs">Calculated at checkout</span>
              </div>
              {cart.discount > 0 && (
                <div className="flex justify-between text-emerald-600">
                  <span>Discount</span>
                  <span className="font-medium">-${parseFloat(cart.discount).toFixed(2)}</span>
                </div>
              )}
            </div>
            <div className="flex justify-between items-center mb-6">
              <span className="text-lg font-bold">Estimated Total</span>
              <span className="text-2xl font-black text-primary">
                ${parseFloat(cart.grandTotal || cart.subTotal || 0).toFixed(2)}
              </span>
            </div>
            
            <button 
              onClick={handleCheckout}
              className="w-full gradient-btn py-4 rounded-xl font-bold flex items-center justify-center gap-2 text-lg shadow-[0_8px_16px_rgba(37,99,235,0.2)]"
            >
              Secure Checkout <ArrowRight size={20} />
            </button>
            <p className="text-center text-xs text-slate-500 mt-4 flex items-center justify-center gap-1">
              <ShieldCheck size={14} /> 100% secure encrypted checkout
            </p>
          </div>
        )}
      </div>
    </>
  );
};

export default CartDrawer;
