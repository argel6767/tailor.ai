import axios from "axios";
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const deleteUser = async (email, token) => {
    try {
        const response = await axios.delete(`${API_ENDPOINT}/user/${email}`, jwtHeader(token));
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default deleteUser;