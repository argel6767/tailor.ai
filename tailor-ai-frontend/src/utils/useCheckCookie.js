import { useEffect } from "react";
import {useNavigate} from "react-router-dom";
import {isCookieExpired} from "../config/cookieConfig.js";


const useCheckCookie = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const checkJwtCookie = () => {
            if (isCookieExpired()) {
                navigate("/auth");
            }
        }
        checkJwtCookie();
        const intervalId = setInterval(checkJwtCookie, 15*60*1000);
        return () => clearInterval(intervalId);

    }, [navigate])
};

export default useCheckCookie;