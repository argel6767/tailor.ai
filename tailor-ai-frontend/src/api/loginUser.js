import axios from 'axios';
import AuthPage from "../pages/AuthPage.jsx";

const loginUser = async (authRequestValues) => {
    try {
        const response = await axios.post('http://localhost:8080/auth/login', authRequestValues);
        console.log(response); //TODO add cookie grabbing logic for jwt soon
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default loginUser;