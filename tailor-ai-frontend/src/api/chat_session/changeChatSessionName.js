import axios from "axios";
import {jwtToken} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";
import {getJwtToken} from "../../config/cookieConfig.js";

const changeChatSessionName = async (id, newName) => {
    try {
        const response = await axios.put(`${API_ENDPOINT}/chatsession/${id}/name`, newName, {
            headers: {
                "Content-Type": "text/plain",
                Authorization: `Bearer ${getJwtToken()}`
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