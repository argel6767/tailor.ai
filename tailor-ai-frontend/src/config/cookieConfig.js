import Cookies from "js-cookie";

export const setCookie = (value) => {
    const thirtyMinutes = new Date();
    thirtyMinutes.setTime(thirtyMinutes.getTime() + (30*60*1000));
   Cookies.set("jwt", value, {expires: thirtyMinutes});
}

export const getJwtToken = () => {
    return Cookies.get("jwt");
};

export const isCookieExpired = () => {
    const jwtToken = getJwtToken();
    return !jwtToken;
};