import axios from 'axios';
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const getUserChatSessions = async (email, token) => {
    try {
        const response = await axios.get(`${API_ENDPOINT}/chatsession/all/${email}`, jwtHeader(token));
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getUserChatSessions