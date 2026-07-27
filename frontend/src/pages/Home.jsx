import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { 
  ArrowRight, TrendingUp, Package, Heart, Tag, 
  Clock, ShoppingBag, Truck, ShieldCheck, Zap
} from 'lucide-react';
import api from '../services/api';
import ProductCard from '../components/products/ProductCard';
import QuickViewModal from '../components/products/QuickViewModal';
import { cartStart, cartSuccess, cartFailure } from '../redux/slices/cartSlice';
import { toggleWishlistItem } from '../redux/slices/wishlistSlice';
import toast from 'react-hot-toast';

const DUMMY_PRODUCTS = [
  {
    id: '1', sku: 'LAP-001', name: 'Enterprise Laptop Pro X', price: 1299.99, discountPrice: 999.99,
    category: 'Electronics', active: true, stock: 15,
    images: ['https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&q=80'],
    description: 'High-performance laptop for enterprise use.'
  },
  {
    id: '2', sku: 'MOU-002', name: 'Ergonomic Wireless Mouse', price: 49.99, discountPrice: null,
    category: 'Accessories', active: true, stock: 50,
    images: ['https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400&q=80'],
    description: 'Comfortable wireless mouse for all-day use.'
  },
  {
    id: '3', sku: 'KEY-003', name: 'Mechanical Keyboard Blue', price: 129.50, discountPrice: 89.99,
    category: 'Accessories', active: true, stock: 30,
    images: ['https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400&q=80'],
    description: 'Tactile blue-switch mechanical keyboard.'
  },
  {
    id: '4', sku: 'MON-004', name: '4K UltraSharp Monitor 27"', price: 450.00, discountPrice: null,
    category: 'Electronics', active: true, stock: 8,
    images: ['https://images.unsplash.com/photo-1527443224154-c4a573d5e6b0?w=400&q=80'],
    description: 'Crystal-clear 4K display for professionals.'
  }
];

