import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';
import MainLayout from '../components/layout/MainLayout';
import Home from '../pages/Home';
import Login from '../pages/auth/Login';
import Register from '../pages/auth/Register';
import CartPage from '../pages/cart/CartPage';
import CheckoutWizard from '../pages/checkout/CheckoutWizard';
import OrderConfirmation from '../pages/checkout/OrderConfirmation';
import Dashboard from '../pages/user/Dashboard';
import NotFound from '../pages/NotFound';
import Products from '../pages/products/Products';
import WishlistPage from '../pages/wishlist/WishlistPage';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useSelector((state) => state.auth);
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
  return children;
};

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        {/* Public Routes */}
        <Route index element={<Home />} />
        <Route path="login" element={<Login />} />
        <Route path="register" element={<Register />} />
        <Route path="products" element={<Products />} />
        <Route path="cart" element={<CartPage />} />
        <Route path="wishlist" element={<WishlistPage />} />
        
        {/* Protected Routes */}
        <Route path="checkout" element={
          <ProtectedRoute>
            <CheckoutWizard />
          </ProtectedRoute>
        } />
        <Route path="order-confirmation" element={
          <ProtectedRoute>
            <OrderConfirmation />
          </ProtectedRoute>
        } />
        <Route path="dashboard/*" element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        } />
        
        {/* Fallback */}
        <Route path="*" element={<NotFound />} />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
