import axios from 'axios';
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * makes the POST request for creating a new chat session in the by the user email
 * while also saving the user's pdf
 */
const createChatSession = async (email, token) => {
    try {
        const response = await axios.post(`${API_ENDPOINT}/chatsession/${email}`, {}, jwtHeader(token));
        return response.data
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default createChatSession;