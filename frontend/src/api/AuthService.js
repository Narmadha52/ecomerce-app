import axios from 'axios';

// The base URL is configured in vite.config.js to proxy to http://localhost:8080
const API_URL = '/api/auth/';

const register = (username, email, password) => {
  return axios.post(API_URL + 'register', {
    username,
    email,
    password,
  });
};

const login = (username, password) => {
  return axios.post(API_URL + 'login', {
    username,
    password,
  })
  .then((response) => {
    // Check if the response contains the JWT token and user info
    if (response.data.accessToken) {
      // Store user data (including JWT) in Local Storage
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  });
};

const logout = () => {
  // Remove user data from Local Storage
  localStorage.removeItem('user');
};

const getCurrentUser = () => {
  // Retrieve user data from Local Storage
  return JSON.parse(localStorage.getItem('user'));
};

const AuthService = {
  register,
  login,
  logout,
  getCurrentUser,
};

export default AuthService;