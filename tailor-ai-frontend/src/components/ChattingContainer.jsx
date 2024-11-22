import MessageBubble from "./MessageBubble.jsx";
import {useState} from "react";
import {useParams} from "react-router-dom";
import createMessage from "../api/message/createMessage.js";


const ChattingContainer = ({initialMessageHistory}) => {

    const [messageHistory, setMessageHistory] = useState(initialMessageHistory);
    const {id} = useParams();
    const [hasInput, setHasInput] = useState(false);

    const createMessageRequest = {
        "message": null,
        "author": "USER"
    }

    const handleInputChange = () => {
        setHasInput(true);
    }

    const handleSubmit = async () => {
        const userMessage = document.getElementById("input").value;
        createMessageRequest.message = userMessage;
        clearInput()
        await createMessage(createMessageRequest, id);
        const newMessage = {
            messageId: Date.now(),
            body: userMessage,
            author: "USER",
            chatSessionId: id,
        };
        setMessageHistory((prevMessages) => [...prevMessages, newMessage]);
    }

    const clearInput = () => {
        document.getElementById("input").value = "";
        setHasInput(false);
    }

    return (
        <main className="flex flex-col items-center h-full overflow-hidden">
            <div>
                <div className="text-center text-4xl py-3">
                    <h1>This is the header where the chat name will go</h1>
                </div>
                <div className="flex flex-col flex-1 gap-2.5 overflow-y-auto h-[60%]">
                    {messageHistory.slice(1).map((message) => (
                        <MessageBubble key={message.messageId} message={message}/>
                    ))}
                </div>
                <div className="flex justify-center items-center gap-4 w-full">
                    <input type="text" placeholder="Message AI" className="input input-bordered w-full max-w-xl"
                           onChange={handleInputChange} id="input"/>
                    <button onClick={handleSubmit} className={`btn ${hasInput ? "btn-primary" : "btn-disabled"}`}>Send
                    </button>
                </div>
            </div>
        </main>
    )


}

export default ChattingContainer;