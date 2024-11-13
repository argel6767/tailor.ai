import axios from 'axios';
import AuthPage from "../pages/AuthPage.jsx";

const loginUser = async (authRequestValues) => {
    try {
        const response = await axios.post('http://localhost:8080/auth/login', authRequestValues);
        console.log(response);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default loginUser;