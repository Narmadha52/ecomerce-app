import axios from 'axios';

// Base URL for the product API endpoints on the Spring Boot backend
const PRODUCT_API_URL = '/api/products';

/**
 * Fetches all products, optionally supporting search/filter query parameters.
 * @param {object} query - Optional object containing search parameters (e.g., { search: 'laptop', category: 'Electronics' })
 */
const getAllProducts = (query = {}) => {
    // Convert the query object into a URL search string
    const params = new URLSearchParams(query).toString();

    // Construct the final URL
    const url = params ? `${PRODUCT_API_URL}?${params}` : PRODUCT_API_URL;

    return axios.get(url);
};

/**
 * Fetches a single product by its ID.
 * @param {number} id - The ID of the product to retrieve.
 */
const getProductById = (id) => {
    return axios.get(PRODUCT_API_URL + `/${id}`);
};

/**
 * Fetches a list of all unique categories available in the store.
 */
const getAllCategories = () => {
    return axios.get(PRODUCT_API_URL + '/categories');
};

/**
 * Fetches a list of products related to the specified product ID.
 * Assumes backend endpoint: GET /api/products/{id}/related
 * @param {number} id - The ID of the primary product.
 */
const getRelatedProducts = (id) => {
    return axios.get(PRODUCT_API_URL + `/${id}/related`);
};

/**
 * Creates a new product. Requires admin authorization.
 */
const createProduct = (productData) => {
    return axios.post(PRODUCT_API_URL, productData);
};

/**
 * Updates an existing product.
 */
const updateProduct = (id, productData) => {
    return axios.put(PRODUCT_API_URL + `/${id}`, productData);
};

/**
 * Deletes a product by ID.
 */
const deleteProduct = (id) => {
    return axios.delete(PRODUCT_API_URL + `/${id}`);
};


const ProductService = {
    getAllProducts,
    getProductById,
    getAllCategories,
    getRelatedProducts, // <-- Included for completeness
    createProduct,
    updateProduct,
    deleteProduct,
};

export default ProductService;