import axios from "axios";
import {blobHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const getPDFFile = async (id, token) => {
    try {
        const response = await axios.get(`${API_ENDPOINT}/s3/pdf/${id}`, blobHeader(token));
        console.log(response.data);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default getPDFFile;