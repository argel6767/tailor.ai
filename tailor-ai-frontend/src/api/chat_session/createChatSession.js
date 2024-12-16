import axios from 'axios';
import {fileHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * makes the POST request for creating a new chat session in the by the user email
 * while also saving the user's pdf
 */
const createChatSession = async (email, resumeFile, token) => {
    console.log("token submitted: ", token);
    const formData = new FormData();
    formData.append('file', resumeFile);
    try {
        const response = await axios.post(`${API_ENDPOINT}/chatsession/${email}`, formData, fileHeader(token));
        return response.data
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default createChatSession;