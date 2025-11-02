import axios from 'axios';

const ORDER_API_URL = '/api/orders';

/**
 * Creates a new order. This is a protected endpoint and requires the user to be authenticated.
 * @param {object} orderData - The complete order details (items, shippingAddress, paymentMethod, etc.).
 */
const createOrder = (orderData) => {
    // This assumes the backend handles the current user context via the JWT token.
    return axios.post(ORDER_API_URL, orderData);
};

/**
 * Fetches the order history for the currently logged-in user.
 */
const getOrderHistory = () => {
    return axios.get(ORDER_API_URL);
};

const OrderService = {
    createOrder,
    getOrderHistory,
    // Add other order-related functions here later (e.g., getOrderDetailsById)
};

export default OrderService;
