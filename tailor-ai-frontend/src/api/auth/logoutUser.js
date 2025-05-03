import {apiClient} from "../apiConfig.js";

const logoutUser = async () => {
    try {
        apiClient.post('/auth/logout')
    }
    catch (error) {
        console.error(error);
    }
}

export  default logoutUser;