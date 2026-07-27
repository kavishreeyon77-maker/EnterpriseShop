import { useState, useRef, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { ShoppingCart, User, LogOut, Moon, Sun, Menu, Search, Heart, Bell, ChevronDown, Settings, Package, Tag, Truck, Check, CheckCheck } from 'lucide-react';
import { logout } from '../../redux/slices/authSlice';
import { toggleTheme } from '../../redux/slices/themeSlice';
import toast from 'react-hot-toast';

const INITIAL_NOTIFICATIONS = [
  { id: 1, icon: Tag, color: 'text-blue-500 bg-blue-50 dark:bg-blue-900/30', title: 'Summer Sale is Live!', body: 'Up to 40% off on laptops, monitors and accessories. Limited time only.', time: '2 min ago', read: false },
  { id: 2, icon: Truck, color: 'text-emerald-500 bg-emerald-50 dark:bg-emerald-900/30', title: 'Order Shipped', body: 'Your order #ORD-2340 has been shipped and is on its way!', time: '1 hour ago', read: false },
  { id: 3, icon: Package, color: 'text-indigo-500 bg-indigo-50 dark:bg-indigo-900/30', title: 'New Products Added', body: '8 new enterprise workstations have been added to our catalogue.', time: '3 hours ago', read: true },
  { id: 4, icon: Tag, color: 'text-amber-500 bg-amber-50 dark:bg-amber-900/30', title: 'Price Drop Alert', body: 'The Noise Cancelling Headphones you saved dropped to $199.99!', time: 'Yesterday', read: true },
];

const Navbar = ({ onCartClick }) => {
  const { isAuthenticated, user } = useSelector((state) => state.auth);
  const { cart } = useSelector((state) => state.cart);
  const { darkMode } = useSelector((state) => state.theme);
  const wishlistCount = useSelector((state) => state.wishlist?.items?.length || 0);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [profileOpen, setProfileOpen] = useState(false);
  const [notifOpen, setNotifOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [notifications, setNotifications] = useState(INITIAL_NOTIFICATIONS);

  const notifRef = useRef(null);
  const profileRef = useRef(null);

  const cartItemCount = cart?.items?.reduce((total, item) => total + item.quantity, 0) || 0;
  const unreadCount = notifications.filter(n => !n.read).length;

  // Close dropdowns when clicking outside
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (notifRef.current && !notifRef.current.contains(e.target)) setNotifOpen(false);
      if (profileRef.current && !profileRef.current.contains(e.target)) setProfileOpen(false);
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleMarkAllRead = () => {
    setNotifications(prev => prev.map(n => ({ ...n, read: true })));
    toast.success('All notifications marked as read');
  };

  const handleMarkRead = (id) => {
    setNotifications(prev => prev.map(n => n.id === id ? { ...n, read: true } : n));
  };

  const handleLogout = () => {
    dispatch(logout());
    setProfileOpen(false);
    toast.success('Logged out successfully');
    navigate('/');
  };

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/products?search=${encodeURIComponent(searchQuery)}`);
    }
  };

  return (
    <nav className="sticky top-0 z-50 glass border-b border-white/20 dark:border-slate-800/50 transition-all">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-20 items-center gap-6">

          {/* Logo */}
          <div className="flex-shrink-0 flex items-center">
            <Link to="/" className="flex items-center gap-2 group">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white shadow-lg group-hover:scale-105 transition-transform">
                <Package size={22} strokeWidth={2.5} />
              </div>
              <span className="text-2xl font-black bg-clip-text text-transparent bg-gradient-to-r from-slate-900 to-slate-700 dark:from-white dark:to-slate-300 hidden sm:block tracking-tight">
                EnterpriseShop
              </span>
            </Link>
          </div>

          {/* Search Bar - Center */}
          <div className="flex-1 max-w-2xl hidden md:block">
            <form onSubmit={handleSearch} className="relative group">
              <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                <Search className="h-5 w-5 text-slate-400 group-focus-within:text-primary transition-colors" />
              </div>
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="block w-full pl-11 pr-4 py-2.5 bg-slate-100/50 dark:bg-slate-900/50 border border-slate-200 dark:border-slate-800 rounded-full leading-5 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary focus:bg-white dark:focus:bg-slate-900 transition-all text-sm shadow-inner"
                placeholder="Search premium products, brands, and categories..."
              />
              <button type="submit" className="absolute inset-y-1.5 right-1.5 bg-primary hover:bg-primary/90 text-white rounded-full px-4 text-sm font-medium transition-colors shadow-sm">
                Search
              </button>
            </form>
          </div>

          {/* Icons & Actions */}
          <div className="flex items-center space-x-2 sm:space-x-4">

            <button
              onClick={() => dispatch(toggleTheme())}
              className="p-2.5 rounded-full text-slate-500 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
              aria-label="Toggle Dark Mode"
            >
              {darkMode ? <Sun size={20} /> : <Moon size={20} />}
            </button>

            {/* Notifications */}
            <div className="relative hidden sm:block" ref={notifRef}>
              <button
                onClick={() => { setNotifOpen(prev => !prev); setProfileOpen(false); }}
                className="p-2.5 rounded-full text-slate-500 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors relative"
                aria-label="Notifications"
              >
                <Bell size={20} />
                {unreadCount > 0 && (
                  <span className="absolute top-1.5 right-1.5 w-2.5 h-2.5 bg-red-500 rounded-full ring-2 ring-white dark:ring-slate-900 animate-pulse" />
                )}
              </button>

              {/* Notification Dropdown */}
              {notifOpen && (
                <div className="absolute right-0 mt-3 w-96 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl shadow-2xl z-50 animate-in fade-in overflow-hidden">
                  {/* Header */}
                  <div className="flex items-center justify-between px-5 py-4 border-b border-slate-100 dark:border-slate-800">
                    <div className="flex items-center gap-2">
                      <h3 className="font-bold text-slate-900 dark:text-white">Notifications</h3>
                      {unreadCount > 0 && (
                        <span className="text-xs font-bold bg-primary/10 text-primary px-2 py-0.5 rounded-full">{unreadCount} new</span>
                      )}
                    </div>
                    {unreadCount > 0 && (
                      <button
                        onClick={handleMarkAllRead}
                        className="flex items-center gap-1 text-xs font-medium text-primary hover:text-primary/80 transition-colors"
                      >
                        <CheckCheck size={14} /> Mark all read
                      </button>
                    )}
                  </div>

                  {/* Notification List */}
                  <div className="max-h-[420px] overflow-y-auto hide-scrollbar divide-y divide-slate-100 dark:divide-slate-800">
                    {notifications.map((notif) => (
                      <div
                        key={notif.id}
                        onClick={() => handleMarkRead(notif.id)}
                        className={`flex items-start gap-4 px-5 py-4 cursor-pointer transition-colors hover:bg-slate-50 dark:hover:bg-slate-800/50 ${!notif.read ? 'bg-blue-50/50 dark:bg-blue-900/10' : ''}`}
                      >
                        <div className={`w-10 h-10 rounded-full flex-shrink-0 flex items-center justify-center ${notif.color}`}>
                          <notif.icon size={18} />
                        </div>
                        <div className="flex-1 min-w-0">
                          <div className="flex justify-between items-start gap-2">
                            <p className={`text-sm font-semibold leading-tight ${!notif.read ? 'text-slate-900 dark:text-white' : 'text-slate-600 dark:text-slate-300'}`}>
                              {notif.title}
                            </p>
                            {!notif.read && (
                              <span className="w-2 h-2 bg-primary rounded-full flex-shrink-0 mt-1.5" />
                            )}
                          </div>
                          <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5 line-clamp-2">{notif.body}</p>
                          <p className="text-[11px] text-slate-400 dark:text-slate-500 mt-1.5 font-medium">{notif.time}</p>
                        </div>
                      </div>
                    ))}
                  </div>

                  {/* Footer */}
                  <div className="px-5 py-3 border-t border-slate-100 dark:border-slate-800 bg-slate-50/50 dark:bg-slate-800/50">
                    <button
                      onClick={() => setNotifOpen(false)}
                      className="w-full text-center text-sm font-semibold text-primary hover:text-primary/80 transition-colors py-1"
                    >
                      View all notifications
                    </button>
                  </div>
                </div>
              )}
            </div>

            {/* Wishlist */}
            <Link to="/wishlist" className="hidden sm:block p-2.5 rounded-full text-slate-500 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors relative">
              <Heart size={20} />
              {wishlistCount > 0 && (
                <span className="absolute -top-1.5 -right-1.5 inline-flex items-center justify-center min-w-[18px] h-[18px] px-1 text-[10px] font-bold text-white bg-red-500 rounded-full ring-2 ring-white dark:ring-slate-900">
                  {wishlistCount}
                </span>
              )}
            </Link>

            {/* Cart Button */}
            <button
              onClick={onCartClick}
              className="relative p-2.5 rounded-full text-slate-500 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors flex items-center gap-2 group"
            >
              <div className="relative">
                <ShoppingCart size={22} className="group-hover:scale-110 transition-transform" />
                {cartItemCount > 0 && (
                  <span className="absolute -top-1.5 -right-1.5 inline-flex items-center justify-center min-w-[20px] h-[20px] px-1 text-[11px] font-bold text-white bg-red-600 rounded-full shadow-sm ring-2 ring-white dark:ring-slate-900 animate-in zoom-in duration-300">
                    {cartItemCount}
                  </span>
                )}
              </div>
            </button>

            {/* User Profile */}
            {isAuthenticated ? (
              <div className="relative" ref={profileRef}>
                <button
                  onClick={() => { setProfileOpen(prev => !prev); setNotifOpen(false); }}
                  className="flex items-center gap-2 p-1.5 pr-3 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors border border-transparent hover:border-slate-200 dark:hover:border-slate-700"
                >
                  <div className="w-8 h-8 rounded-full bg-gradient-to-r from-blue-100 to-indigo-100 dark:from-blue-900 dark:to-indigo-900 flex items-center justify-center text-primary font-bold text-sm">
                    {user?.firstName?.charAt(0) || 'U'}
                  </div>
                  <span className="text-sm font-medium hidden md:block">{user?.firstName}</span>
                  <ChevronDown size={16} className={`text-slate-400 transition-transform ${profileOpen ? 'rotate-180' : ''}`} />
                </button>

                {/* Dropdown Menu */}
                {profileOpen && (
                  <div className="absolute right-0 mt-2 w-56 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-xl shadow-xl py-2 animate-in fade-in z-50">
                    <div className="px-4 py-3 border-b border-slate-100 dark:border-slate-800">
                      <p className="text-sm text-slate-500">Signed in as</p>
                      <p className="text-sm font-bold truncate">{user?.email}</p>
                    </div>
                    <Link to="/dashboard" onClick={() => setProfileOpen(false)} className="flex items-center gap-3 px-4 py-2 text-sm hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors">
                      <User size={16} className="text-slate-400" /> My Profile
                    </Link>
                    <Link to="/dashboard/orders" onClick={() => setProfileOpen(false)} className="flex items-center gap-3 px-4 py-2 text-sm hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors">
                      <Package size={16} className="text-slate-400" /> Orders
                    </Link>
                    <Link to="/dashboard/settings" onClick={() => setProfileOpen(false)} className="flex items-center gap-3 px-4 py-2 text-sm hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors">
                      <Settings size={16} className="text-slate-400" /> Settings
                    </Link>
                    <div className="border-t border-slate-100 dark:border-slate-800 my-1"></div>
                    <button
                      onClick={handleLogout}
                      className="w-full flex items-center gap-3 px-4 py-2 text-sm text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
                    >
                      <LogOut size={16} /> Logout
                    </button>
                  </div>
                )}
              </div>
            ) : (
              <div className="hidden md:flex items-center space-x-3 ml-2">
                <Link to="/login" className="text-sm font-medium text-slate-600 dark:text-slate-300 hover:text-primary transition-colors px-3 py-2">
                  Sign In
                </Link>
                <Link to="/register" className="gradient-btn px-5 py-2.5 rounded-full text-sm font-medium">
                  Create Account
                </Link>
              </div>
            )}

            {/* Mobile menu button */}
            <button className="md:hidden p-2 text-slate-500 hover:text-slate-900 dark:hover:text-white">
              <Menu size={24} />
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
