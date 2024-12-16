import {getJwtToken} from "./cookieConfig.js";


export const jwtHeader = (token) =>  {
    return  {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    };
};

export const fileHeader = {
    headers: {
        'Content-Type': "multipart/form-data",
        Authorization: `Bearer ${getJwtToken()}`
    }
}