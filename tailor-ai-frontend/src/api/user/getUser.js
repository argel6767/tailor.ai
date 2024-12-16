import axios from "axios";
import {jwtHeader} from "../../config/httpConfigs.js";
import {API_ENDPOINT} from "../../config/apiEndpointConfig.js";

const getUser = async (email,token) => {
    console.log("token submitted in getUser: ", token);
    try {
        const response = await axios(`${API_ENDPOINT}/user/${email}`, jwtHeader(token))
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default getUser;