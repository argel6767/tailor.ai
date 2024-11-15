import {getJwtToken} from "./cookieConfig.js";
const accessToken = getJwtToken()

const config = {
    headers: {
        'Authorization': `Bearer ${accessToken}`
    }
};

export default config;