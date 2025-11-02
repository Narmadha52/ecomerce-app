import axios from 'axios';

const USER_API_URL = '/api/users'; // Assuming this is the base for user management

/**
 * Fetches details of the currently authenticated user.
 * Assumes backend uses the JWT token to identify the user: GET /api/users/me
 */
const getMyDetails = () => {
    // Note: The backend typically uses the JWT in the header (handled by the interceptor)
    // to determine 'who' is requesting /me.
    return axios.get(USER_API_URL + '/me');
};

/**
 * Updates the details of the currently authenticated user.
 * Assumes backend endpoint: PUT /api/users/me
 * @param {object} userData - The fields to update (firstName, lastName, etc.)
 */
const updateMyDetails = (userData) => {
    return axios.put(USER_API_URL + '/me', userData);
};

const UserService = {
    getMyDetails,
    updateMyDetails,
    // Add other user-related functions here (e.g., changing password)
};

export default UserService;