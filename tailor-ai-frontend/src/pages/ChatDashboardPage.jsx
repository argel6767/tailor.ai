import Sidebar from "../components/Sidebar.jsx";
import {useState} from "react";
import {sleep} from "../utils/sleep.js";
import createChatSession from "../api/createChatSession.js";
import {useNavigate} from "react-router-dom";

/**
 * Chat dashboard page that contains previous chats on the side and allows users to start a new one
 */
const ChatDashboardPage = () => {

    const [hasSubmitted, setHasSubmitted] = useState(false);
    const [pdfReminder, setPdfReminder] = useState(false);
    const navigate = useNavigate();

    /**
     * handles when a user uploads a file, first checks if the file is a pdf
     * if not then a message pops up for 2 seconds and then disappears,
     * then resets the input
     * else sets changes state of hasSubmitted to allow button to be pressed
     */
    const handleFileSubmission = async () => {
        const fileInput = document.getElementById("file-input");
        const file = fileInput.value;
        if (!file.includes(".pdf")) {
            setPdfReminder(true);
            await sleep(2000);
            setPdfReminder(false);
            fileInput.value = "";
            setHasSubmitted(false)
        }
        else {
            setHasSubmitted(!hasSubmitted);
        }
    }

    /**
     * creates the chat session via the api call, then routes user to a new page for their chat session
     * that uses the returned ChatSessionPage object from the api call to make a new route
     */
    const makeChatSession = async () => {
        const file = document.getElementById("file-input").value;
        const email = localStorage.getItem("email");
        const chatSessionDetails = await createChatSession(email, file);
        navigate(`/chats/${chatSessionDetails.id}`);
    }

    return (
        <div className="flex gap-3">
            <span>
                <Sidebar />
            </span>
            <main className="flex-1 flex flex-col text-center px-2 pt-12 gap-8">
                <h1 className="text-6xl">Welcome, begin a new chat by uploading your resume.</h1>
                <div className="flex gap-8 justify-center">
                    <input type="file" onChange={handleFileSubmission}
                           className="file-input file-input-bordered w-full max-w-xs" id='file-input' data-testid="file-input" />
                    <button className={`btn btn-active ${hasSubmitted ? 'btn-primary' : 'btn-disable'}`}
                            disabled={!hasSubmitted} onClick={makeChatSession}>Begin chat
                    </button>
                </div>
                <footer className={`flex gap-8 justify-center text-center ${pdfReminder ? 'visible' : 'invisible'}`}> Must be of type PDF</footer>
            </main>
        </div>

    )
}

export default ChatDashboardPage;