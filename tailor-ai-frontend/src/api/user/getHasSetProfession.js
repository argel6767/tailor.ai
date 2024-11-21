import axios from "axios";
import {jwtToken} from "../../config/httpConfigs.js";

/**
 * returns an object that holds a boolean value, whether a user has already set their profession
 */
const getHasSetProfession = async (email) => {
    try {
        const response = await axios.get(`http://localhost:8080/user/profession/${email}`, jwtToken);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getHasSetProfession;