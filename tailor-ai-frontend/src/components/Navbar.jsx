import { Link, useNavigate} from "react-router-dom";
import {UserAvatar} from "./UserAvatar.jsx";
import {useGlobalContext} from "./GlobalContext.jsx";


const Navbar = () => {
    const {isSignedIn} = useGlobalContext()

    const determineChatLinkDestination = () => {
        if (!isSignedIn) {
            return '/auth';
        }
        else {
            return '/chats';
        }
    }

    return (
        <nav className="navbar  bg-base-300 gap-6 text-center p-2 px-4">
            <Link className="btn bg-primary flex-1 max-w-28" to={'/'}>Home</Link>
            <Link className="btn bg-primary flex-1 max-w-28" to={determineChatLinkDestination()}>Chat Dashboard</Link>
            <Link className="btn bg-primary" to={"https://github.com/argel6767/tailor.ai"}>Source Code</Link>
            <div className="flex-1"></div>
            <UserAvatar/>
        </nav>
    )
}
export default Navbar;