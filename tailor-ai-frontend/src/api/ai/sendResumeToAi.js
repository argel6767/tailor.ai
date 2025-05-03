
import  {apiClient} from "../apiConfig.js";

const sendResumeToAi = async (id, profession, resumeFile) => {
    const formData = new FormData();
    formData.append("file", resumeFile);
    formData.append("profession", profession);
    try {
        const response = await apiClient.post(`/ai/file/${id}`, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            }
        });
        return response.data
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default sendResumeToAi;