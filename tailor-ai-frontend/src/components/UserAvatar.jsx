
import {Link} from "react-router-dom";
import {useNavigate} from "react-router-dom";
import {useGlobalContext} from "./GlobalContext.jsx";
import logoutUser from "../api/auth/logoutUser.js";

export const UserAvatar = () => {

    const navigate = useNavigate();
    const {isSignedIn, setIsSignedIn} = useGlobalContext();


    /*
     * removes, to make user have to sign back in
     * sets signed back to false
     * and navigates back to landing page
     */
    const handleSignOut = async () => {
        await logoutUser()
        setIsSignedIn(false);
        navigate("/")
    }

    const goToProfile = () => {
      navigate('/user')
    }

    return (
        <main>
            <div className="dropdown dropdown-end">
                <div tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar">
                    <div className="w-10 rounded-full">
                        <img
                            alt="Profile Avatar"
                            src="https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg"/>
                    </div>
                </div>
                <ul
                    tabIndex={0}
                    className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
                            <li><button onClick={goToProfile}>Profile</button></li>
                    {isSignedIn ? (<li><button onClick={handleSignOut}>Sign Out</button></li>) :
                        (<li><Link to={"/auth"}>Sign In</Link></li>)}
                </ul>
            </div>
        </main>
    )
}