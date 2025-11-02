import React, { useState, useEffect, useContext } from 'react';
import { Link, Navigate } from 'react-router-dom';
import OrderService from '../../api/OrderService';
import { AuthContext } from '../../context/AuthContext';
import toast from 'react-hot-toast';

const OrderHistoryPage = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const { currentUser } = useContext(AuthContext);

    // Redirect to login if user is not logged in (though ProtectedRoute handles this, it's good practice)
    if (!currentUser) {
        toast.error("Please log in to view your order history.");
        return <Navigate to="/login" replace />;
    }

    useEffect(() => {
        setLoading(true);
        // Note: We use getUserOrders from OrderService, which should use the secured /api/orders endpoint
        OrderService.getUserOrders()
            .then(response => {
                // Assuming the backend returns an array of orders in response.data,
                // and they are sorted by date descending (newest first)
                setOrders(response.data);
                setLoading(false);
            })
            .catch(err => {
                setLoading(false);
                // The Axios interceptor handles the general error toast
                console.error("Failed to fetch order history:", err);
                setOrders([]);
            });
    }, []);

    if (loading) {
        return <div className="text-center py-20 text-xl">Loading order history...</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-8 bg-white shadow-xl rounded-lg">
            <h1 className="text-4xl font-bold mb-8 text-center text-indigo-700">Your Order History</h1>

            {orders.length === 0 ? (
                <div className="text-center p-10 border border-gray-200 rounded-lg">
                    <p className="text-lg text-gray-600 mb-4">You have not placed any orders yet.</p>
                    <Link to="/products" className="text-indigo-600 hover:underline font-semibold">Start Shopping &rarr;</Link>
                </div>
            ) : (
                <div className="space-y-6">
                    {orders.map((order) => (
                        <div key={order.id} className="bg-gray-50 p-6 rounded-lg shadow-md border-l-4 border-indigo-500">

                            <div className="flex justify-between items-center border-b pb-3 mb-3">
                                <h2 className="text-xl font-semibold text-gray-800">Order #{order.id}</h2>
                                <span className={`px-3 py-1 text-sm rounded-full font-medium ${order.status === 'DELIVERED' ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700'}`}>
                                    {order.status || 'PROCESSING'}
                                </span>
                            </div>

                            <div className="text-sm text-gray-600 space-y-1">
                                <p>Date Placed: <span className="font-medium">{new Date(order.orderDate).toLocaleDateString()}</span></p>
                                <p>Total: <span className="font-bold text-green-600">${order.totalAmount ? order.totalAmount.toFixed(2) : 'N/A'}</span></p>
                                <p>Shipping To: {order.shippingAddress?.addressLine1 || 'Address unavailable'}</p>
                            </div>

                            {/* Simple list of items in the order */}
                            <ul className="mt-4 pt-4 border-t border-gray-200 space-y-1 text-sm text-gray-700">
                                {order.orderItems && order.orderItems.slice(0, 3).map((item, index) => (
                                    <li key={index}>
                                        {item.quantity}x {item.productName || "Product"} (${item.priceAtPurchase ? item.priceAtPurchase.toFixed(2) : 'N/A'} ea.)
                                    </li>
                                ))}
                                {order.orderItems && order.orderItems.length > 3 && <li>... and {order.orderItems.length - 3} more items.</li>}
                            </ul>

                            {/* Placeholder for viewing full details */}
                            <button disabled className="mt-4 text-indigo-400 text-sm font-semibold cursor-not-allowed">
                                View Full Details (Coming Soon)
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default OrderHistoryPage;
```eof

With the primary user flow complete, let's look at the remaining major features:

1.  **Admin Panel Functionality**: Implementing the forms and API calls within the `AdminDashboard.jsx` to create, update, and delete products.
2.  **Category Filtering**: Adding a category list and logic to filter the products on `ProductListPage.jsx`.

Would you like to start by implementing the **Admin Panel's product creation functionality**?