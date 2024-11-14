import axios from 'axios';

const verifyUser = async (verifyRequest) => {
    try {
        const response = await axios.post('http://localhost:8080/auth/verify', verifyRequest)
        console.log(response)
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default verifyUser;