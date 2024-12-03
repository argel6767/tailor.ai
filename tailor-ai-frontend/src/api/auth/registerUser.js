import axios from 'axios';
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * registers users and also sends a verification email to them in the backend
 */
const registerUser = async (authRequestValues) => {
    try {
        const response =  await axios.post(`${API_ENDPOINT}/auth/register`, authRequestValues);
        return response.data;
    }
    catch (error) {
        console.log(error);
        return null;
    }
}

export default registerUser;