import axios from "axios";
import {jwtToken} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const getUser = async (email) => {
    try {
        const response = await axios(`${API_ENDPOINT}/user/${email}`, jwtToken)
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default getUser;