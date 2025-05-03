
import {apiClient} from "../apiConfig.js";

/**
 * logs in user then saves the jwt token response in cookie to allow for security and to use across
 * site for secured endpoints
 */
const loginUser = async (authRequestValues) => {
    try {
        const response =  await apiClient.post(`/auth/login`, authRequestValues);
        if (response.status !== 200) {
            return null;
        }
        return response;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        return null;
    }
}

export default loginUser;