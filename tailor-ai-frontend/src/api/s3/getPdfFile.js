
import {apiClient} from "../apiConfig.js";

const getPdfFile = async (id) => {
    try {
        const response = await apiClient.get(`/s3/pdf/${id}`, {
            responseType: 'blob'
        });
        console.log(response.data);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default getPdfFile;