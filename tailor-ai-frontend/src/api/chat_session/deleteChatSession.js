

import {apiClient} from "../apiConfig.js";

const deleteChatSession = async (id) => {
    try {
        const response = await apiClient.delete(`/chatsession/${id}`);
        console.log(response.data)
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default deleteChatSession;