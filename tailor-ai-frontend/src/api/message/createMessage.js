import axios from "axios";
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const createMessage = async (createMessageRequest, id, token) => {
    try {
        const response = await axios.post(`${API_ENDPOINT}/message/create/${id}`, createMessageRequest, jwtHeader(token));
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default createMessage;