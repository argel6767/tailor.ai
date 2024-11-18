import {getJwtToken} from "./cookieConfig.js";
const accessToken = getJwtToken()

export const jwtToken = {
    headers: {
        'Authorization': `Bearer ${accessToken}`
    }
};

export const fileHeader = {
    headers: {
        'Content-Type': "multipart/form-data",
        Authorization: `Bearer ${accessToken}`
    }
}