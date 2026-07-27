import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Search, Package, Filter, SlidersHorizontal, ChevronDown } from 'lucide-react';
import { cartStart, cartSuccess, cartFailure } from '../../redux/slices/cartSlice';
import { toggleWishlistItem } from '../../redux/slices/wishlistSlice';
import api from '../../services/api';
import toast from 'react-hot-toast';
import ProductCard from '../../components/products/ProductCard';
import QuickViewModal from '../../components/products/QuickViewModal';
import { useSearchParams } from 'react-router-dom';

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
  },
  {
    id: '5', sku: 'HDR-005', name: 'Noise Cancelling Headphones', price: 299.99, discountPrice: 199.99,
    category: 'Audio', active: true, stock: 20,
    images: ['https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&q=80'],
    description: 'Premium ANC headphones with 30-hour battery.'
  },
  {
    id: '6', sku: 'WEB-006', name: 'HD Webcam 1080p', price: 79.99, discountPrice: null,
    category: 'Accessories', active: true, stock: 40,
    images: ['https://images.unsplash.com/photo-1611532736597-de2d4265fba3?w=400&q=80'],
    description: 'Sharp HD webcam with built-in mic.'
  },
  {
    id: '7', sku: 'CHR-007', name: 'USB-C Fast Charger 65W', price: 39.99, discountPrice: 24.99,
    category: 'Accessories', active: true, stock: 100,
    images: ['https://images.unsplash.com/photo-1591488320449-011701bb6704?w=400&q=80'],
    description: '65W GaN USB-C charger, ultra-compact.'
  },
  {
    id: '8', sku: 'SSD-008', name: 'Portable SSD 1TB', price: 119.99, discountPrice: 89.99,
    category: 'Storage', active: true, stock: 25,
    images: ['https://images.unsplash.com/photo-1531492746076-161ca9bcad58?w=400&q=80'],
    description: 'Ultra-fast 1TB portable SSD, USB 3.2.'
  },
];

const Products = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  
  const [addingToCart, setAddingToCart] = useState({});
  const [quickViewProduct, setQuickViewProduct] = useState(null);
  const [activeCategory, setActiveCategory] = useState('All');

  const { isAuthenticated } = useSelector(state => state.auth);
  const wishlistItems = useSelector(state => state.wishlist?.items || []);
  const dispatch = useDispatch();

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await api.get('/products').catch(() => ({ data: { data: [] } }));
      let fetched = response?.data?.data;
      if (!fetched || fetched.length === 0) {
        fetched = DUMMY_PRODUCTS;
      }
      setProducts(fetched);
    } catch {
      setProducts(DUMMY_PRODUCTS);
    } finally {
      setLoading(false);
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
      const response = await api.post('/cart/items', {
        sku: product.sku,
        quantity: 1,
      });
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

  const categories = ['All', ...new Set(products.map(p => p.category).filter(Boolean))];

  const filteredProducts = products.filter(p => {
    const matchesSearch = p.name.toLowerCase().includes(search.toLowerCase()) || 
                          (p.category || '').toLowerCase().includes(search.toLowerCase());
    const matchesCategory = activeCategory === 'All' || p.category === activeCategory;
    return matchesSearch && matchesCategory;
  });

  return (
    <div className="bg-slate-50 dark:bg-slate-950 min-h-screen pb-20">
      
      {/* Header Banner */}
      <div className="bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 pt-8 pb-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row justify-between items-end gap-6">
            <div>
              <h1 className="text-4xl font-black text-slate-900 dark:text-white mb-2 tracking-tight">Explore Collection</h1>
              <p className="text-slate-500">Discover our premium range of enterprise equipment.</p>
            </div>
            <div className="flex items-center gap-3">
              <span className="text-sm font-medium text-slate-500">
                Showing <strong className="text-slate-900 dark:text-white">{filteredProducts.length}</strong> products
              </span>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex flex-col lg:flex-row gap-8">
          
          {/* Sidebar / Filters */}
          <div className="lg:w-64 flex-shrink-0">
            <div className="sticky top-28 space-y-6">
              
              <div className="bg-white dark:bg-slate-900 rounded-2xl p-5 border border-slate-200 dark:border-slate-800 shadow-sm">
                <div className="flex items-center gap-2 font-bold mb-4 pb-4 border-b border-slate-100 dark:border-slate-800">
                  <Filter size={18} className="text-primary" />
                  Categories
                </div>
                <div className="space-y-1">
                  {categories.map(cat => (
                    <button
                      key={cat}
                      onClick={() => setActiveCategory(cat)}
                      className={`w-full text-left px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                        activeCategory === cat 
                          ? 'bg-primary/10 text-primary' 
                          : 'text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800'
                      }`}
                    >
                      {cat}
                    </button>
                  ))}
                </div>
              </div>

              {/* Add more filters here like price range, brand, etc. */}
              
            </div>
          </div>

          {/* Product Grid */}
          <div className="flex-1">
            
            {/* Toolbar */}
            <div className="flex justify-between items-center mb-6 bg-white dark:bg-slate-900 p-3 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm">
              <div className="flex items-center gap-2 px-3">
                <Search size={18} className="text-slate-400" />
                <input
                  type="text"
                  value={search}
                  onChange={(e) => setSearchParams({ search: e.target.value })}
                  placeholder="Search in results..."
                  className="bg-transparent border-none focus:outline-none text-sm w-48 text-slate-900 dark:text-white"
                />
              </div>
              <button className="flex items-center gap-2 px-4 py-2 bg-slate-50 dark:bg-slate-800 rounded-xl text-sm font-medium hover:bg-slate-100 dark:hover:bg-slate-700 transition-colors">
                <SlidersHorizontal size={16} />
                Sort By
                <ChevronDown size={14} />
              </button>
            </div>

            {loading ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {[1, 2, 3, 4, 5, 6].map(i => (
                  <div key={i} className="glass-card h-[420px] animate-pulse bg-slate-200/50 dark:bg-slate-800/50" />
                ))}
              </div>
            ) : filteredProducts.length === 0 ? (
              <div className="bg-white dark:bg-slate-900 rounded-3xl p-16 text-center border border-slate-200 dark:border-slate-800 shadow-sm">
                <div className="w-24 h-24 bg-slate-100 dark:bg-slate-800 rounded-full flex items-center justify-center mx-auto mb-6">
                  <Package size={40} className="text-slate-400" />
                </div>
                <h3 className="text-xl font-bold mb-2">No products found</h3>
                <p className="text-slate-500 mb-6 max-w-md mx-auto">We couldn't find anything matching "{search}" in {activeCategory}. Try adjusting your filters.</p>
                <button 
                  onClick={() => { setSearchParams({}); setActiveCategory('All'); }}
                  className="bg-slate-900 dark:bg-white text-white dark:text-slate-900 px-6 py-2.5 rounded-full font-medium"
                >
                  Clear Filters
                </button>
              </div>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-6">
                {filteredProducts.map((product) => (
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
              </div>
            )}
          </div>
        </div>
      </div>

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

export default Products;
