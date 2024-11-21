import axios from "axios";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * used if the user needs the verification email to be resent for whatever reason
 */
const resendVerificationEmail = async (resendEmailRequest) => {
    try {
        const response = await axios.post(`${API_ENDPOINT}/auth/verify`, resendEmailRequest);
        console.log(response);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default resendVerificationEmail;