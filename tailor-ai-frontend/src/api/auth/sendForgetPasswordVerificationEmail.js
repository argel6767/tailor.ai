import axios from "axios";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";
import {jwtHeader} from "../../config/httpConfigs.js";

/**
 * sends verification code for a forgotten password via email
 */
const sendForgetPasswordVerificationEmail = async (email, token) => {
    try {
        const response = await axios.post(`${API_ENDPOINT}/forgot/${email}`, jwtHeader(token))
        return response.data;
    }
    catch (error) {
        console.log(error);
        throw error;
    }
}

export default sendForgetPasswordVerificationEmail;