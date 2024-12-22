import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";
import {jwtHeader} from "../../config/httpConfigs.js";
import axios from "axios";

/**
 * resets password for user
 * THIS ENDPOINT IS USED FOR FORGOTTEN PASSWORDS
 */
const resetPassword = async (token, resetPasswordRequest) => {
    try {
        const response = await axios.post(`${API_ENDPOINT}/auth/reset`, resetPasswordRequest, jwtHeader(token))
        return response.data;
    }
    catch (error) {
        console.log(error);
        throw error;
    }
}

export default resetPassword;