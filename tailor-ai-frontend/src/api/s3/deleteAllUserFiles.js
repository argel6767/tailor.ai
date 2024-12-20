import axios from "axios";
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * api call for deleting pdf file from s3 bucket
 */
const deleteAllUserFiles = async (email, token) => {
    try {
        const response = await axios.delete(`${API_ENDPOINT}/s3/${email}`, jwtHeader(token));
        console.log(response.data);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default deleteAllUserFiles;