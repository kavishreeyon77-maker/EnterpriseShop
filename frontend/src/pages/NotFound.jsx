import { Link } from 'react-router-dom';
import { ShoppingCart } from 'lucide-react';

const NotFound = () => {
  return (
    <div className="min-h-[calc(100vh-16rem)] flex flex-col items-center justify-center text-center px-4">
      <ShoppingCart size={64} className="text-muted-foreground mb-6 opacity-20" />
      <h1 className="text-6xl font-extrabold text-foreground mb-4">404</h1>
      <h2 className="text-2xl font-bold text-foreground mb-2">Page Not Found</h2>
      <p className="text-muted-foreground max-w-md mb-8">
        We couldn't find the page you're looking for. It might have been moved, deleted, or perhaps never existed.
      </p>
      <Link to="/" className="gradient-btn px-6 py-3 rounded-lg font-medium">
        Back to Homepage
      </Link>
    </div>
  );
};

export default NotFound;
