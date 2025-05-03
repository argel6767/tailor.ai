
import {apiClient} from "../apiConfig.js";

/**
 * resets password for user
 * THIS ENDPOINT IS USED FOR FORGOTTEN PASSWORDS
 */
const resetPassword = async (resetPasswordRequest) => {
    try {
        console.log(resetPasswordRequest);
        const response = await apiClient.put(`auth/reset`, resetPasswordRequest)
        return response.data;
    }
    catch (error) {
        console.log(error);
        throw error;
    }
}

export default resetPassword;