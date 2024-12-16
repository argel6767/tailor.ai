import axios from "axios";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const changeChatSessionName = async (id, newName, token) => {
    try {
        const response = await axios.put(`${API_ENDPOINT}/chatsession/${id}/name`, newName, {
            headers: {
                "Content-Type": "text/plain",
                Authorization: `Bearer ${token}`
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