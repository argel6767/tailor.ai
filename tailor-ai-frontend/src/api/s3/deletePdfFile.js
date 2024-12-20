import axios from "axios";
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const deletePdfFile = async (id, token) => {
    try {
        const response = await axios.delete(`${API_ENDPOINT}/s3/pdf/${id}`, jwtHeader(token));
        console.log(response.data);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default deletePdfFile;