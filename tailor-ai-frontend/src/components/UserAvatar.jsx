import React, {useState} from 'react';
import {Link} from "react-router-dom";
import {isCookieExpired} from "../config/cookieConfig.js";

export const UserAvatar = () => {

    const [isSignedIn, setIsSignedIn] = useState(false);

    const handleIsSignedIn = () => {
        if (!isCookieExpired()) {
            setIsSignedIn(true);
        }
        else {
            setIsSignedIn(false);
        }
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
                    {isSignedIn ? (<li><Link to={"/"}>Sign Out</Link></li>) :
                        (<li><Link to={"/auth"}>Sign In</Link></li>)}
                </ul>
            </div>
        </main>
    )
}