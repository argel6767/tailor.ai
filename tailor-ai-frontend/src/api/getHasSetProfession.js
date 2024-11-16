import axios from "axios";
import jwtToken from "../config/jwtToken.js";

/**
 * returns an object that holds a boolean value, whether a user has already set their profession
 */
const getHasSetProfession = async () => {
    try {
        const response = await axios.get("https://localhost:8080/profession", jwtToken);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getHasSetProfession;