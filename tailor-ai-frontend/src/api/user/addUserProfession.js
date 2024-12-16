import axios from 'axios';
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * PUT request that is made to backend to add a users profession
 */
const addUserProfession = async (addProfessionRequest, token) => {
    try {
        const response = await axios.put(`${API_ENDPOINT}/user/profession`, addProfessionRequest, jwtHeader(token));
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default addUserProfession;