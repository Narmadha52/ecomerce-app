import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../../context/CartContext';
import ImagePlaceholder from '../common/ImagePlaceholder';
import toast from 'react-hot-toast';

/**
 * Component to display a single product in the list view.
 * @param {object} product - The product data object from the API.
 */
const ProductCard = ({ product }) => {
    const { addToCart } = useCart();

    const handleAddToCart = () => {
        // We only add the basic information needed to identify the product
        addToCart({
            id: product.id,
            name: product.name,
            price: product.price,
            imageUrl: product.imageUrl,
            quantity: 1
        });
        toast.success(`${product.name} added to cart!`, {
            icon: '🛒',
        });
    };

    // Fallback for image URL if it's null or undefined
    const displayImage = product.imageUrl && product.imageUrl.startsWith('http')
        ? product.imageUrl
        : null;

    return (
        <div className="bg-white rounded-xl shadow-lg hover:shadow-2xl transition-all duration-300 overflow-hidden flex flex-col group">

            {/* Image or Placeholder */}
            <Link to={`/products/${product.id}`} className="block relative h-48 sm:h-56 overflow-hidden">
                {displayImage ? (
                    <img
                        src={displayImage}
                        alt={product.name}
                        className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                        onError={(e) => {
                            // Fallback if the image URL is invalid
                            e.target.onerror = null;
                            e.target.src = "https://placehold.co/600x400/CCCCCC/888888?text=Image+Error";
                        }}
                    />
                ) : (
                    <ImagePlaceholder text={product.name} />
                )}
            </Link>

            {/* Content Area */}
            <div className="p-4 flex flex-col flex-grow">

                {/* Title */}
                <h3 className="text-xl font-semibold mb-2 text-gray-800 line-clamp-2">
                    <Link to={`/products/${product.id}`} className="hover:text-indigo-600 transition-colors">
                        {product.name}
                    </Link>
                </h3>

                {/* Description (Optional) */}
                {product.shortDescription && (
                    <p className="text-sm text-gray-500 mb-3 flex-grow line-clamp-3">
                        {product.shortDescription}
                    </p>
                )}

                {/* Price */}
                <div className="mt-auto flex items-center justify-between pt-3 border-t border-gray-100">
                    <span className="text-2xl font-bold text-green-600">
                        ${product.price ? product.price.toFixed(2) : '0.00'}
                    </span>

                    {/* Add to Cart Button */}
                    <button
                        onClick={handleAddToCart}
                        className="bg-indigo-600 text-white px-4 py-2 rounded-full text-sm font-medium hover:bg-indigo-700 transition-colors shadow-md"
                        aria-label={`Add ${product.name} to cart`}
                    >
                        <svg className="w-5 h-5 inline mr-1 -mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4"></path></svg>
                        Add
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ProductCard;
