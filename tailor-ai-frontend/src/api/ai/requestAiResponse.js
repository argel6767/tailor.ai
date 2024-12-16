import axios  from "axios";
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";


const requestAiResponse = async (aiRequest, token) => {
    try {
        const response = await axios.post(`${API_ENDPOINT}/ai/response`, aiRequest, jwtHeader(token));
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default requestAiResponse;