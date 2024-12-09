import {Link} from "react-router-dom";
import {UserAvatar} from "./UserAvatar.jsx";

const Navbar = ({refreshApp}) => {
    return (
        <nav className="navbar  bg-base-300 gap-6 text-center p-2 px-4">
            <Link className="btn btn-primary flex-1 max-w-24" to="/" >Home</Link>
            <Link className="btn btn-primary flex-1 max-w-28" to="/chats">View Chats</Link>
            <button className="btn bg-primary"> <a href={"https://github.com/argel6767/tailor.ai"}>Source Code</a></button>
            <div className="flex-1"></div>
            <UserAvatar refreshApp={refreshApp}/>
        </nav>
    )
}
export default Navbar;