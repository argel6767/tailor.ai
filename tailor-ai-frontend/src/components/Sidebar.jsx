import {useEffect, useState} from "react";
import Loading from "./Loading.jsx";
import getUserChatSessions from "../api/getUserChatSessions.js";
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
            <div className="drawer-side bg-base-300 pl-2">
                <h1 className="text-2xl text-center pt-1 px-3">View previous chats below.</h1>
                <label htmlFor="my-drawer-2" aria-label="close sidebar" className="drawer-overlay"></label>
                    {/*TODO dynamically make list of chat sessions gotten by the api request*/}
                    {/*TODO also make a loading placeholder for them until they load up eventually*/}
                {loading ? <div className="flex items-center justify-center h-3/4">
                        <div className="flex flex-col">
                            <Loading/>
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