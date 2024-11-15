import axios from 'axios';
import {setCookie} from "../config/cookieConfig.js";

const loginUser = async (authRequestValues) => {
    try {
        const response = await axios.post('http://localhost:8080/auth/login', authRequestValues);
        setCookie(response.data.token);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default loginUser;