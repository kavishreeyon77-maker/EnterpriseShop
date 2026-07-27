import { Link } from 'react-router-dom';
import { Globe, MessageCircle, Video, Share2, Mail, MapPin, Phone, CreditCard, ShieldCheck, Truck, Clock } from 'lucide-react';

const Footer = () => {
  return (
    <footer className="bg-slate-950 text-slate-300 pt-16 pb-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Features Row */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-16 border-b border-slate-800 pb-16">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-slate-900 flex items-center justify-center text-primary">
              <Truck size={24} />
            </div>
            <div>
              <h4 className="text-white font-bold mb-1">Free Shipping</h4>
              <p className="text-sm text-slate-400">On all orders over $100</p>
            </div>
          </div>
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-slate-900 flex items-center justify-center text-emerald-500">
              <ShieldCheck size={24} />
            </div>
            <div>
              <h4 className="text-white font-bold mb-1">Secure Payment</h4>
              <p className="text-sm text-slate-400">100% secure checkout</p>
            </div>
          </div>
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-slate-900 flex items-center justify-center text-amber-500">
              <Clock size={24} />
            </div>
            <div>
              <h4 className="text-white font-bold mb-1">24/7 Support</h4>
              <p className="text-sm text-slate-400">Dedicated help center</p>
            </div>
          </div>
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-full bg-slate-900 flex items-center justify-center text-purple-500">
              <CreditCard size={24} />
            </div>
            <div>
              <h4 className="text-white font-bold mb-1">Easy Returns</h4>
              <p className="text-sm text-slate-400">30-day return policy</p>
            </div>
          </div>
        </div>

        {/* Main Footer Content */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-12 mb-12">
          
          {/* Brand & About */}
          <div>
            <Link to="/" className="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-indigo-400 inline-block mb-6">
              EnterpriseShop
            </Link>
            <p className="text-sm text-slate-400 mb-6 leading-relaxed">
              Your premium destination for enterprise-grade electronics, accessories, and professional gear. Delivering excellence since 2026.
            </p>
            <div className="flex items-center gap-4">
              <a href="#" className="w-10 h-10 rounded-full bg-slate-900 flex items-center justify-center hover:bg-primary hover:text-white transition-colors"><Globe size={18} /></a>
              <a href="#" className="w-10 h-10 rounded-full bg-slate-900 flex items-center justify-center hover:bg-primary hover:text-white transition-colors"><MessageCircle size={18} /></a>
              <a href="#" className="w-10 h-10 rounded-full bg-slate-900 flex items-center justify-center hover:bg-primary hover:text-white transition-colors"><Video size={18} /></a>
              <a href="#" className="w-10 h-10 rounded-full bg-slate-900 flex items-center justify-center hover:bg-primary hover:text-white transition-colors"><Share2 size={18} /></a>
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-white font-bold text-lg mb-6">Quick Links</h4>
            <ul className="space-y-3 text-sm">
              <li><Link to="/" className="hover:text-primary transition-colors">Home</Link></li>
              <li><Link to="/products" className="hover:text-primary transition-colors">All Products</Link></li>
              <li><Link to="/about" className="hover:text-primary transition-colors">About Us</Link></li>
              <li><Link to="/contact" className="hover:text-primary transition-colors">Contact Support</Link></li>
              <li><Link to="/faq" className="hover:text-primary transition-colors">FAQs</Link></li>
            </ul>
          </div>

          {/* Contact Info */}
          <div>
            <h4 className="text-white font-bold text-lg mb-6">Contact Us</h4>
            <ul className="space-y-4 text-sm">
              <li className="flex items-start gap-3">
                <MapPin size={18} className="text-primary shrink-0 mt-0.5" />
                <span>123 Enterprise Way, Tech District<br />San Francisco, CA 94105</span>
              </li>
              <li className="flex items-center gap-3">
                <Phone size={18} className="text-primary shrink-0" />
                <span>+1 (800) 123-4567</span>
              </li>
              <li className="flex items-center gap-3">
                <Mail size={18} className="text-primary shrink-0" />
                <span>support@enterpriseshop.com</span>
              </li>
            </ul>
          </div>

          {/* Newsletter */}
          <div>
            <h4 className="text-white font-bold text-lg mb-6">Newsletter</h4>
            <p className="text-sm text-slate-400 mb-4">Subscribe to receive updates, access to exclusive deals, and more.</p>
            <form className="flex flex-col gap-3" onSubmit={(e) => e.preventDefault()}>
              <input 
                type="email" 
                placeholder="Enter your email address" 
                className="bg-slate-900 border border-slate-800 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:border-primary text-white"
                required
              />
              <button type="submit" className="bg-primary hover:bg-primary/90 text-white font-medium rounded-lg px-4 py-2.5 transition-colors">
                Subscribe
              </button>
            </form>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="border-t border-slate-800 pt-8 mt-8 flex flex-col md:flex-row justify-between items-center gap-4">
          <p className="text-sm text-slate-500">
            &copy; {new Date().getFullYear()} EnterpriseShop. All rights reserved.
          </p>
          <div className="flex items-center gap-4 text-slate-500 text-sm">
            <Link to="/privacy" className="hover:text-white transition-colors">Privacy Policy</Link>
            <Link to="/terms" className="hover:text-white transition-colors">Terms of Service</Link>
            <Link to="/shipping" className="hover:text-white transition-colors">Shipping Policy</Link>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
