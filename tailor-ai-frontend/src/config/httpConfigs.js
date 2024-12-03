import {getJwtToken} from "./cookieConfig.js";


export const jwtToken = {
    headers: {
        'Authorization': `Bearer ${getJwtToken()}`
    }
};

export const fileHeader = {
    headers: {
        'Content-Type': "multipart/form-data",
        Authorization: `Bearer ${getJwtToken()}`
    }
}