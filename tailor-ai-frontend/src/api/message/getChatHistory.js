import axios from "axios";
import {jwtToken} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const getChatHistory = async (chatId) => {
    try {
        const response = await axios.get(`${API_ENDPOINT}/message/${chatId}`, jwtToken);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default getChatHistory