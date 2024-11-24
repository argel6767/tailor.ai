import axios from 'axios';
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * logs in user then saves the jwt token response in cookie to allow for security and to use across
 * site for secured endpoints
 */
const loginUser = async (authRequestValues) => {
    try {
        const response = await axios.post(`${API_ENDPOINT}/auth/login`, authRequestValues);
        return response.data.token;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error.response? error.response.data : error;
    }
}

export default loginUser;