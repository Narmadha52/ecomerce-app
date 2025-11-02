import React, { createContext, useState, useContext, useEffect } from 'react';

// 1. Create the Context object
export const CartContext = createContext();

// Hook for easy access to cart context (optional, but good practice)
export const useCart = () => {
  return useContext(CartContext);
};

// Key for local storage
const CART_STORAGE_KEY = 'ecomm_cart';

// 2. Create the Provider component
export const CartProvider = ({ children }) => {
  // Initialize cart state from local storage or an empty array
  const [cartItems, setCartItems] = useState(() => {
    try {
      const storedCart = localStorage.getItem(CART_STORAGE_KEY);
      return storedCart ? JSON.parse(storedCart) : [];
    } catch (error) {
      console.error("Error loading cart from storage:", error);
      return [];
    }
  });

  // 3. Update local storage whenever cartItems state changes
  useEffect(() => {
    localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(cartItems));
  }, [cartItems]);

  // Function to add an item to the cart
  const addToCart = (product, quantity = 1) => {
    setCartItems(prevItems => {
      const itemIndex = prevItems.findIndex(item => item.id === product.id);

      if (itemIndex > -1) {
        // Item exists, update quantity
        return prevItems.map((item, index) =>
          index === itemIndex
            ? { ...item, quantity: item.quantity + quantity }
            : item
        );
      } else {
        // Item is new, add it to the cart
        return [...prevItems, { ...product, quantity }];
      }
    });
  };

  // Function to remove an item entirely from the cart
  const removeItem = (productId) => {
    setCartItems(prevItems => prevItems.filter(item => item.id !== productId));
  };

  // Function to update the quantity of a specific item
  const updateQuantity = (productId, newQuantity) => {
    setCartItems(prevItems => {
      if (newQuantity <= 0) {
        return prevItems.filter(item => item.id !== productId);
      }
      return prevItems.map(item =>
        item.id === productId ? { ...item, quantity: newQuantity } : item
      );
    });
  };

  // Function to clear the entire cart (useful after checkout)
  const clearCart = () => {
    setCartItems([]);
  };

  // Calculate total price and total items
  const totalItems = cartItems.reduce((acc, item) => acc + item.quantity, 0);
  const totalPrice = cartItems.reduce((acc, item) => acc + item.price * item.quantity, 0);

  const value = {
    cartItems,
    totalItems,
    totalPrice,
    addToCart,
    removeItem,
    updateQuantity,
    clearCart,
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
};