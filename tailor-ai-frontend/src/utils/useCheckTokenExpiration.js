import { useEffect } from "react";
import {useNavigate} from "react-router-dom";

/**
 * custom hook that checks whether token is expired, and does so every 15 minutes
 * if the token is indeed expired then the user is sent back to the home page to re login
 */
const useCheckTokenExpiration = (redirectPath = "/auth", excludePaths = ["/"], refreshAppKey, expiration) => {
    const navigate = useNavigate();

    useEffect(() => {
        const checkJwtToken = () => {
            const currentPath = window.location.pathname;
            if ((Date.now() >= expiration) && !excludePaths.includes(currentPath)) {
                navigate(redirectPath);
                refreshAppKey();
            }
        }
        checkJwtToken();
        const intervalId = setInterval(checkJwtToken, 15*60*1000);
        return () => clearInterval(intervalId);

    }, [navigate, refreshAppKey, expiration])
};

export default useCheckTokenExpiration;