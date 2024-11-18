import axios from 'axios';
import config from "../config/jwtToken.js";

const getUserChatSessions = async (email) => {
    try {
        const response = await axios.get(`http://localhost:8080/chatsession/${email}`, config);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getUserChatSessions