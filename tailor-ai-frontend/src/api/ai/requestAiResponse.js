

import {apiClient} from "../apiConfig.js";


const requestAiResponse = async (aiRequest) => {
    try {
        const response = await apiClient.post(`/ai/response`, aiRequest);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default requestAiResponse;