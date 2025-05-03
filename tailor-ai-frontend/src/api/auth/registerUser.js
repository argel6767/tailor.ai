import {apiClient} from "../apiConfig.js";

/**
 * registers users and also sends a verification email to them in the backend
 */
const registerUser = async (authRequestValues) => {
    try {
        const response =  await apiClient.post(`/auth/register`, authRequestValues);
        return response.data;
    }
    catch (error) {
        console.log(error);
        return null;
    }
}

export default registerUser;