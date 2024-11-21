import axios from 'axios';
import {fileHeader} from "../config/httpConfigs.js";

/**
 * makes the POST request for creating a new chat session in the by the user email
 * while also saving the user's pdf
 */
const createChatSession = async (email, resumeFile) => {
    const formData = new FormData();
    formData.append('file', resumeFile);
    try {
        const response = await axios.post(`http://localhost:8080/chatsession/${email}`, formData, fileHeader);
        return response.data
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default createChatSession;