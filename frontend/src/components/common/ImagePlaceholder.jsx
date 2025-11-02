import React from 'react';

/**
 * Displays a clean, centered placeholder for images that are missing or fail to load.
 * @param {string} text - The name of the product or category to display in the placeholder.
 */
const ImagePlaceholder = ({ text = "Product Image" }) => {
    return (
        <div className="w-full h-full bg-gray-200 flex items-center justify-center rounded-lg shadow-inner border border-dashed border-gray-400 p-4">
            <div className="text-center text-gray-500">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-10 w-10 mx-auto mb-2 text-gray-400"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    strokeWidth={2}
                >
                    <path strokeLinecap="round" strokeLinejoin="round" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <p className="font-semibold text-lg">{text}</p>
                <p className="text-sm">Image Unavailable</p>
            </div>
        </div>
    );
};

export default ImagePlaceholder;
