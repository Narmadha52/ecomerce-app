import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../../context/CartContext';
import ImagePlaceholder from '../../components/common/ImagePlaceholder';
import toast from 'react-hot-toast';

const CartPage = () => {
    const {
        cartItems,
        totalItems,
        cartTotal,
        updateQuantity,
        removeItem
    } = useCart();

    const handleQuantityChange = (id, newQuantity) => {
        const quantity = parseInt(newQuantity);
        if (quantity >= 1) {
            updateQuantity(id, quantity);
        } else if (quantity === 0) {
            handleRemoveItem(id);
        }
    };

    const handleRemoveItem = (id) => {
        const item = cartItems.find(i => i.id === id);
        removeItem(id);
        toast.success(`${item.name} removed from cart.`, { icon: '🗑️' });
    };

    const isCartEmpty = cartItems.length === 0;

    return (
        <div className="max-w-6xl mx-auto p-4">
            <h1 className="text-4xl font-extrabold text-gray-900 mb-8 text-center">
                Your Shopping Cart ({totalItems})
            </h1>

            {isCartEmpty ? (
                <div className="text-center py-20 bg-white rounded-xl shadow-lg">
                    <p className="text-2xl text-gray-600 mb-4">Your cart is currently empty.</p>
                    <Link
                        to="/products"
                        className="text-indigo-600 font-semibold text-lg hover:text-indigo-800 transition-colors"
                    >
                        &larr; Start Shopping
                    </Link>
                </div>
            ) : (
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">

                    {/* --- Left Column: Cart Items List --- */}
                    <div className="lg:col-span-2 space-y-4">
                        {cartItems.map(item => (
                            <div key={item.id} className="flex items-center bg-white p-4 rounded-lg shadow-md border border-gray-100">

                                {/* Image */}
                                <div className="w-20 h-20 flex-shrink-0 mr-4">
                                    {item.imageUrl ? (
                                        <img
                                            src={item.imageUrl}
                                            alt={item.name}
                                            className="w-full h-full object-cover rounded-md"
                                        />
                                    ) : (
                                        <ImagePlaceholder text="Product" />
                                    )}
                                </div>

                                {/* Details and Quantity Control */}
                                <div className="flex-grow">
                                    <Link
                                        to={`/products/${item.id}`}
                                        className="text-lg font-semibold text-gray-800 hover:text-indigo-600 transition-colors line-clamp-1"
                                    >
                                        {item.name}
                                    </Link>
                                    <p className="text-sm text-gray-500">Price: ${item.price.toFixed(2)}</p>

                                    <div className="flex items-center space-x-3 mt-2">
                                        <input
                                            type="number"
                                            min="0"
                                            value={item.quantity}
                                            onChange={(e) => handleQuantityChange(item.id, e.target.value)}
                                            className="w-16 p-1 border border-gray-300 rounded-md text-center"
                                        />
                                        <span className="text-gray-700">x ${item.price.toFixed(2)}</span>
                                    </div>
                                </div>

                                {/* Subtotal and Remove Button */}
                                <div className="text-right flex flex-col justify-between items-end h-full">
                                    <p className="text-lg font-bold text-gray-800">
                                        ${(item.price * item.quantity).toFixed(2)}
                                    </p>
                                    <button
                                        onClick={() => handleRemoveItem(item.id)}
                                        className="text-red-500 hover:text-red-700 text-sm mt-1"
                                    >
                                        Remove
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* --- Right Column: Order Summary & Checkout --- */}
                    <div className="lg:col-span-1 bg-white p-6 rounded-lg shadow-xl sticky top-24 h-fit">
                        <h2 className="text-2xl font-bold mb-4 border-b pb-2 text-gray-800">Order Summary</h2>

                        <div className="space-y-3 mb-6">
                            <div className="flex justify-between text-gray-600">
                                <span>Total Items:</span>
                                <span className="font-medium">{totalItems}</span>
                            </div>
                            <div className="flex justify-between text-gray-600">
                                <span>Subtotal:</span>
                                <span className="font-medium">${cartTotal.toFixed(2)}</span>
                            </div>
                            <div className="flex justify-between text-gray-600">
                                <span>Shipping (Flat Rate):</span>
                                <span className="font-medium">$5.00</span>
                            </div>
                        </div>

                        <div className="flex justify-between text-2xl font-bold border-t pt-4">
                            <span>Order Total:</span>
                            <span className="text-indigo-600">${(cartTotal + 5.00).toFixed(2)}</span>
                        </div>

                        <Link
                            to="/checkout"
                            className="block w-full text-center bg-green-600 text-white py-3 mt-6 rounded-lg font-bold text-lg hover:bg-green-700 transition-colors shadow-md"
                        >
                            Proceed to Checkout
                        </Link>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CartPage;
```eof

The Cart Page is now fully functional, visually appealing, and ready to send users to the protected checkout route!

The next crucial step is to enable users to complete an actual purchase.

Would you like to move on to **implementing the checkout page logic** (`CheckoutPage.jsx`), which involves gathering shipping/payment information and calling the `OrderService.createOrder` function?