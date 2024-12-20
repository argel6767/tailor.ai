import Sidebar from "../components/Sidebar.jsx";
import {useState} from "react";
import {sleep} from "../utils/sleep.js";
import createChatSession from "../api/chat_session/createChatSession.js";
import sendResumeToAi from "../api/ai/sendResumeToAi.js";
import sendResumeToAiWithJob from "../api/ai/sendResumeToAiWithJob.js";
import getUser from "../api/user/getUser.js";
import Loading from "../components/Loading.jsx";
import {useNavigate} from "react-router-dom";
import UploadJob from "../components/UploadJob.jsx";
import {useGlobalContext} from "../components/GlobalContext.jsx";
import uploadPdfFile from "../api/s3/uploadPdfFile.js";

/**
 * Chat dashboard page that contains previous chats on the side and allows users to start a new one
 */
const ChatDashboardPage = () => {
    const {token} = useGlobalContext();
    const {user, setUser} = useGlobalContext();
    const [hasSubmitted, setHasSubmitted] = useState(false);
    const [pdfReminder, setPdfReminder] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [urlValue, setUrlValue] = useState("");
    const navigate = useNavigate();

    const handleLoading = () => {
        setIsLoading(!isLoading);
    }

    const handleUrlValue = (url) => {
        setUrlValue(url);
    }

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
        const file = document.getElementById("file-input").files[0];
        const email = sessionStorage.getItem("email");
        handleLoading()
        try {
            const chatSessionDetails = await createChatSession(email, token);
            await uploadPdfFile(chatSessionDetails.chatSessionId, file, token);
            if (urlValue !== "") {
                await sendResumeToAiWithJob(chatSessionDetails.chatSessionId, urlValue, file, token);
            }
            else {
                let response = null;
                if (!user) {
                    response = await getUser(email, token);
                    setUser(response);
                    await sendResumeToAi(chatSessionDetails.chatSessionId, response.profession, file, token);
                }
                else {
                    await sendResumeToAi(chatSessionDetails.chatSessionId, user.profession, file, token);
                }
            }
            navigate(`/chats/${chatSessionDetails.chatSessionId}`);
        }
        catch (error) {
            console.log(error);
        }

    }

    return (
        <div className="flex gap-3">
            <span>
                <Sidebar />
            </span>
            <main className="flex-1 flex flex-col text-center px-2 pt-12 gap-8">
                <h1 className="text-6xl">Welcome, begin a new chat by uploading your resume.</h1>
                <div className="flex gap-8 justify-center pb-6">
                    <input type="file" onChange={handleFileSubmission}
                           className="file-input file-input-bordered w-full max-w-xs" id='file-input' data-testid="file-input" />
                    <UploadJob sendUpUrl={handleUrlValue}/>
                    <button className={`btn btn-active ${hasSubmitted ? 'btn-primary' : 'btn-disable'}`}
                            disabled={!hasSubmitted} onClick={makeChatSession}>Begin chat
                    </button>
                </div>
                    <span className="flex justify-center items-center">
                        <div className="w-2/5">
                            {isLoading ? <Loading loadingMessage={"Creating new chat..."}/> : null}
                        </div>
                    </span>
                <footer
                    className={`flex gap-8 justify-center text-center ${pdfReminder ? 'visible' : 'invisible'}`}> Must be of type PDF</footer>
            </main>
        </div>

    )
}

export default ChatDashboardPage;