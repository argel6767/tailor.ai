/**
 * makes the POST request for uploading pdf file (resume) to the s3 buckt
 */
import axios from "axios";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";
import {fileHeader} from "../../config/httpConfigs.js";

const uploadPdfFile = async (chatSessionId, resumeFile, token) => {
    const formData = new FormData();
    formData.append('file', resumeFile);
    try {
        const response = await axios.post(`${API_ENDPOINT}/s3/pdf/${chatSessionId}`, formData, fileHeader(token));
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default uploadPdfFile;