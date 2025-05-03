import { useNavigate} from "react-router-dom";
import {UserAvatar} from "./UserAvatar.jsx";
import {useGlobalContext} from "./GlobalContext.jsx";


const Navbar = () => {

    const navigate = useNavigate();
    const {isSignedIn} = useGlobalContext()

    const goHome = () => {
        navigate("/");
    }

    const goToChats = () => {
        if (!isSignedIn) {
            navigate("/auth");
        }
        else {
            navigate("/chats");
        }
    }

    return (
        <nav className="navbar  bg-base-300 gap-6 text-center p-2 px-4">
            <button className="btn btn-primary flex-1 max-w-28" onClick={goHome}>Home</button>
            <button className="btn btn-primary flex-1 max-w-28" onClick={goToChats}>View Chat Dashboard</button>
            <button className="btn bg-primary"> <a href={"https://github.com/argel6767/tailor.ai"}>Source Code</a></button>
            <div className="flex-1"></div>
            <UserAvatar/>
        </nav>
    )
}
export default Navbar;