

import {apiClient} from "../apiConfig.js";

const getUser = async () => {
    try {
        const response = await apiClient.get(`/user`)
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
        throw error;
    }
}

export default getUser;