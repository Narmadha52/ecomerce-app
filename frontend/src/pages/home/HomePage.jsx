import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import ProductService from '../../api/ProductService';
import ProductCard from '../../components/product/ProductCard';

const HomePage = () => {
    const [featuredProducts, setFeaturedProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Fetch all products and take a small subset for display as "featured"
        ProductService.getAllProducts()
            .then(response => {
                // Assume we display the first 8 products as featured
                setFeaturedProducts(response.data.slice(0, 8));
            })
            .catch(error => {
                console.error("Error fetching featured products:", error);
                // Continue rendering even if fetch fails
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    return (
        <div className="min-h-screen">

            {/* --- 1. Hero Section --- */}
            <header className="bg-indigo-600 text-white py-20 shadow-lg rounded-b-2xl">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
                    <h1 className="text-5xl font-extrabold tracking-tight sm:text-6xl mb-4">
                        Discover Your Next Favorite Thing
                    </h1>
                    <p className="mt-3 text-xl sm:text-2xl text-indigo-100 max-w-3xl mx-auto">
                        High quality goods, fast shipping, and exceptional customer service—all in one place.
                    </p>
                    <div className="mt-10">
                        <Link
                            to="/products"
                            className="inline-block bg-white text-indigo-600 border border-transparent rounded-full px-8 py-3 text-lg font-medium hover:bg-indigo-50 transition-colors shadow-xl transform hover:scale-105"
                        >
                            Shop All Products &rarr;
                        </Link>
                    </div>
                </div>
            </header>

            {/* --- 2. Featured Products Section --- */}
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
                <h2 className="text-3xl font-bold text-gray-900 mb-10 border-b-2 border-indigo-500 inline-block pb-2">
                    Featured Products
                </h2>

                {loading ? (
                    <div className="text-center py-10 text-lg text-gray-500">Loading amazing products...</div>
                ) : featuredProducts.length > 0 ? (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
                        {featuredProducts.map(product => (
                            <ProductCard key={product.id} product={product} />
                        ))}
                    </div>
                ) : (
                    <div className="text-center py-10 text-lg text-gray-500">No products available yet.</div>
                )}
            </main>

            {/* --- 3. Call to Action / Footer CTA --- */}
            <section className="bg-gray-100 py-12">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
                    <h3 className="text-2xl font-bold text-gray-800 mb-4">
                        Ready to join our community?
                    </h3>
                    <p className="text-lg text-gray-600 mb-6">
                        Log in or create an account to start tracking your favorites and orders.
                    </p>
                    <Link
                        to="/login"
                        className="inline-block bg-indigo-500 text-white rounded-lg px-6 py-3 font-semibold hover:bg-indigo-700 transition-colors shadow-md"
                    >
                        Sign In / Register
                    </Link>
                </div>
            </section>
        </div>
    );
};

export default HomePage;
