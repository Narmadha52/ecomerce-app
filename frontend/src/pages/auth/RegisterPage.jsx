import React, { useState } from 'react';
import AuthService from '../../api/AuthService';
import { useNavigate, Link } from 'react-router-dom';

const RegisterPage = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleRegister = (e) => {
    e.preventDefault();
    setMessage('');

    if (!username || !email || !password) {
        setMessage('Please fill in all fields.');
        return;
    }

    AuthService.register(username, email, password)
      .then(
        (response) => {
          // Success message from the backend (e.g., "User registered successfully!")
          setMessage(response.data.message);
          // Optional: redirect to login after a short delay
          setTimeout(() => {
              navigate('/login');
          }, 2000);
        },
        (error) => {
          // Handle specific error messages from the backend
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
          setMessage(resMessage || 'Registration failed.');
        }
      );
  };

  return (
    <div className="auth-form-container">
      <h2>Register</h2>
      <form onSubmit={handleRegister} className="register-form">

        <label htmlFor="username">Username</label>
        <input
          type="text"
          id="username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        <label htmlFor="email">Email</label>
        <input
          type="email"
          id="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
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

        <button type="submit">Register</button>
      </form>

      {message && (
        <div className="message">
          {message}
        </div>
      )}

      <p>
          Already have an account? <Link to="/login">Login here.</Link>
      </p>
    </div>
  );
};

export default RegisterPage;