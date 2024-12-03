import { useEffect } from "react";
import {useNavigate} from "react-router-dom";
import {isCookieExpired} from "../config/cookieConfig.js";

/**
 * custom hook that checks whether cookie is expired, and does so every 15 minutes
 * if the token is indeed expired then the user is sent back to the home page to re login
 */
const useCheckCookie = (redirectPath = "/auth", excludePaths = ["/"], refreshAppKey) => {
    const navigate = useNavigate();

    useEffect(() => {
        const checkJwtCookie = () => {
            const currentPath = window.location.pathname;
            if (isCookieExpired() && !excludePaths.includes(currentPath)) {
                navigate(redirectPath);
                refreshAppKey();
            }
        }
        checkJwtCookie();
        const intervalId = setInterval(checkJwtCookie, 15*60*1000);
        return () => clearInterval(intervalId);

    }, [navigate, refreshAppKey])
};

export default useCheckCookie;