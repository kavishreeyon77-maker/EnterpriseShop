import { useEffect } from 'react';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import { CheckCircle, Package, ArrowRight, Download } from 'lucide-react';
import { motion } from 'framer-motion';
import confetti from 'canvas-confetti';

const OrderConfirmation = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const order = location.state?.order;

  useEffect(() => {
    if (!order) {
      navigate('/');
      return;
    }
    
    // Trigger confetti
    const duration = 3 * 1000;
    const end = Date.now() + duration;

    const frame = () => {
      confetti({
        particleCount: 5,
        angle: 60,
        spread: 55,
        origin: { x: 0 },
        colors: ['#3b82f6', '#10b981']
      });
      confetti({
        particleCount: 5,
        angle: 120,
        spread: 55,
        origin: { x: 1 },
        colors: ['#3b82f6', '#10b981']
      });

      if (Date.now() < end) {
        requestAnimationFrame(frame);
      }
    };
    frame();
  }, [order, navigate]);

  if (!order) return null;

  return (
    <div className="min-h-[calc(100vh-16rem)] flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <motion.div 
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
        className="max-w-2xl w-full glass-card p-8 md:p-12 text-center"
      >
        <div className="flex justify-center mb-6">
          <motion.div 
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            transition={{ delay: 0.3, type: "spring" }}
            className="w-24 h-24 bg-emerald-100 text-emerald-600 rounded-full flex items-center justify-center"
          >
            <CheckCircle size={48} />
          </motion.div>
        </div>
        
        <h1 className="text-3xl font-extrabold text-foreground mb-2">Order Confirmed!</h1>
        <p className="text-muted-foreground mb-8">
          Thank you for your purchase. We've received your order and are getting it ready to ship.
        </p>

        <div className="bg-secondary p-6 rounded-xl text-left mb-8">
          <div className="flex items-center gap-3 mb-4 text-foreground font-bold border-b border-border pb-4">
            <Package size={24} className="text-primary" />
            <span>Order #{order.orderNumber}</span>
          </div>
          
          <div className="space-y-3 text-sm">
            <div className="flex justify-between">
              <span className="text-muted-foreground">Date:</span>
              <span className="font-medium text-foreground">{new Date(order.createdAt).toLocaleDateString()}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-muted-foreground">Payment Method:</span>
              <span className="font-medium text-foreground">{order.paymentMethod.replace('_', ' ')}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-muted-foreground">Total Amount:</span>
              <span className="font-bold text-primary">${order.grandTotal.toFixed(2)}</span>
            </div>
          </div>
        </div>

        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <button className="flex items-center justify-center gap-2 bg-secondary text-secondary-foreground hover:bg-secondary/80 px-6 py-3 rounded-lg font-medium transition-colors">
            <Download size={20} />
            <span>Download Invoice</span>
          </button>
          
          <Link to="/products" className="flex items-center justify-center gap-2 gradient-btn px-6 py-3 rounded-lg font-medium">
            <span>Continue Shopping</span>
            <ArrowRight size={20} />
          </Link>
        </div>
      </motion.div>
    </div>
  );
};

export default OrderConfirmation;
