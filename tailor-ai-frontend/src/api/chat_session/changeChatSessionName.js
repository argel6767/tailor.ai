
import {apiClient} from "../apiConfig.js";

const changeChatSessionName = async (id, newName) => {
    try {
        const response = await apiClient.put(`/chatsession/${id}/name`, newName, {
            headers: {
                "Content-Type": "text/plain"
            }
        });
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default changeChatSessionName;