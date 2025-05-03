/**
 * makes the POST request for uploading pdf file (resume) to the s3 buckt
 */
import {apiClient} from "../apiConfig.js";

const uploadPdfFile = async (chatSessionId, resumeFile) => {
    const formData = new FormData();
    formData.append('file', resumeFile);
    try {
        const response = await apiClient.post(`/s3/pdf/${chatSessionId}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default uploadPdfFile;