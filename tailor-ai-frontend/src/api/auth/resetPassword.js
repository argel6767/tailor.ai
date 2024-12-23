import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";
import axios from "axios";

/**
 * resets password for user
 * THIS ENDPOINT IS USED FOR FORGOTTEN PASSWORDS
 */
const resetPassword = async (resetPasswordRequest) => {
    try {
        console.log(resetPasswordRequest);
        const response = await axios.put(`${API_ENDPOINT}/auth/reset`, resetPasswordRequest)
        return response.data;
    }
    catch (error) {
        console.log(error);
        throw error;
    }
}

export default resetPassword;