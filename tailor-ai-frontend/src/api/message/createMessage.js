
import {apiClient} from "../apiConfig.js";

const createMessage = async (createMessageRequest, id) => {
    try {
        const response = await apiClient.post(`/message/create/${id}`, createMessageRequest);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default createMessage;