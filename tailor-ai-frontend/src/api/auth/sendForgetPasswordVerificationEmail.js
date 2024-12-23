import axios from "axios";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * sends verification code for a forgotten password via email
 */
const sendForgetPasswordVerificationEmail = async (email) => {
    try {
        const response = await axios.post(`${API_ENDPOINT}/auth/forgot/${email}`)
        return response.data;
    }
    catch (error) {
        console.log(error);
        throw error;
    }
}

export default sendForgetPasswordVerificationEmail;