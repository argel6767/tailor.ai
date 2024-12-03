import Cookies from "js-cookie";

/**
 * sets jwt cookie
 */
export const setCookie = (value) => {
    const thirtyMinutes = new Date();
    thirtyMinutes.setTime(thirtyMinutes.getTime() + (30*60*1000));
   Cookies.set("jwt", value, {expires: thirtyMinutes});
}

/**
 * grabs jwt token
 */
export const getJwtToken = () => {
    return Cookies.get("jwt");
};

/**
 * removes token, from cookies, usually used for when signing out
 */
export const removeJwtToken = () => {
    Cookies.remove("jwt");
}


/**
 * checks whether the jwt cookie is expired
 * (expires after 30 minutes)
 */
export const isCookieExpired = () => {
    const jwtToken = getJwtToken();
    return !jwtToken;
};