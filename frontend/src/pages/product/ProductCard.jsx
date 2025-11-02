import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../../context/CartContext';
import toast from 'react-hot-toast';
import ImagePlaceholder from '../common/ImagePlaceholder';

/**
 * A reusable component to display a product tile in grids.
 * @param {object} product - The product object to display (must include id, name, price, stock, imageUrl, category).
 */
const ProductCard = ({ product }) => {
    const { addToCart } = useCart();

    // Check if the product object exists and has required fields
    if (!product || !product.id) {
        return null;
    }

    const handleAddToCart = (e) => {
        // Stop event propagation so clicking the button doesn't trigger the Link
        e.preventDefault();
        e.stopPropagation();

        if (product.stock === 0) {
            toast.error("This product is currently out of stock.");
            return;
        }

        addToCart({
            id: product.id,
            name: product.name,
            price: product.price,
            imageUrl: product.imageUrl,
            quantity: 1 // Always add 1 from the product card
        });

        toast.success(`${product.name} added to cart!`, {
            icon: '🛒',
        });
    };

    const displayImage = product.imageUrl && product.imageUrl.startsWith('http')
        ? product.imageUrl
        : null;


    return (
        // Link wraps the entire card so clicking anywhere goes to the detail page
        <Link
            to={`/products/${product.id}`}
            className="group block bg-white rounded-xl shadow-lg hover:shadow-2xl transition-shadow duration-300 transform hover:-translate-y-1 relative overflow-hidden"
        >
            {/* Image Placeholder/Container */}
            <div className="aspect-square w-full relative overflow-hidden">
                {displayImage ? (
                    <img
                        src={displayImage}
                        alt={product.name}
                        className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                        onError={(e) => {
                            // Fallback to ImagePlaceholder logic if component fails
                            e.target.style.display = 'none';
                        }}
                    />
                ) : (
                    // This assumes you have an ImagePlaceholder component for missing images
                    <ImagePlaceholder text={product.name} />
                )}
            </div>

            <div className="p-4 flex flex-col justify-between h-auto">
                {/* Text Details */}
                <div>
                    <h3 className="text-lg font-semibold text-gray-800 truncate mb-1" title={product.name}>
                        {product.name}
                    </h3>
                    <p className="text-sm text-gray-500 mb-2">
                        {product.category || 'General'}
                    </p>
                </div>

                {/* Price and Stock */}
                <div className="flex justify-between items-center mb-4">
                    <span className="text-xl font-bold text-indigo-600">
                        ${product.price ? product.price.toFixed(2) : 'N/A'}
                    </span>
                    <span className={`text-xs px-2 py-1 rounded-full font-medium ${
                        product.stock > 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                    }`}>
                        {product.stock > 0 ? `In Stock (${product.stock})` : 'Out of Stock'}
                    </span>
                </div>

                {/* Add to Cart Button */}
                <button
                    onClick={handleAddToCart}
                    disabled={product.stock === 0}
                    className={`w-full py-2 rounded-lg text-white font-bold transition-colors shadow-md ${
                        product.stock > 0
                        ? 'bg-indigo-500 hover:bg-indigo-700'
                        : 'bg-gray-400 cursor-not-allowed'
                    }`}
                >
                    {product.stock > 0 ? 'Add to Cart' : 'View Details'}
                </button>
            </div>
        </Link>
    );
};

export default ProductCard;
