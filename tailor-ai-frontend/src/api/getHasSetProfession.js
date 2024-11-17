import axios from "axios";
import config from "../config/jwtToken.js";

/**
 * returns an object that holds a boolean value, whether a user has already set their profession
 */
const getHasSetProfession = async (request) => {
    try {
        const response = await axios.get("http://localhost:8080/profession", request, config);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getHasSetProfession;