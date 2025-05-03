
import {apiClient} from "../apiConfig.js";

const getChatSession = async (id) => {
    try {
        const response = await apiClient.get(`/chatsession/${id}`);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default getChatSession;