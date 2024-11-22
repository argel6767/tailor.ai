import Sidebar from "../components/Sidebar.jsx";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import ChattingContainer from "../components/ChattingContainer.jsx";
import Loading from "../components/Loading.jsx";
import getChatHistory from "../api/message/getChatHistory.js";


const ChatSessionPage = () => {
    const [messages, setMessages] = useState([]);
    const [messageHistoryGrabbed, setMessageHistoryGrabbed] = useState(false);
    const {id} = useParams();

    useEffect(() => {
        const fetchChatHistory = async () => {
            const chats = await getChatHistory(id);
            if (chats) {
                setMessages(chats);
                setMessageHistoryGrabbed(true);
            }
        }
        fetchChatHistory();
    },[id]);


    return (
        <main className="flex h-full overflow-hidden">
            <span>
                <Sidebar/>
            </span>
            <div className="flex-1 flex justify-center w-4/5 bg-accent">
                {messageHistoryGrabbed ?
                    <ChattingContainer initialMessageHistory={messages}/>
                :
                    <Loading loadingMessage={"Grabbing chat history..."} />}

            </div>
        </main>
    )
}

export default ChatSessionPage