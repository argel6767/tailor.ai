import axios from 'axios';

/**
 * registers users and also sends a verification email to them in the backend
 */
const registerUser = async (authRequestValues) => {
    try {
        const response = await axios.post('http://localhost:8080/auth/register', authRequestValues);
        console.log(response);
    }
    catch (error) {
        throw error.response? error.response.data : error;
    }
}

export default registerUser;