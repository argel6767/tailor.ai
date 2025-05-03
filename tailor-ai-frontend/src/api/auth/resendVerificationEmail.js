
import {apiClient} from "../apiConfig.js";

/**
 * used if the user needs the verification email to be resent for whatever reason
 */
const resendVerificationEmail = async (resendEmailRequest) => {
    try {
        const response = await apiClient.post(`/auth/resend`, resendEmailRequest);
        console.log(response);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default resendVerificationEmail;