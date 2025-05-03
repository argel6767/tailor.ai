
import {apiClient} from "../apiConfig.js";

/**
 * api call for deleting pdf file from s3 bucket
 */
const deleteAllUserFiles = async () => {
    try {
        const response = await apiClient.delete(`/s3}`);
        console.log(response.data);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default deleteAllUserFiles;