
import {apiClient} from "../apiConfig.js";

/**
 * returns an object that holds a boolean value, whether a user has already set their profession
 */
const getHasSetProfession = async () => {
    try {
        const response = await apiClient.get(`/user/profession`);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getHasSetProfession;