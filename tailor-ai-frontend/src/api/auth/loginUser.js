import axios from 'axios';
import {getJwtToken, setCookie} from "../config/cookieConfig.js";

/**
 * logs in user then saves the jwt token response in cookie to allow for security and to use across
 * site for secured endpoints
 */
const loginUser = async (authRequestValues) => {
    try {
        const response = await axios.post('http://localhost:8080/auth/login', authRequestValues);
        setCookie(response.data.token);
        console.log(getJwtToken());
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error.response? error.response.data : error;
    }
}

export default loginUser;