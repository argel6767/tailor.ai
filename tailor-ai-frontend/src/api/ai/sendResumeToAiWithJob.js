

import {apiClient} from "../apiConfig.js";

const sendResumeToAiWithJob = async (id, jobUrl, file) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("jobUrl", jobUrl);
    try {
        const response = await apiClient.post(`/ai/file/${id}/job`, formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        });
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default sendResumeToAiWithJob;