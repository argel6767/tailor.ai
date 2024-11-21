import axios from "axios";

/**
 * used if the user needs the verification email to be resent for whatever reason
 */
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