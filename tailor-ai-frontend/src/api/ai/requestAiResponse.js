import axios  from "axios";
import {jwtToken} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";


const requestAiResponse = async (aiRequest) => {
    try {
        const response = await axios.post(`${API_ENDPOINT}/ai/response`, aiRequest, jwtToken);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default requestAiResponse;