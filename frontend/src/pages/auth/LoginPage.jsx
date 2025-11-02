import React, { useState } from 'react';
import AuthService from '../../api/AuthService';
import { useNavigate, Link } from 'react-router-dom';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    setMessage('');

    if (!username || !password) {
        setMessage('Please enter both username and password.');
        return;
    }

    AuthService.login(username, password)
      .then(
        () => {
          // Redirect to the home page on successful login
          navigate('/');
          window.location.reload(); // Force app state reload (better handled with Context later)
        },
        (error) => {
          // Display error message (e.g., "Bad credentials")
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
          setMessage(resMessage || 'Login failed.');
        }
      );
  };

  return (
    <div className="auth-form-container">
      <h2>Login</h2>
      <form onSubmit={handleLogin} className="login-form">

        <label htmlFor="username">Username</label>
        <input
          type="text"
          id="username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        <label htmlFor="password">Password</label>
        <input
          type="password"
          id="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button type="submit">Login</button>
      </form>

      {message && (
        <div className="message">
          {message}
        </div>
      )}

      <p>
          Don't have an account? <Link to="/register">Register here.</Link>
      </p>
    </div>
  );
};

export default LoginPage;