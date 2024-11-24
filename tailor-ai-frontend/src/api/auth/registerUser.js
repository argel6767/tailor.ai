import axios from 'axios';
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * registers users and also sends a verification email to them in the backend
 */
const registerUser = async (authRequestValues) => {
    try {
        return await axios.post(`${API_ENDPOINT}/auth/register`, authRequestValues);
    }
    catch (error) {
        throw error.response? error.response.data : error;
    }
}

export default registerUser;