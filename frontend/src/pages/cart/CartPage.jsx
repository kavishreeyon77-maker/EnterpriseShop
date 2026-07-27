import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { Trash2, Plus, Minus, ArrowRight, Loader2, Tag } from 'lucide-react';
import api from '../../services/api';
import { cartStart, cartSuccess, cartFailure } from '../../redux/slices/cartSlice';
import toast from 'react-hot-toast';

const CartPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { cart, loading } = useSelector((state) => state.cart);
  const { isAuthenticated } = useSelector((state) => state.auth);
  
  const [couponCode, setCouponCode] = useState('');
  const [applyingCoupon, setApplyingCoupon] = useState(false);

  useEffect(() => {
    if (isAuthenticated) {
      fetchCart();
    }
  }, [isAuthenticated]);

  const fetchCart = async () => {
    dispatch(cartStart());
    try {
      const response = await api.get('/cart');
      dispatch(cartSuccess(response.data.data));
    } catch (error) {
      dispatch(cartFailure(error.response?.data?.message || 'Failed to fetch cart'));
    }
  };

  const updateQuantity = async (sku, currentQty, change) => {
    const newQty = currentQty + change;
    if (newQty < 1) return;
    
    try {
      const response = await api.put(`/cart/items/${sku}`, { quantity: newQty });
      dispatch(cartSuccess(response.data.data));
    } catch (error) {
      toast.error(error.response?.data?.message || 'Could not update quantity');
    }
  };

  const removeItem = async (sku) => {
    try {
      const response = await api.delete(`/cart/items/${sku}`);
      dispatch(cartSuccess(response.data.data));
      toast.success('Item removed');
    } catch (error) {
      toast.error('Could not remove item');
    }
  };

  const applyCoupon = async (e) => {
    e.preventDefault();
    if (!couponCode) return;
    setApplyingCoupon(true);
    try {
      const response = await api.post('/cart/coupon/apply', { couponCode });
      dispatch(cartSuccess(response.data.data));
      toast.success('Coupon applied!');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Invalid coupon');
    } finally {
      setApplyingCoupon(false);
    }
  };
  
  const removeCoupon = async () => {
    setApplyingCoupon(true);
    try {
      const response = await api.delete('/cart/coupon');
      dispatch(cartSuccess(response.data.data));
      setCouponCode('');
      toast.success('Coupon removed');
    } catch (error) {
      toast.error('Could not remove coupon');
    } finally {
      setApplyingCoupon(false);
    }
  };

  const handleCheckout = () => {
    if (!isAuthenticated) {
      navigate('/login', { state: { from: { pathname: '/checkout' } } });
    } else {
      navigate('/checkout');
    }
  };

  if (!isAuthenticated && !cart?.items?.length) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 text-center">
        <h2 className="text-3xl font-bold mb-4">Your Cart is Empty</h2>
        <p className="text-muted-foreground mb-8">Sign in to sync your cart or start shopping now.</p>
        <Link to="/login" className="gradient-btn px-6 py-3 rounded-lg mr-4">Sign In</Link>
        <Link to="/products" className="bg-secondary text-secondary-foreground px-6 py-3 rounded-lg hover:bg-secondary/80 transition-colors">Browse Products</Link>
      </div>
    );
  }

  if (!cart?.items?.length) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 text-center">
        <h2 className="text-3xl font-bold mb-4">Your Cart is Empty</h2>
        <p className="text-muted-foreground mb-8">Looks like you haven't added anything yet.</p>
        <Link to="/products" className="gradient-btn px-6 py-3 rounded-lg">Start Shopping</Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <h1 className="text-3xl font-extrabold text-foreground mb-8">Shopping Cart</h1>
      
      <div className="flex flex-col lg:flex-row gap-12">
        {/* Cart Items */}
        <div className="lg:w-2/3 space-y-6">
          {cart.items.map((item) => (
            <div key={item.sku} className="glass-card p-4 sm:p-6 flex flex-col sm:flex-row gap-6 items-start sm:items-center">
              <div className="h-24 w-24 bg-secondary rounded-lg flex items-center justify-center flex-shrink-0 overflow-hidden">
                {item.image ? (
                  <img src={item.image} alt={item.productName} className="w-full h-full object-cover" onError={(e) => { e.target.style.display='none'; }} />
                ) : (
                  <span className="text-xs text-muted-foreground text-center px-1">No Image</span>
                )}
              </div>
              <div className="flex-1">
                <div className="flex justify-between items-start">
                  <div>
                    <h3 className="font-bold text-lg text-foreground">{item.productName}</h3>
                    <p className="text-sm text-muted-foreground">SKU: {item.sku}</p>
                  </div>
                  <p className="font-bold text-lg">${item.price.toFixed(2)}</p>
                </div>
                
                <div className="mt-4 flex items-center justify-between">
                  <div className="flex items-center border border-border rounded-lg bg-background">
                    <button 
                      onClick={() => updateQuantity(item.sku, item.quantity, -1)}
                      className="p-2 text-muted-foreground hover:text-foreground transition-colors disabled:opacity-50"
                      disabled={item.quantity <= 1}
                    >
                      <Minus size={16} />
                    </button>
                    <span className="w-12 text-center font-medium">{item.quantity}</span>
                    <button 
                      onClick={() => updateQuantity(item.sku, item.quantity, 1)}
                      className="p-2 text-muted-foreground hover:text-foreground transition-colors disabled:opacity-50"
                      disabled={item.quantity >= 10}
                    >
                      <Plus size={16} />
                    </button>
                  </div>
                  
                  <button 
                    onClick={() => removeItem(item.sku)}
                    className="text-destructive hover:text-red-700 p-2 transition-colors flex items-center gap-1 text-sm font-medium"
                  >
                    <Trash2 size={16} />
                    <span className="hidden sm:inline">Remove</span>
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Order Summary */}
        <div className="lg:w-1/3">
          <div className="glass-card p-6 sticky top-24">
            <h2 className="text-xl font-bold mb-6">Order Summary</h2>
            
            <div className="space-y-4 mb-6">
              <div className="flex justify-between text-muted-foreground">
                <span>Subtotal</span>
                <span>${cart.subTotal?.toFixed(2)}</span>
              </div>
              
              {cart.discount > 0 && (
                <div className="flex justify-between text-emerald-600 font-medium">
                  <span>Discount {cart.couponCode ? `(${cart.couponCode})` : ''}</span>
                  <span>-${cart.discount?.toFixed(2)}</span>
                </div>
              )}
              
              <div className="flex justify-between text-muted-foreground">
                <span>Tax</span>
                <span>${cart.tax?.toFixed(2)}</span>
              </div>
              
              <div className="flex justify-between text-muted-foreground">
                <span>Shipping</span>
                <span>${(cart.shippingCharge ?? cart.shipping ?? 0)?.toFixed(2)}</span>
              </div>
              
              <div className="border-t border-border pt-4 mt-4">
                <div className="flex justify-between items-center text-lg font-bold">
                  <span>Grand Total</span>
                  <span className="text-primary">${cart.grandTotal?.toFixed(2)}</span>
                </div>
              </div>
            </div>

            {/* Coupon Section */}
            <div className="mb-6">
              {cart.couponCode ? (
                <div className="flex items-center justify-between bg-emerald-50 dark:bg-emerald-900/20 p-3 rounded-lg border border-emerald-200 dark:border-emerald-800">
                  <div className="flex items-center gap-2 text-emerald-700 dark:text-emerald-400">
                    <Tag size={16} />
                    <span className="font-medium text-sm">{cart.couponCode} applied</span>
                  </div>
                  <button 
                    onClick={removeCoupon}
                    disabled={applyingCoupon}
                    className="text-sm font-medium text-destructive hover:underline"
                  >
                    Remove
                  </button>
                </div>
              ) : (
                <form onSubmit={applyCoupon} className="flex gap-2">
                  <input
                    type="text"
                    value={couponCode}
                    onChange={(e) => setCouponCode(e.target.value.toUpperCase())}
                    placeholder="Coupon code"
                    className="flex-1 px-3 py-2 border border-border rounded-lg bg-background text-sm focus:ring-2 focus:ring-primary focus:border-primary"
                  />
                  <button 
                    type="submit" 
                    disabled={!couponCode || applyingCoupon}
                    className="px-4 py-2 bg-secondary text-secondary-foreground rounded-lg text-sm font-medium hover:bg-secondary/80 disabled:opacity-50 transition-colors"
                  >
                    {applyingCoupon ? <Loader2 size={16} className="animate-spin" /> : 'Apply'}
                  </button>
                </form>
              )}
            </div>

            <button 
              onClick={handleCheckout}
              disabled={loading || applyingCoupon}
              className="w-full flex items-center justify-center gap-2 gradient-btn py-3 rounded-lg font-bold disabled:opacity-70 disabled:cursor-not-allowed"
            >
              <span>Proceed to Checkout</span>
              <ArrowRight size={20} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartPage;
