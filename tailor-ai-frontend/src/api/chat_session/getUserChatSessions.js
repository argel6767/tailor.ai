
import {apiClient} from "../apiConfig.js";

const getUserChatSessions = async () => {
    try {
        const response = await apiClient.get(`chatsession/all`);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getUserChatSessions