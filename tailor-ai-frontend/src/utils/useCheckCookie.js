import { useEffect } from "react";
import {useNavigate} from "react-router-dom";
import {isCookieExpired} from "../config/cookeConfig.js";


const useCheckCookie = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const checkJwtCookie = () => {
            if (isCookieExpired()) {
                navigate("/login");
            }
        }
        checkJwtCookie();
        const intervalId = setInterval(checkJwtCookie, 15*60*1000);
        return () => clearInterval(intervalId);

    }, [navigate])
};

export default useCheckCookie;