
import {apiClient} from "../apiConfig.js";

/**
 * PUT request that is made to backend to add a users profession
 */
const addUserProfession = async (addProfessionRequest, token) => {
    try {
        const response = await apiClient.put(`/user/profession`, addProfessionRequest);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default addUserProfession;