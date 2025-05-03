
import {apiClient} from "../apiConfig.js";

const deletePdfFile = async (id) => {
    try {
        const response = await apiClient.delete(`/s3/pdf/${id}`);
        console.log(response.data);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default deletePdfFile;