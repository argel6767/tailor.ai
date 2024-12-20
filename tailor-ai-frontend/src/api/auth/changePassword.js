import axios from "axios";
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * API request for changing user's password
 * takes an object that has
 * email, old password, and new password
 */
const changePassword = async (changePasswordRequest, token) => {
    try {
        const response = await axios.put(`${API_ENDPOINT}/auth/password`, changePasswordRequest, jwtHeader(token) );
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default changePassword;