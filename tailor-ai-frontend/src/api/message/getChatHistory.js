import axios from "axios";
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const getChatHistory = async (chatId, token) => {
    try {
        const response = await axios.get(`${API_ENDPOINT}/message/${chatId}`, jwtHeader(token));
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default getChatHistory