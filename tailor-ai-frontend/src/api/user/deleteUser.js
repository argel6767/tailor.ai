import axios from "axios";
import {jwtToken} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const deleteUser = async (email) => {
    try {
        const response = await axios.delete(`${API_ENDPOINT}/user/${email}`, jwtToken);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default deleteUser;