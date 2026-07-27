import { Heart, ShoppingCart, Eye, Star, Package } from 'lucide-react';

const ProductCard = ({ 
  product, 
  isWishlisted, 
  isAdding, 
  onToggleWishlist, 
  onAddToCart, 
  onQuickView 
}) => {
  const displayPrice = product.discountPrice || product.price;
  const hasDiscount = product.discountPrice && product.discountPrice < product.price;
  const discountPct = hasDiscount 
    ? Math.round(((product.price - product.discountPrice) / product.price) * 100) 
    : 0;
  const imageUrl = product.images?.[0] || null;

  return (
    <div className="glass-card group flex flex-col overflow-hidden transition-all duration-500 hover:shadow-[0_20px_40px_rgba(0,0,0,0.08)] hover:-translate-y-2 h-[420px]">
      
      {/* Image Container */}
      <div className="relative h-[220px] bg-slate-50 dark:bg-slate-900/50 overflow-hidden flex items-center justify-center p-4">
        {imageUrl ? (
          <img 
            src={imageUrl} 
            alt={product.name} 
            className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700 ease-out"
            onError={(e) => { e.target.style.display = 'none'; e.target.nextSibling.style.display = 'flex'; }}
          />
        ) : null}
        <div className={`absolute inset-0 flex flex-col items-center justify-center text-slate-300 ${imageUrl ? 'hidden' : 'flex'}`}>
          <Package size={48} strokeWidth={1} />
        </div>

        {/* Badges */}
        <div className="absolute top-3 left-3 flex flex-col gap-2">
          {hasDiscount && (
            <span className="bg-red-500 text-white text-[10px] font-black px-2.5 py-1 rounded-full uppercase tracking-wider shadow-sm">
              {discountPct}% OFF
            </span>
          )}
          {product.stock !== undefined && product.stock < 10 && (
            <span className="bg-amber-500 text-white text-[10px] font-black px-2.5 py-1 rounded-full uppercase tracking-wider shadow-sm">
              Only {product.stock} left
            </span>
          )}
        </div>

        {/* Floating Actions */}
        <div className="absolute top-3 right-3 flex flex-col gap-2 transform translate-x-12 opacity-0 group-hover:translate-x-0 group-hover:opacity-100 transition-all duration-300">
          <button 
            onClick={(e) => { e.stopPropagation(); onToggleWishlist(product); }}
            className={`w-9 h-9 rounded-full flex items-center justify-center backdrop-blur-md transition-colors shadow-sm ${
              isWishlisted 
                ? 'bg-red-500 text-white' 
                : 'bg-white/80 dark:bg-slate-800/80 text-slate-600 hover:text-red-500 hover:bg-white'
            }`}
            title={isWishlisted ? 'Remove from wishlist' : 'Add to wishlist'}
          >
            <Heart size={18} fill={isWishlisted ? 'currentColor' : 'none'} />
          </button>
          
          <button 
            onClick={(e) => { e.stopPropagation(); onQuickView(product); }}
            className="w-9 h-9 rounded-full bg-white/80 dark:bg-slate-800/80 backdrop-blur-md flex items-center justify-center text-slate-600 hover:text-primary hover:bg-white transition-colors shadow-sm"
            title="Quick view"
          >
            <Eye size={18} />
          </button>
        </div>
      </div>

      {/* Content */}
      <div className="flex flex-col flex-1 p-5 border-t border-slate-100 dark:border-slate-800/50 bg-white dark:bg-slate-900/80">
        
        <div className="flex justify-between items-start mb-1.5">
          <span className="text-[10px] font-bold text-primary uppercase tracking-wider">
            {product.category || 'General'}
          </span>
          <div className="flex items-center gap-1 text-amber-400 text-xs">
            <Star fill="currentColor" size={12} />
            <span className="font-medium text-slate-600 dark:text-slate-400">
              {product.rating ? parseFloat(product.rating).toFixed(1) : '4.5'}
            </span>
          </div>
        </div>

        <h3 className="font-bold text-sm text-slate-900 dark:text-white line-clamp-2 mb-2 leading-relaxed group-hover:text-primary transition-colors">
          {product.name}
        </h3>

        <div className="mt-auto flex items-end justify-between">
          <div>
            {hasDiscount && (
              <p className="text-xs text-slate-400 line-through mb-0.5">
                ${parseFloat(product.price).toFixed(2)}
              </p>
            )}
            <p className="text-lg font-black text-slate-900 dark:text-white">
              ${parseFloat(displayPrice).toFixed(2)}
            </p>
          </div>
          
          <button 
            onClick={(e) => { e.preventDefault(); e.stopPropagation(); onAddToCart(product); }}
            disabled={isAdding}
            className={`h-10 px-4 rounded-xl font-bold flex items-center gap-2 transition-all ${
              isAdding 
                ? 'bg-slate-100 dark:bg-slate-800 text-primary cursor-wait' 
                : 'bg-primary/10 text-primary hover:bg-primary hover:text-white hover:shadow-[0_4px_12px_rgba(37,99,235,0.2)]'
            }`}
          >
            <ShoppingCart size={18} className={isAdding ? 'animate-bounce' : ''} />
            <span className="text-sm">Add</span>
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;
