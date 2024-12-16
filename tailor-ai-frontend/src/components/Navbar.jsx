import {Link, useNavigate} from "react-router-dom";
import {UserAvatar} from "./UserAvatar.jsx";
import {useGlobalContext} from "./GlobalContext.jsx";

const Navbar = ({refreshApp}) => {

    const {token} = useGlobalContext();

    const navigate = useNavigate();

    const goHome = () => {
        navigate("/");
    }

    const goToChats = () => {
        if (!token) {
            navigate("/auth");
        }
        else {
            navigate("/chats");
        }
    }

    return (
        <nav className="navbar  bg-base-300 gap-6 text-center p-2 px-4">
            <button className="btn btn-primary flex-1 max-w-24" onClick={goHome}>Home</button>
            <button className="btn btn-primary flex-1 max-w-28" onClick={goToChats}>View Chats</button>
            <button className="btn bg-primary"> <a href={"https://github.com/argel6767/tailor.ai"}>Source Code</a></button>
            <div className="flex-1"></div>
            <UserAvatar refreshApp={refreshApp}/>
        </nav>
    )
}
export default Navbar;