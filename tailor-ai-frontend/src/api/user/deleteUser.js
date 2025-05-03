
import {apiClient} from "../apiConfig.js";

const deleteUser = async () => {
    try {
        const response = await apiClient.delete(`/user`);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default deleteUser;