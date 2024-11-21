import axios from 'axios';
import {jwtToken} from "../../config/httpConfigs.js";

const getUserChatSessions = async (email) => {
    try {
        const response = await axios.get(`http://localhost:8080/chatsession/all/${email}`, jwtToken);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getUserChatSessions