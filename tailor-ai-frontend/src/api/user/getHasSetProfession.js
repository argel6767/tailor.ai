import axios from "axios";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";
import {jwtHeader} from "../../config/httpConfigs.js";

/**
 * returns an object that holds a boolean value, whether a user has already set their profession
 */
const getHasSetProfession = async (email, token) => {
    console.log("token", token);
    try {
        const response = await axios.get(`${API_ENDPOINT}/user/profession/${email}`, jwtHeader(token));
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getHasSetProfession;