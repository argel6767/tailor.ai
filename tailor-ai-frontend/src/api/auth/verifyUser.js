
import {apiClient} from "../apiConfig.js";

/**
 * sends a request to verify user via the code they received in their email
 */
const verifyUser = async (verifyRequest) => {
    try {
        const response = await apiClient.post(`/auth/verify`, verifyRequest)
        return response.status === 200;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default verifyUser;