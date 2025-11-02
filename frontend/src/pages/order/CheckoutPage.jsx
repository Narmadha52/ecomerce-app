import React, { useState, useContext, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useCart } from '../../context/CartContext';
import { AuthContext } from '../../context/AuthContext';
import OrderService from '../../api/OrderService';

const CheckoutPage = () => {
    const { cartItems, totalPrice, clearCart } = useCart();
    const { currentUser } = useContext(AuthContext);
    const navigate = useNavigate();

    const [shippingAddress, setShippingAddress] = useState('');
    const [paymentMethod, setPaymentMethod] = useState('Credit Card');
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    // Redirect unauthenticated users or empty carts
    useEffect(() => {
        if (!currentUser) {
            navigate('/login?redirect=/checkout');
        }
        if (cartItems.length === 0) {
            setMessage('Your cart is empty. Please add products before checking out.');
            // navigate('/cart'); // Optional: redirect back to cart
        }
    }, [currentUser, cartItems.length, navigate]);

    if (!currentUser || cartItems.length === 0) {
        // Render a message while the redirect is happening
        return (
            <div className="text-center py-20 text-xl text-gray-700">
                {message || 'Redirecting...'}
            </div>
        );
    }

    // Convert cart items into the format the backend expects (id, quantity)
    const orderItemsPayload = cartItems.map(item => ({
        productId: item.id,
        quantity: item.quantity,
        priceAtPurchase: item.price // Store the price at the time of purchase
    }));

    const handleSubmitOrder = async (e) => {
        e.preventDefault();
        setMessage('');
        setLoading(true);

        const orderPayload = {
            userId: currentUser.id, // Assuming the backend requires the user ID
            items: orderItemsPayload,
            totalAmount: totalPrice,
            shippingAddress: shippingAddress || 'Default Address, City, Country',
            paymentMethod: paymentMethod,
        };

        try {
            const response = await OrderService.createOrder(orderPayload);

            // Success: clear the cart and navigate to a success page
            clearCart();
            setLoading(false);
            navigate('/order-success', { state: { orderId: response.data.id } });

        } catch (error) {
            setLoading(false);
            console.error('Order Submission Error:', error);
            const errorMessage = error.response?.data?.message || error.message || 'Order failed due to a server error.';
            setMessage(errorMessage);
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-8 bg-white shadow-xl rounded-lg">
            <h1 className="text-3xl font-bold mb-8 text-center text-gray-800">Complete Your Order</h1>

            <form onSubmit={handleSubmitOrder} className="grid grid-cols-1 md:grid-cols-3 gap-8">

                {/* Shipping and Payment (2/3 width) */}
                <div className="md:col-span-2 space-y-6">

                    <div className="bg-indigo-50 p-6 rounded-lg shadow-inner">
                        <h2 className="text-xl font-semibold mb-4 border-b pb-2 text-indigo-700">1. Shipping Information</h2>

                        <label className="block mb-2 font-medium text-gray-700">Delivery Address</label>
                        <textarea
                            value={shippingAddress}
                            onChange={(e) => setShippingAddress(e.target.value)}
                            placeholder="Enter your full address"
                            required
                            className="w-full p-3 border border-indigo-300 rounded-lg focus:ring-2 focus:ring-indigo-500 transition duration-150"
                            rows="3"
                        ></textarea>
                    </div>

                    <div className="bg-green-50 p-6 rounded-lg shadow-inner">
                        <h2 className="text-xl font-semibold mb-4 border-b pb-2 text-green-700">2. Payment Method</h2>

                        <div className="flex space-x-6">
                            <label className="flex items-center space-x-2">
                                <input
                                    type="radio"
                                    name="payment"
                                    value="Credit Card"
                                    checked={paymentMethod === 'Credit Card'}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                    className="text-green-600 focus:ring-green-500"
                                />
                                <span>Credit Card</span>
                            </label>
                            <label className="flex items-center space-x-2">
                                <input
                                    type="radio"
                                    name="payment"
                                    value="PayPal"
                                    checked={paymentMethod === 'PayPal'}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                    className="text-green-600 focus:ring-green-500"
                                />
                                <span>PayPal</span>
                            </label>
                        </div>
                        {/* Note: Real world would require additional payment form fields */}
                    </div>
                </div>

                {/* Order Summary (1/3 width) */}
                <div className="md:col-span-1 p-6 bg-gray-100 rounded-lg shadow-md h-fit">
                    <h2 className="text-2xl font-bold mb-4 border-b pb-3 text-gray-800">Summary</h2>

                    <div className="space-y-3">
                        <div className="flex justify-between text-gray-600">
                            <span>Items ({cartItems.length}):</span>
                            <span className="font-semibold">${(totalPrice).toFixed(2)}</span>
                        </div>
                        <div className="flex justify-between text-gray-600 border-b pb-3">
                            <span>Shipping:</span>
                            <span className="font-semibold text-green-500">Free</span>
                        </div>
                        <div className="flex justify-between text-2xl font-bold text-gray-900 pt-3">
                            <span>Total:</span>
                            <span>${totalPrice.toFixed(2)}</span>
                        </div>
                    </div>

                    {message && (
                        <div className={`mt-4 p-3 rounded-lg text-sm ${message.includes('success') ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                            {message}
                        </div>
                    )}

                    <button
                        type="submit"
                        disabled={loading || cartItems.length === 0}
                        className="mt-6 w-full bg-indigo-600 text-white text-lg py-3 rounded-xl hover:bg-indigo-700 transition-colors duration-200 disabled:bg-indigo-400 disabled:cursor-not-allowed"
                    >
                        {loading ? 'Processing Order...' : 'Place Order Now'}
                    </button>

                    <Link to="/cart" className="mt-4 block w-full text-center text-sm text-gray-500 hover:text-gray-800">
                        &larr; Back to Cart
                    </Link>
                </div>
            </form>
        </div>
    );
};

export default CheckoutPage;


Since the checkout process finishes with a redirect, we should quickly implement the success page.

## 3. ✨ Create Order Success Page

Create a new file **`C:\ecommerce-project\frontend\src\pages\order\OrderSuccessPage.jsx`** (you will need to update `App.jsx` afterwards to include the route).


http://googleusercontent.com/immersive_entry_chip/0

## 4. 🔗 Update `App.jsx` for New Route

Finally, open **`C:\ecommerce-project\frontend\src\App.jsx`** and add the import and the new route for the success page.


http://googleusercontent.com/immersive_entry_chip/1

The core e-commerce workflow (Auth, Products, Cart, Checkout) is now complete on the frontend!

Would you like to move on to **implementing product management for the Admin Dashboard** (allowing authenticated users with the ADMIN role to create, update, and delete products)?