const Home = () => {
  const { user, isAuthenticated } = useSelector(state => state.auth);
  const { cart } = useSelector(state => state.cart);
  const wishlistItems = useSelector(state => state.wishlist?.items || []);
  const [featuredProducts, setFeaturedProducts] = useState([]);
  const [quickViewProduct, setQuickViewProduct] = useState(null);
  const [addingToCart, setAddingToCart] = useState({});
  const dispatch = useDispatch();

  useEffect(() => {
    fetchFeaturedProducts();
  }, []);

  const fetchFeaturedProducts = async () => {
    try {
      const response = await api.get('/products');
      let fetched = response?.data?.data;
      if (!fetched || fetched.length === 0) {
        fetched = DUMMY_PRODUCTS;
      }
      setFeaturedProducts(fetched.slice(0, 4));
    } catch {
      setFeaturedProducts(DUMMY_PRODUCTS);
    }
  };

  const addToCart = async (product) => {
    if (!isAuthenticated) {
      toast.error('Please login to add to cart');
      return;
    }
    setAddingToCart(prev => ({ ...prev, [product.sku]: true }));
    dispatch(cartStart());
    try {
      const response = await api.post('/cart/items', { sku: product.sku, quantity: 1 });
      dispatch(cartSuccess(response.data.data));
      toast.success(`${product.name} added to cart! 🛒`);
    } catch (error) {
      dispatch(cartFailure('Failed'));
      toast.error(error.response?.data?.message || 'Could not add to cart');
    } finally {
      setAddingToCart(prev => ({ ...prev, [product.sku]: false }));
    }
  };

  const handleToggleWishlist = (product) => {
    if (!isAuthenticated) {
      toast.error('Please login to save to wishlist');
      return;
    }
    const alreadyIn = wishlistItems.some(p => p.sku === product.sku);
    dispatch(toggleWishlistItem(product));
    if (alreadyIn) toast('Removed from wishlist', { icon: '💔' });
    else toast.success('Added to wishlist! ❤️');
  };

  const isWishlisted = (sku) => wishlistItems.some(p => p.sku === sku);

  const stats = [
    { title: 'Total Products', value: '2,500+', icon: Package, color: 'text-blue-500', bg: 'bg-blue-500/10', trend: '+12% this week' },
    { title: 'In Cart', value: cart?.items?.length || 0, icon: ShoppingBag, color: 'text-emerald-500', bg: 'bg-emerald-500/10' },
    { title: 'Wishlist', value: wishlistItems.length, icon: Heart, color: 'text-red-500', bg: 'bg-red-500/10' },
    { title: 'Active Offers', value: '15', icon: Tag, color: 'text-amber-500', bg: 'bg-amber-500/10', trend: 'Ends in 2 days' },
  ];

  return (
    <div className="bg-slate-50 dark:bg-slate-950 min-h-screen">
      
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800">
        <div className="absolute inset-0 bg-gradient-to-br from-blue-50/50 to-indigo-50/50 dark:from-blue-900/20 dark:to-indigo-900/20 pointer-events-none" />
        
        {/* Decorative Shapes */}
        <div className="absolute -top-24 -right-24 w-96 h-96 bg-blue-400/10 rounded-full blur-3xl animate-pulse" />
        <div className="absolute -bottom-24 -left-24 w-96 h-96 bg-indigo-400/10 rounded-full blur-3xl animate-pulse" style={{ animationDelay: '2s' }} />

        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 lg:py-28 relative z-10">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            
            <div className="space-y-8 animate-in slide-in-from-left-8 duration-700 fade-in">
              <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-blue-50 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 font-semibold text-sm border border-blue-100 dark:border-blue-800/50 shadow-sm">
                <Zap size={16} className="fill-current" />
                <span>Enterprise Summer Sale is Live</span>
              </div>
              
              <h1 className="text-5xl lg:text-7xl font-black text-slate-900 dark:text-white leading-[1.1] tracking-tight">
                Welcome back, <br/>
                <span className="bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-indigo-600">
                  {isAuthenticated ? user?.firstName : 'Innovator'}
                </span>
              </h1>
              
              <p className="text-lg text-slate-600 dark:text-slate-300 max-w-xl leading-relaxed">
                Discover the latest enterprise-grade hardware, professional accessories, and high-performance equipment designed to elevate your workflow.
              </p>
              
              <div className="flex flex-col sm:flex-row gap-4">
                <Link to="/products" className="gradient-btn px-8 py-4 rounded-xl font-bold flex items-center justify-center gap-2 text-lg shadow-[0_8px_20px_rgba(37,99,235,0.25)]">
                  Shop Now <ArrowRight size={20} />
                </Link>
                <Link to="/cart" className="px-8 py-4 rounded-xl font-bold flex items-center justify-center gap-2 text-lg bg-white dark:bg-slate-800 text-slate-900 dark:text-white border-2 border-slate-200 dark:border-slate-700 hover:border-primary hover:text-primary transition-colors shadow-sm">
                  View Cart
                </Link>
              </div>
            </div>

            <div className="relative hidden lg:block animate-in slide-in-from-right-8 duration-700 fade-in">
              <div className="relative aspect-square">
                <div className="absolute inset-0 bg-gradient-to-tr from-blue-500 to-indigo-500 rounded-full blur-3xl opacity-20" />
                <img 
                  src="https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80" 
                  alt="Premium Hardware" 
                  className="relative z-10 w-full h-full object-cover rounded-3xl shadow-2xl border border-white/20"
                />
                
                {/* Floating Elements */}
                <div className="absolute -left-8 top-1/4 glass p-4 rounded-2xl flex items-center gap-4 animate-bounce" style={{ animationDuration: '3s' }}>
                  <div className="w-12 h-12 bg-emerald-100 rounded-xl flex items-center justify-center text-emerald-600">
                    <TrendingUp size={24} />
                  </div>
                  <div>
                    <p className="text-sm font-bold text-slate-900">Top Rated</p>
                    <p className="text-xs text-slate-500">4.9/5 from users</p>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </section>

      {/* Statistics Row */}
      <section className="py-12 border-b border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 relative z-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 lg:gap-8 -mt-20">
            {stats.map((stat, idx) => (
              <div key={idx} className="glass-card p-6 flex flex-col justify-between hover:-translate-y-1 transition-transform">
                <div className="flex justify-between items-start mb-4">
                  <div className={`w-12 h-12 rounded-xl flex items-center justify-center ${stat.bg} ${stat.color}`}>
                    <stat.icon size={24} />
                  </div>
                  {stat.trend && (
                    <span className="text-xs font-bold text-emerald-500 bg-emerald-50 px-2 py-1 rounded-full">
                      {stat.trend}
                    </span>
                  )}
                </div>
                <div>
                  <h3 className="text-3xl font-black text-slate-900 dark:text-white mb-1">{stat.value}</h3>
                  <p className="text-sm font-medium text-slate-500">{stat.title}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Trending Products Carousel */}
      <section className="py-16 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-end mb-8">
          <div>
            <h2 className="text-3xl font-black text-slate-900 dark:text-white mb-2">Trending Now</h2>
            <p className="text-slate-500">Most popular choices among enterprise users this week.</p>
          </div>
          <Link to="/products" className="hidden sm:flex items-center gap-2 text-primary font-bold hover:gap-3 transition-all">
            View All <ArrowRight size={20} />
          </Link>
        </div>
        
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {featuredProducts.map(product => (
            <ProductCard 
              key={product.sku}
              product={product}
              isWishlisted={isWishlisted(product.sku)}
              isAdding={addingToCart[product.sku]}
              onToggleWishlist={handleToggleWishlist}
              onAddToCart={addToCart}
              onQuickView={setQuickViewProduct}
            />
          ))}
          {featuredProducts.length === 0 && [1,2,3,4].map(i => (
            <div key={i} className="h-[420px] bg-slate-200/50 dark:bg-slate-800/50 animate-pulse rounded-2xl" />
          ))}
        </div>
      </section>

      {/* Promotional Banner */}
      <section className="py-12 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="rounded-3xl overflow-hidden relative shadow-2xl group">
          <div className="absolute inset-0 bg-gradient-to-r from-slate-900 via-indigo-900 to-slate-900 z-10 opacity-90" />
          <img src="https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=1200&q=80" className="absolute inset-0 w-full h-full object-cover group-hover:scale-105 transition-transform duration-700" alt="Promo" />
          
          <div className="relative z-20 p-10 lg:p-16 flex flex-col md:flex-row items-center justify-between gap-8">
            <div className="text-center md:text-left">
              <span className="inline-block bg-red-500 text-white font-black px-4 py-1.5 rounded-full text-sm uppercase tracking-wider mb-4">Limited Time Offer</span>
              <h2 className="text-4xl lg:text-5xl font-black text-white mb-4">Upgrade Your Setup</h2>
              <p className="text-slate-300 text-lg max-w-xl">Get up to 40% off on select premium workstations, monitors, and ergonomic accessories.</p>
            </div>
            
            <div className="flex flex-col items-center">
              <div className="flex gap-4 mb-6">
                {/* Countdown visual */}
                <div className="flex flex-col items-center bg-white/10 backdrop-blur-md rounded-xl p-3 w-16">
                  <span className="text-2xl font-black text-white">02</span>
                  <span className="text-[10px] uppercase text-slate-300 font-bold">Days</span>
                </div>
                <div className="flex flex-col items-center bg-white/10 backdrop-blur-md rounded-xl p-3 w-16">
                  <span className="text-2xl font-black text-white">14</span>
                  <span className="text-[10px] uppercase text-slate-300 font-bold">Hrs</span>
                </div>
                <div className="flex flex-col items-center bg-white/10 backdrop-blur-md rounded-xl p-3 w-16">
                  <span className="text-2xl font-black text-white">45</span>
                  <span className="text-[10px] uppercase text-slate-300 font-bold">Mins</span>
                </div>
              </div>
              <Link to="/products?search=sale" className="bg-white text-slate-900 hover:bg-slate-100 font-black px-8 py-4 rounded-xl w-full text-center transition-colors">
                Claim Offer Now
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Feature Grid */}
      <section className="py-16 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid md:grid-cols-3 gap-6">
          <div className="glass-card p-8 text-center group">
            <div className="w-16 h-16 mx-auto bg-blue-50 dark:bg-slate-800 rounded-full flex items-center justify-center text-primary mb-6 group-hover:scale-110 transition-transform">
              <Truck size={32} />
            </div>
            <h3 className="text-xl font-bold mb-3">Global Delivery</h3>
            <p className="text-slate-500">Fast, insured shipping to over 150 countries worldwide with real-time tracking.</p>
          </div>
          <div className="glass-card p-8 text-center group">
            <div className="w-16 h-16 mx-auto bg-emerald-50 dark:bg-slate-800 rounded-full flex items-center justify-center text-emerald-500 mb-6 group-hover:scale-110 transition-transform">
              <ShieldCheck size={32} />
            </div>
            <h3 className="text-xl font-bold mb-3">Extended Warranty</h3>
            <p className="text-slate-500">All enterprise products come with a 3-year comprehensive hardware warranty.</p>
          </div>
          <div className="glass-card p-8 text-center group">
            <div className="w-16 h-16 mx-auto bg-amber-50 dark:bg-slate-800 rounded-full flex items-center justify-center text-amber-500 mb-6 group-hover:scale-110 transition-transform">
              <Clock size={32} />
            </div>
            <h3 className="text-xl font-bold mb-3">24/7 Priority Support</h3>
            <p className="text-slate-500">Get instant access to our dedicated enterprise tech support team anytime.</p>
          </div>
        </div>
      </section>

      <QuickViewModal 
        isOpen={!!quickViewProduct}
        product={quickViewProduct}
        onClose={() => setQuickViewProduct(null)}
        onAddToCart={addToCart}
        isAdding={quickViewProduct ? addingToCart[quickViewProduct.sku] : false}
      />
      
    </div>
  );
};

export default Home;
