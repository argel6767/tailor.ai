import axios from 'axios';

const registerUser = async (authRequestValues) => {
    try {
        const response = await axios.post('http://localhost:8080/auth/register', authRequestValues);
        console.log(response);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default registerUser;