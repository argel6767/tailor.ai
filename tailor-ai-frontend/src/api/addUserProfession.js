import axios from 'axios';
import jwtToken from "../config/jwtToken.js";

const addUserProfession = async (addProfessionRequest) => {
    try {
        const response = await axios.put('http://localhost:8080/user/profession', addProfessionRequest, jwtToken);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default addUserProfession;