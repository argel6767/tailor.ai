import axios from 'axios';
import {jwtToken} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

/**
 * PUT request that is made to backend to add a users profession
 */
const addUserProfession = async (addProfessionRequest) => {
    try {
        const response = await axios.put(`${API_ENDPOINT}/user/profession`, addProfessionRequest, jwtToken);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default addUserProfession;