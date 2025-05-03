
import {apiClient} from "../apiConfig.js";

/**
 * sends verification code for a forgotten password via email
 */
const sendForgetPasswordVerificationEmail = async (email) => {
    try {
        const response = await apiClient.post(`/auth/forgot/${email}`)
        return response.data;
    }
    catch (error) {
        console.log(error);
        throw error;
    }
}

export default sendForgetPasswordVerificationEmail;