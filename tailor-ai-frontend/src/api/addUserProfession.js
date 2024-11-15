import axios from 'axios';
import jwtToken from "../config/jwtToken.js";

const addUserProfession = async (addProfessionRequest) => {
    try {
        const response = await axios.post('http://localhost:8080/user/profession', jwtToken, addProfessionRequest);
        console.log(response.data);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default addUserProfession;