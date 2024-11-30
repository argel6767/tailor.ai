import {useEffect, useState} from "react";
import Loading from "./Loading.jsx";
import getUserChatSessions from "../api/chat_session/getUserChatSessions.js";
import {Link} from "react-router-dom";

const Sidebar = () => {

    const [userChats, setChats] = useState([])
    const [loading, setLoading] = useState(true);

    useEffect( () => {
        const getChats = async () => {
            const grabbedChatSessions  = await getUserChatSessions(localStorage.getItem("email"));
            if (grabbedChatSessions) {
                setChats(grabbedChatSessions);
                setLoading(false);
            }
        }
        getChats() //commented until real implementation
    }, [])



    return (
        <div className="drawer lg:drawer-open">
            <input id="my-drawer-2" type="checkbox" className="drawer-toggle"/>
            <div className="drawer-content flex flex-col items-center justify-center">
                {/* Page content here */}
                <label htmlFor="my-drawer-2" className="btn btn-primary drawer-button lg:hidden">
                    Open drawer
                </label>
            </div>
            <div className="drawer-side bg-base-300 pl-2 pt-2.5">
                <Link className="text-xl text-center px-3 btn btn-primary" to={"/chats"}><h1>Go back to Chats Dashboard</h1></Link>
                <h1 className="text-2xl text-center pt-2 px-3">View previous chats below.</h1>
                <label htmlFor="my-drawer-2" aria-label="close sidebar" className="drawer-overlay"></label>
                {loading ? <div className="flex items-center justify-center h-3/4">
                        <div className="flex flex-col">
                            <Loading loadingMessage={"Grabbing previous chats..."} />
                        </div>

                    </div>
                        :
                    <ul className="menu bg-base-300 text-base-content min-h-full w-80 p-4 gap-2.5">
                        {userChats.map((chat, index) => (
                            <li key={index}>
                                <Link to={`/chats/${chat.chatSessionId}`}>{chat.chatSessionName}</Link>
                            </li>
                        ))}
                    </ul>}
            </div>
        </div>
    )
}

export default Sidebar;