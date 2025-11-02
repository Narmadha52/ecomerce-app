import React, { useState, useEffect, useContext } from 'react';
import UserService from '../../api/UserService';
import { AuthContext } from '../../context/AuthContext';
import toast from 'react-hot-toast';

const ProfilePage = () => {
    const { currentUser, setCurrentUser } = useContext(AuthContext);

    // State to hold the editable user data
    const [profileData, setProfileData] = useState({
        firstName: '',
        lastName: '',
        email: '', // Email is typically read-only or requires extra validation
        // Add shipping details here if your backend stores them directly on the user entity
        // e.g., addressLine1: '', city: '',
    });
    const [loading, setLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);

    // 1. Fetch User Data on Mount
    useEffect(() => {
        if (currentUser) {
            setLoading(true);
            UserService.getMyDetails()
                .then(response => {
                    const data = response.data;
                    setProfileData({
                        firstName: data.firstName || '',
                        lastName: data.lastName || '',
                        email: data.email || '',
                        // Populate other fields if available in the response
                    });
                })
                .catch(error => {
                    console.error("Error fetching profile details:", error);
                    toast.error("Failed to load profile details.");
                })
                .finally(() => {
                    setLoading(false);
                });
        }
    }, [currentUser]);

    // Handler for input changes
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setProfileData(prev => ({ ...prev, [name]: value }));
    };

    // Handler for form submission
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (isSubmitting) return;
        setIsSubmitting(true);
        toast.loading("Updating profile...", { id: 'profile_update' });

        try {
            // Only send fields that the user is allowed to update
            const updatePayload = {
                firstName: profileData.firstName,
                lastName: profileData.lastName,
                // Do NOT send email if it's not meant to be updated via this form
            };

            const response = await UserService.updateMyDetails(updatePayload);

            // Update local state and AuthContext
            const updatedUser = response.data;
            setProfileData(prev => ({
                ...prev,
                firstName: updatedUser.firstName,
                lastName: updatedUser.lastName
            }));

            // Crucially, update the AuthContext so the Header reflects new name
            setCurrentUser(prev => ({
                ...prev,
                firstName: updatedUser.firstName,
                lastName: updatedUser.lastName
            }));

            toast.success("Profile updated successfully!", { id: 'profile_update' });

        } catch (error) {
            toast.error("Failed to update profile. Check form data.", { id: 'profile_update' });
            console.error("Profile update error:", error);
        } finally {
            setIsSubmitting(false);
        }
    };

    if (!currentUser) {
        // Should be protected by ProtectedRoute, but a fallback is good
        return <div className="text-center py-20 text-red-500">Not authenticated.</div>;
    }

    if (loading) {
        return <div className="text-center py-20 text-xl">Loading profile...</div>;
    }

    return (
        <div className="max-w-xl mx-auto p-8 bg-white shadow-2xl rounded-xl">
            <h1 className="text-4xl font-bold mb-8 text-center text-indigo-700 border-b pb-4">
                My Profile
            </h1>

            <div className="mb-6 text-center">
                <p className="text-xl font-semibold">User: {currentUser.email}</p>
                <p className="text-sm text-gray-500">Roles: {currentUser.roles.join(', ')}</p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-6">

                {/* First and Last Name */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <input
                        type="text" name="firstName" placeholder="First Name" required
                        value={profileData.firstName} onChange={handleInputChange}
                        className="input-profile"
                    />
                    <input
                        type="text" name="lastName" placeholder="Last Name" required
                        value={profileData.lastName} onChange={handleInputChange}
                        className="input-profile"
                    />
                </div>

                {/* Email (Read-Only) */}
                <div>
                    <input
                        type="email" name="email" placeholder="Email Address"
                        value={profileData.email} disabled
                        className="input-profile bg-gray-100 cursor-not-allowed"
                    />
                    <p className="text-xs text-gray-500 mt-1">Email address cannot be changed here.</p>
                </div>

                {/* TODO: Add section here for updating Shipping/Billing Address if needed */}

                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="w-full bg-indigo-600 text-white py-3 rounded-lg font-bold text-lg hover:bg-indigo-700 transition-colors disabled:bg-gray-400"
                >
                    {isSubmitting ? 'Saving...' : 'Update Profile'}
                </button>
            </form>

            <style jsx="true">{`
                .input-profile {
                    @apply w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-shadow duration-200;
                }
            `}</style>
        </div>
    );
};

export default ProfilePage;