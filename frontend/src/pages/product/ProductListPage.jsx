import React, { useState, useEffect, useCallback } from 'react';
import ProductService from '../../api/ProductService';
import ProductCard from '../../components/product/ProductCard';
import toast from 'react-hot-toast';

// Utility for basic debouncing to prevent excessive API calls
const debounce = (func, delay) => {
    let timeoutId;
    const debounced = (...args) => {
        if (timeoutId) clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            func.apply(null, args);
        }, delay);
    };
    // Allows cleanup function to cancel pending calls
    debounced.cancel = () => clearTimeout(timeoutId);
    return debounced;
};

const ProductListPage = () => {
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('');
    const [isSearching, setIsSearching] = useState(false);

    // --- Category Fetching ---
    useEffect(() => {
        ProductService.getAllCategories()
            .then(response => {
                setCategories(response.data);
            })
            .catch(error => {
                console.error("Error fetching categories:", error);
                // Interceptor handles the toast
            });
    }, []);


    // --- Data Fetching Logic (Search & Filter) ---
    const fetchData = useCallback(async (currentSearchTerm, currentCategory) => {
        setLoading(true);
        // Set searching state if any filter is active
        setIsSearching(currentSearchTerm.length > 0 || currentCategory.length > 0);

        const query = {};
        if (currentSearchTerm) {
            query.search = currentSearchTerm;
        }
        if (currentCategory) {
            query.category = currentCategory;
        }

        try {
            const response = await ProductService.getAllProducts(query);
            setProducts(response.data);

            // Notification for no results found
            if ((currentSearchTerm || currentCategory) && response.data.length === 0) {
                 toast.error(`No results found for your current filter/search.`);
            }
        } catch (error) {
            console.error("Error fetching products:", error);
        } finally {
            setLoading(false);
            setIsSearching(false);
        }
    }, []);

    // Debounced version of the fetch function (only for search term changes)
    const debouncedFetch = useCallback(debounce(fetchData, 500), [fetchData]);

    // --- useEffect to trigger data fetching ---
    useEffect(() => {
        // Decide whether to use debounce (for search term) or immediate fetch (for category change)
        if (searchTerm) {
            debouncedFetch(searchTerm, selectedCategory);
        } else {
            fetchData(searchTerm, selectedCategory);
        }

        return () => debouncedFetch.cancel && debouncedFetch.cancel();
    }, [searchTerm, selectedCategory, debouncedFetch, fetchData]);


    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value);
    };

    const handleCategoryChange = (category) => {
        // Reset search term when switching categories for a clean filter state
        setSearchTerm('');
        setSelectedCategory(category);
    };

    if (loading && !isSearching) {
        return <div className="text-center py-20 text-xl">Loading products...</div>;
    }

    return (
        <div className="min-h-screen p-4">
            <h1 className="text-4xl font-extrabold text-gray-900 mb-8 text-center">
                Product Catalog
            </h1>

            <div className="flex flex-col lg:flex-row gap-6">

                {/* --- Left Column: Category Filter (Sticky) --- */}
                <div className="lg:w-1/4 bg-white p-6 rounded-xl shadow-lg h-fit lg:sticky lg:top-24">
                    <h2 className="text-xl font-bold mb-4 border-b pb-2 text-gray-800">
                        Categories
                    </h2>
                    <ul className="space-y-2">
                        {/* All Products button */}
                        <li>
                            <button
                                onClick={() => handleCategoryChange('')}
                                className={`w-full text-left py-2 px-3 rounded-lg font-semibold transition-colors ${
                                    selectedCategory === ''
                                    ? 'bg-indigo-600 text-white'
                                    : 'text-gray-700 hover:bg-gray-100'
                                }`}
                            >
                                All Products
                            </button>
                        </li>

                        {/* Category List */}
                        {categories.map(category => (
                            <li key={category}>
                                <button
                                    onClick={() => handleCategoryChange(category)}
                                    className={`w-full text-left py-2 px-3 rounded-lg transition-colors ${
                                        selectedCategory === category
                                        ? 'bg-indigo-100 text-indigo-700 font-semibold'
                                        : 'text-gray-700 hover:bg-gray-100'
                                    }`}
                                >
                                    {category}
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>

                {/* --- Right Column: Search Bar and Product Grid --- */}
                <div className="lg:w-3/4">
                    {/* Search Bar */}
                    <div className="mb-8 p-4 bg-white rounded-xl shadow-md">
                        <input
                            type="text"
                            placeholder="Search products by name or description..."
                            value={searchTerm}
                            onChange={handleSearchChange}
                            className="w-full p-3 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-indigo-500 transition-colors"
                            aria-label="Search products"
                        />
                    </div>

                    {/* Product Grid */}
                    <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-8">

                        {loading && isSearching && (
                            <div className="col-span-full text-center py-12 bg-white rounded-xl shadow-lg">
                                <p className="text-2xl text-indigo-500">Filtering products...</p>
                            </div>
                        )}

                        {!loading && products.length > 0 ? (
                            products.map(product => (
                                <ProductCard key={product.id} product={product} />
                            ))
                        ) : !loading && (
                            <div className="col-span-full text-center py-12 bg-white rounded-xl shadow-lg">
                                <p className="text-2xl text-gray-500">No products found.</p>
                                <p className="text-md text-gray-400 mt-2">
                                    Try adjusting your search or category filter.
                                </p>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProductListPage;
