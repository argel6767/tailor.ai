import axios from "axios";

const resendVerificationEmail = async (resendEmailRequest) => {
    try {
        const response = await axios.post('http://localhost:8080/auth/verify', resendEmailRequest);
        console.log(response);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default resendVerificationEmail;