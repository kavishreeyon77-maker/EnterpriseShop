import { Routes, Route, Link } from 'react-router-dom';

const Dashboard = () => {
  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div className="flex flex-col md:flex-row gap-8">
        <aside className="w-full md:w-64 flex-shrink-0">
          <div className="glass-card p-4 flex flex-col gap-2">
            <h2 className="text-lg font-bold text-foreground mb-2 px-2">My Account</h2>
            <Link to="/dashboard" className="px-4 py-2 text-sm text-foreground bg-secondary rounded-lg font-medium">Orders</Link>
            <Link to="/dashboard/addresses" className="px-4 py-2 text-sm text-muted-foreground hover:bg-secondary/50 rounded-lg transition-colors">Addresses</Link>
            <Link to="/dashboard/wishlist" className="px-4 py-2 text-sm text-muted-foreground hover:bg-secondary/50 rounded-lg transition-colors">Wishlist</Link>
          </div>
        </aside>
        
        <main className="flex-1 glass-card p-6">
          <Routes>
            <Route index element={<div><h2>Order History</h2><p className="text-muted-foreground mt-4">You have no recent orders.</p></div>} />
            <Route path="addresses" element={<div><h2>My Addresses</h2><p className="text-muted-foreground mt-4">No saved addresses.</p></div>} />
            <Route path="wishlist" element={<div><h2>My Wishlist</h2><p className="text-muted-foreground mt-4">Your wishlist is empty.</p></div>} />
          </Routes>
        </main>
      </div>
    </div>
  );
};

export default Dashboard;
