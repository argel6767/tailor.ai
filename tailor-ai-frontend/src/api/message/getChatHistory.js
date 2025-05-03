
import {apiClient} from "../apiConfig.js";

const getChatHistory = async (chatId) => {
    try {
        const response = await apiClient.get(`/message/${chatId}`);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default getChatHistory