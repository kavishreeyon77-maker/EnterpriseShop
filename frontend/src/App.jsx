import { BrowserRouter as Router } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { useSelector, useDispatch } from 'react-redux';
import { useEffect } from 'react';
import AppRoutes from './routes/AppRoutes';
import api from './services/api';
import { cartSuccess } from './redux/slices/cartSlice';

function App() {
  const { darkMode } = useSelector((state) => state.theme);
  const { isAuthenticated } = useSelector((state) => state.auth);
  const dispatch = useDispatch();

  // Apply dark/light theme class
  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [darkMode]);

  // Fetch cart from backend whenever user is authenticated
  useEffect(() => {
    if (isAuthenticated) {
      api.get('/cart')
        .then(res => {
          if (res?.data?.data) dispatch(cartSuccess(res.data.data));
        })
        .catch(() => {}); // silently fail if cart fetch fails
    }
  }, [isAuthenticated]);

  return (
    <Router>
      <div className="min-h-screen bg-background text-foreground font-sans">
        <AppRoutes />
        <Toaster 
          position="bottom-right"
          toastOptions={{
            className: 'dark:bg-slate-800 dark:text-white',
            duration: 3000,
          }}
        />
      </div>
    </Router>
  );
}

export default App;

