import Sidebar from "../components/Sidebar.jsx";

const ChatSessionPage = () => {
    return (
        <main className="flex">
            <span>
                <Sidebar/>
            </span>
            <div className="flex-1 w-3/5 bg-accent">

            </div>
            <div className="w-1/5">
            </div>
        </main>
    )
}

export default ChatSessionPage