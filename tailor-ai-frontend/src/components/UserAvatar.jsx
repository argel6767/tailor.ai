import React, {useState} from 'react';
import {Link} from "react-router-dom";
import {isCookieExpired, removeJwtToken} from "../config/cookieConfig.js";
import {useNavigate} from "react-router-dom";

export const UserAvatar = ({refreshApp}) => {

    const [isSignedIn, setIsSignedIn] = useState(false);
    const navigate = useNavigate();

    const handleIsSignedIn = () => {
        if (!isCookieExpired()) {
            setIsSignedIn(true);
        }
        else {
            setIsSignedIn(false);
        }
    }

    const handleSignOut = () => {
        removeJwtToken();
        refreshApp();
        setIsSignedIn(false);
        navigate("/")
    }

    return (
        <main>
            <div className="dropdown dropdown-end">
                <div tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar" onClick={handleIsSignedIn}>
                    <div className="w-10 rounded-full">
                        <img
                            alt="Profile Avatar"
                            src="https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg"/>
                    </div>
                </div>
                <ul
                    tabIndex={0}
                    className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
                            <li><Link to={"/user"}>Profile</Link></li>
                    {isSignedIn ? (<li><button onClick={handleSignOut}>Sign Out</button></li>) :
                        (<li><Link to={"/auth"}>Sign In</Link></li>)}
                </ul>
            </div>
        </main>
    )
}