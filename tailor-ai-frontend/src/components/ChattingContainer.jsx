import MessageBubble from "./MessageBubble.jsx";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import createMessage from "../api/message/createMessage.js";
import requestAiResponse from "../api/ai/requestAiResponse.js";


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

    const handleKeyPress = (e) => {
        if (e.key === "Enter") {
            handleSubmit();
        }
    }

    /*
     * submit
     */
    const handleSubmit = async () => {
        const userMessage = document.getElementById("input").value;
        createMessageRequest.message = userMessage;
        clearInput()
        await createMessage(createMessageRequest, id);
        const newMessage = {
            messageId: Math.random(),
            body: userMessage,
            author: "USER",
            chatSessionId: id,
        };
        setMessageHistory((prevMessages) => [...prevMessages, newMessage]);
        const aiRequest = {
            chatSessionId: id,
            userMessage: userMessage,
        };
        const response = await requestAiResponse(aiRequest);

        const newResponse = {
            messageId : Math.random(),
            body : response.response,
            author : "ASSISTANT",
            chatSessionId: id,
        }

        setMessageHistory((prevMessages) => [...prevMessages, newResponse]);
    }

    const clearInput = () => {
        document.getElementById("input").value = "";
        setHasInput(false);
    }

    useEffect(() => {
        const messagesContainer = document.getElementById("messages")
            messagesContainer.scrollTop = messagesContainer.scrollHeight;

    }, [messageHistory])

    return (
        <main className="flex flex-col items-center h-full px-1">
                <div className="text-center text-4xl py-4">
                    <h1>This is the header where the chat name will go</h1>
                </div>
                <div className="flex flex-col flex-1 gap-2.5 overflow-y-auto max-h-[600px] px-1 mb-3" id="messages">
                    {messageHistory.slice(1).map((message) => (
                        <MessageBubble key={message.messageId} message={message}/>
                    ))}
                </div>
                <div className="flex justify-center items-center gap-4 w-full pt-4">
                    <input type="text" placeholder="Message AI" className="input input-bordered w-full max-w-xl"
                           onChange={handleInputChange} onKeyDown={handleKeyPress} id="input"/>
                    <button onClick={handleSubmit}  className={`btn ${hasInput ? "btn-primary" : "btn-disabled"}`}>Send
                    </button>
                </div>
        </main>
    )


}

export default ChattingContainer;