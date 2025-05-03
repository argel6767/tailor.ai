
import {apiClient} from "../apiConfig.js";

/**
 * makes the POST request for creating a new chat session in the by the user email
 * while also saving the user's pdf
 */
const createChatSession = async () => {
    try {
        const response = await apiClient.post(`/chatsession/`);
        return response.data
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default createChatSession;