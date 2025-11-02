import React from 'react';
import { Link } from 'react-router-dom';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="footer-area">
      <div className="container mx-auto p-6 flex flex-col md:flex-row justify-between items-center text-sm">

        {/* Copyright Section */}
        <div className="text-gray-400 mb-4 md:mb-0">
          &copy; {currentYear} E-Commerce App. All rights reserved.
        </div>

        {/* Links Section */}
        <div className="space-x-4">
          {/* These are placeholder links, feel free to update them */}
          <Link to="/about" className="text-gray-300 hover:text-white transition-colors">About Us</Link>
          <Link to="/contact" className="text-gray-300 hover:text-white transition-colors">Contact</Link>
          <Link to="/terms" className="text-gray-300 hover:text-white transition-colors">Terms of Service</Link>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
