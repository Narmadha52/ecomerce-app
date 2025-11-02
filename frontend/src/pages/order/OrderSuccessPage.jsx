import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

const OrderSuccessPage = () => {
    const [orderId, setOrderId] = useState(null); // Simulated Order ID
    const [isLoading, setIsLoading] = useState(true);

    // Simulate fetching the new Order ID or receiving it from state/session storage
    useEffect(() => {
        // In a real application, the checkout component would pass the actual
        // returned order ID via URL state or context, but here we simulate it.
        const mockOrderId = "ABC-" + Math.floor(Math.random() * 100000);

        // Use a slight delay to simulate processing confirmation
        const timer = setTimeout(() => {
            setOrderId(mockOrderId);
            setIsLoading(false);
        }, 500);

        return () => clearTimeout(timer);
    }, []);

    return (
        <div className="max-w-xl mx-auto p-8 my-12 bg-white rounded-xl shadow-2xl border-t-4 border-green-500 text-center">
            {isLoading ? (
                <div className="py-12">
                    <div className="spinner mb-4 mx-auto"></div>
                    <p className="text-xl text-gray-600">Finalizing your order...</p>
                    {/* CSS for a simple spinner */}
                    <style>{`
                        .spinner {
                            border: 4px solid rgba(0, 0, 0, 0.1);
                            border-left-color: #48bb78; /* Green */
                            border-radius: 50%;
                            width: 40px;
                            height: 40px;
                            animation: spin 1s linear infinite;
                        }
                        @keyframes spin {
                            to { transform: rotate(360deg); }
                        }
                    `}</style>
                </div>
            ) : (
                <>
                    <svg className="w-20 h-20 text-green-500 mx-auto mb-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>

                    <h1 className="text-4xl font-extrabold text-green-600 mb-3">
                        Order Placed Successfully!
                    </h1>

                    <p className="text-lg text-gray-700 mb-6">
                        Thank you for your purchase. Your order has been confirmed and is now being processed.
                    </p>

                    <div className="inline-block bg-gray-100 p-4 rounded-lg mb-8">
                        <p className="text-xl font-mono text-gray-800">
                            Reference ID: <span className="font-bold text-indigo-600">{orderId}</span>
                        </p>
                        <p className="text-sm text-gray-500 mt-1">Please keep this ID for future reference.</p>
                    </div>

                    <div className="flex justify-center space-x-4">
                        <Link
                            to="/orders"
                            className="bg-indigo-600 text-white font-semibold py-3 px-6 rounded-lg hover:bg-indigo-700 transition-colors shadow-md"
                        >
                            View Order History
                        </Link>
                        <Link
                            to="/products"
                            className="text-indigo-600 border border-indigo-600 font-semibold py-3 px-6 rounded-lg hover:bg-indigo-50 transition-colors"
                        >
                            Continue Shopping
                        </Link>
                    </div>
                </>
            )}
        </div>
    );
};

export default OrderSuccessPage;
