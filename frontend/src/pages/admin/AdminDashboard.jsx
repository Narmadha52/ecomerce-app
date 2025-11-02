import React, { useState, useEffect, useCallback } from 'react';
import ProductService from '../../api/ProductService';
import toast from 'react-hot-toast';

// Initial state for a new product
const initialProductState = {
    name: '',
    description: '',
    price: 0.01,
    stock: 0,
    imageUrl: '',
    category: '',
};

const AdminDashboard = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [newProduct, setNewProduct] = useState(initialProductState);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null); // Product being edited

    // --- Data Fetching ---
    const fetchProducts = useCallback(async () => {
        setLoading(true);
        try {
            const response = await ProductService.getAllProducts();
            setProducts(response.data);
        } catch (error) {
            console.error("Failed to fetch products for admin:", error);
            // Axios interceptor will handle toast notification
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchProducts();
    }, [fetchProducts]);

    // --- Handler for Create/Edit Form ---
    const handleInputChange = (e) => {
        const { name, value, type } = e.target;
        const targetState = editingProduct ? setEditingProduct : setNewProduct;

        targetState(prev => ({
            ...prev,
            [name]: type === 'number' ? parseFloat(value) : value
        }));
    };

    // --- Product Creation Function ---
    const handleCreateProduct = async (e) => {
        e.preventDefault();

        if (isSubmitting) return;
        setIsSubmitting(true);
        toast.loading("Creating product...", { id: 'product_action' });

        try {
            await ProductService.createProduct(newProduct);
            toast.success(`${newProduct.name} created successfully!`, { id: 'product_action' });
            setNewProduct(initialProductState); // Clear form
            fetchProducts(); // Refresh list

        } catch (error) {
            toast.error("Failed to create product.", { id: 'product_action' });
            console.error("Product creation error:", error);
        } finally {
            setIsSubmitting(false);
        }
    };

    // --- Product Update Function ---
    const handleUpdateProduct = async (e) => {
        e.preventDefault();
        if (isSubmitting || !editingProduct?.id) return;

        setIsSubmitting(true);
        toast.loading(`Updating ${editingProduct.name}...`, { id: 'product_action' });

        try {
            await ProductService.updateProduct(editingProduct.id, editingProduct);
            toast.success(`${editingProduct.name} updated successfully!`, { id: 'product_action' });
            setEditingProduct(null); // Exit edit mode
            fetchProducts(); // Refresh list

        } catch (error) {
            toast.error("Failed to update product.", { id: 'product_action' });
            console.error("Product update error:", error);
        } finally {
            setIsSubmitting(false);
        }
    };

    // --- Product Deletion Function ---
    const handleDeleteProduct = async (productId, productName) => {
        if (!window.confirm(`Are you sure you want to delete product: ${productName}?`)) {
            return;
        }

        toast.loading(`Deleting ${productName}...`, { id: 'product_delete' });

        try {
            await ProductService.deleteProduct(productId);
            toast.success(`${productName} deleted successfully!`, { id: 'product_delete' });
            fetchProducts(); // Refresh list
        } catch (error) {
            toast.error("Failed to delete product.", { id: 'product_delete' });
            console.error("Product deletion error:", error);
        }
    };

    // --- Render Form based on mode (Create or Edit) ---
    const renderProductForm = () => {
        const productData = editingProduct || newProduct;
        const isEditMode = !!editingProduct;
        const handleSubmit = isEditMode ? handleUpdateProduct : handleCreateProduct;
        const title = isEditMode ? `Edit Product: ${editingProduct.name}` : "Create New Product";

        return (
            <div className="bg-white p-6 rounded-xl shadow-lg border border-red-200">
                <h2 className="text-2xl font-semibold mb-6 text-gray-800">{title}</h2>
                <form onSubmit={handleSubmit} className="space-y-4">

                    {/* Input Fields (reused for both modes) */}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <input type="text" name="name" placeholder="Product Name" value={productData.name} onChange={handleInputChange} required className="input-admin" />
                        <input type="text" name="category" placeholder="Category" value={productData.category} onChange={handleInputChange} required className="input-admin" />
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                        <input type="number" name="price" placeholder="Price" value={productData.price} onChange={handleInputChange} required min="0.01" step="0.01" className="input-admin" />
                        <input type="number" name="stock" placeholder="Stock Quantity" value={productData.stock} onChange={handleInputChange} required min="0" className="input-admin" />
                    </div>
                    <input type="url" name="imageUrl" placeholder="Image URL" value={productData.imageUrl} onChange={handleInputChange} className="input-admin" />
                    <textarea name="description" placeholder="Full Product Description" value={productData.description} onChange={handleInputChange} required rows="3" className="input-admin resize-none" />

                    <div className="flex space-x-4 pt-2">
                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className="flex-grow bg-red-600 text-white py-3 rounded-lg font-bold text-lg hover:bg-red-700 transition-colors disabled:bg-red-300"
                        >
                            {isSubmitting ? 'Processing...' : (isEditMode ? 'Save Changes' : 'Create Product')}
                        </button>
                        {isEditMode && (
                            <button
                                type="button"
                                onClick={() => setEditingProduct(null)}
                                className="w-1/3 bg-gray-500 text-white py-3 rounded-lg font-bold hover:bg-gray-600 transition-colors"
                            >
                                Cancel
                            </button>
                        )}
                    </div>
                </form>
            </div>
        );
    };

    if (loading) {
        return <div className="text-center py-20 text-xl">Loading Admin Data...</div>;
    }

    return (
        <div className="max-w-6xl mx-auto p-8 space-y-10">
            <h1 className="text-4xl font-bold mb-4 text-center text-red-700 border-b pb-4">
                Admin Product Management
            </h1>

            {/* Product Form (Create or Edit Mode) */}
            {renderProductForm()}

            {/* Product List Table (Read/Delete Actions) */}
            <div className="bg-white p-6 rounded-xl shadow-2xl">
                <h2 className="text-2xl font-semibold mb-6 text-gray-800">Existing Products ({products.length})</h2>

                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="table-header w-1/4">Name</th>
                                <th className="table-header">Category</th>
                                <th className="table-header">Price</th>
                                <th className="table-header">Stock</th>
                                <th className="table-header w-1/6 text-right">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                            {products.map((product) => (
                                <tr key={product.id} className="hover:bg-red-50 transition-colors">
                                    <td className="table-data font-medium text-gray-900">{product.name}</td>
                                    <td className="table-data">{product.category}</td>
                                    <td className="table-data">${product.price.toFixed(2)}</td>
                                    <td className="table-data">{product.stock}</td>
                                    <td className="table-data text-right space-x-2">
                                        <button
                                            onClick={() => setEditingProduct(product)}
                                            className="text-indigo-600 hover:text-indigo-800 font-semibold text-sm"
                                        >
                                            Edit
                                        </button>
                                        <button
                                            onClick={() => handleDeleteProduct(product.id, product.name)}
                                            className="text-red-600 hover:text-red-800 font-semibold text-sm"
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {/* Custom Tailwind input/table styles */}
            <style jsx="true">{`
                .input-admin {
                    @apply w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500 transition-shadow duration-200;
                }
                .table-header {
                    @apply px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider;
                }
                .table-data {
                    @apply px-6 py-4 whitespace-nowrap text-sm text-gray-700;
                }
            `}</style>
        </div>
    );
};

export default AdminDashboard;
