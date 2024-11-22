import axios from "axios";
import {fileHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const sendResumeToAi = async (id, profession, resumeFile) => {
    const formData = new FormData();
    formData.append("file", resumeFile);
    formData.append("profession", profession);
    try {
        const response = await axios.post(`${API_ENDPOINT}/file/${id}`, formData, fileHeader);
        return response.data
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default sendResumeToAi;