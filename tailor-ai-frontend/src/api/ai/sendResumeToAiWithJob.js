import axios from 'axios';
import {fileHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const sendResumeToAiWithJob = async (id, jobUrl, file) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("jobUrl", jobUrl);
    try {
        const response = await axios.post(`${API_ENDPOINT}/ai/file/${id}/job`, formData,fileHeader);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default sendResumeToAiWithJob;