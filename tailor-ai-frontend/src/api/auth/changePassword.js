
import {apiClient} from "../apiConfig.js";

/**
 * API request for changing user's password
 * takes an object that has
 * email, old password, and new password
 */
const changePassword = async (changePasswordRequest) => {
    try {
        const response = await apiClient.put(`/auth/password`, changePasswordRequest );
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default changePassword;