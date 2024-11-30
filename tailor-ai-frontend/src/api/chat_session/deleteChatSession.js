import axios from "axios";
import {jwtToken} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const deleteChatSession = async (id) => {
    try {
        const response = axios.delete(`${API_ENDPOINT}/chatsession/${id}`, jwtToken);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default deleteChatSession;