import axios from 'axios';
import {jwtToken} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const getUserChatSessions = async (email) => {
    try {
        const response = await axios.get(`${API_ENDPOINT}/chatsession/all/${email}`, jwtToken);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getUserChatSessions