import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import ProductService from '../../api/ProductService';
import { useCart } from '../../context/CartContext';
import ImagePlaceholder from '../../components/common/ImagePlaceholder';
import ProductCard from '../../components/product/ProductCard'; // For related products
import toast from 'react-hot-toast';

const ProductDetailPage = () => {
    const { id } = useParams(); // Get product ID from URL parameter (e.g., /products/1)
    const { addToCart } = useCart();

    const [product, setProduct] = useState(null);
    const [relatedProducts, setRelatedProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [quantity, setQuantity] = useState(1);

    // --- Data Fetching ---
    useEffect(() => {
        // Function to fetch all necessary data
        const loadProductData = async () => {
            setLoading(true);
            setProduct(null); // Clear previous product when ID changes

            try {
                // 1. Fetch main product details
                const productResponse = await ProductService.getProductById(id);
                setProduct(productResponse.data);

                // 2. Fetch related products
                const relatedResponse = await ProductService.getRelatedProducts(id);
                setRelatedProducts(relatedResponse.data.slice(0, 4)); // Limit to 4 related products

            } catch (error) {
                console.error("Error fetching product data:", error);
                // Interceptor handles the toast for critical errors
            } finally {
                setLoading(false);
            }
        };

        loadProductData();
        // Effect re-runs whenever the 'id' parameter changes
    }, [id]);

    // --- Cart Handlers ---
    const handleAddToCart = () => {
        if (!product || product.stock === 0) return;

        addToCart({
            id: product.id,
            name: product.name,
            price: product.price,
            imageUrl: product.imageUrl,
            quantity: quantity
        });
        toast.success(`${quantity}x ${product.name} added to cart!`, {
            icon: '🛒',
        });
    };

    const displayImage = product?.imageUrl && product.imageUrl.startsWith('http')
        ? product.imageUrl
        : null;

    // --- Loading and Not Found States ---
    if (loading) {
        return <div className="text-center py-20 text-xl">Loading product details...</div>;
    }

    if (!product) {
        return (
            <div className="text-center py-20">
                <h1 className="text-4xl font-bold text-red-600">Product Not Found</h1>
                <p className="text-lg text-gray-600 mt-2">The product with ID {id} does not exist or an error occurred.</p>
                <Link to="/products" className="text-indigo-600 font-semibold mt-4 block hover:underline">
                    &larr; Back to Products
                </Link>
            </div>
        );
    }

    // --- Render Product Details ---
    return (
        <div className="p-4">
            <div className="max-w-6xl mx-auto bg-white shadow-xl rounded-lg mb-16">
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-10 p-6 lg:p-10">

                    {/* Image Section */}
                    <div className="lg:sticky lg:top-24 h-full">
                        {displayImage ? (
                            <img
                                src={displayImage}
                                alt={product.name}
                                className="w-full object-cover rounded-lg shadow-lg aspect-square"
                            />
                        ) : (
                            <div className="aspect-square">
                                <ImagePlaceholder text={product.name} />
                            </div>
                        )}
                    </div>

                    {/* Details and Purchase Section */}
                    <div className="p-4">
                        <h1 className="text-4xl font-extrabold text-gray-900 mb-4">{product.name}</h1>
                        <span className="text-sm font-medium text-indigo-600 uppercase tracking-wider">
                            {product.category || 'Uncategorized'}
                        </span>

                        <p className="text-4xl font-bold text-green-600 my-6">${product.price.toFixed(2)}</p>

                        <div className="mb-8 text-gray-700 leading-relaxed">
                            <h2 className="text-xl font-semibold mb-2 border-b pb-1">Product Description</h2>
                            <p className="whitespace-pre-wrap">{product.description || 'No detailed description available.'}</p>
                        </div>

                        {/* Quantity and Add to Cart */}
                        <div className="flex items-center space-x-4 mb-8">
                            <label htmlFor="quantity" className="text-lg font-medium">Quantity:</label>
                            <input
                                type="number"
                                id="quantity"
                                min="1"
                                max={product.stock}
                                value={quantity}
                                onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
                                className="w-20 p-2 border border-gray-300 rounded-md text-center"
                                disabled={product.stock === 0}
                            />

                            <button
                                onClick={handleAddToCart}
                                disabled={product.stock === 0}
                                className={`flex-grow py-3 rounded-lg text-white text-lg font-bold transition-colors shadow-md ${
                                    product.stock > 0
                                    ? 'bg-indigo-600 hover:bg-indigo-700'
                                    : 'bg-gray-400 cursor-not-allowed'
                                }`}
                            >
                                {product.stock > 0 ? 'Add to Cart' : 'Out of Stock'}
                            </button>
                        </div>

                        {/* Stock Info */}
                        <div className="text-sm pt-6 border-t">
                            {product.stock > 0 ? (
                                <p className="text-green-700 font-semibold text-md">In Stock: {product.stock} units</p>
                            ) : (
                                <p className="text-red-600 font-semibold text-md">Out of Stock</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            {/* --- Related Products Section --- */}
            {relatedProducts.length > 0 && (
                <section className="max-w-6xl mx-auto py-10">
                    <h2 className="text-3xl font-bold text-gray-900 mb-8 border-b-2 border-green-500 inline-block pb-2">
                        You Might Also Like
                    </h2>
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
                        {relatedProducts.map(related => (
                            // Use product.id in key and pass the product data
                            <ProductCard key={related.id} product={related} />
                        ))}
                    </div>
                </section>
            )}
        </div>
    );
};

export default ProductDetailPage;
```eof

We've covered all the main user features now! Would you like to **review the entire project structure** or are there any **minor refinements** you'd like to make to any of these existing pages?