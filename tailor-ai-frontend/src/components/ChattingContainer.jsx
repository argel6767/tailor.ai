import MessageBubble from "./MessageBubble.jsx";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import createMessage from "../api/message/createMessage.js";
import requestAiResponse from "../api/ai/requestAiResponse.js";
import getChatSession from "../api/chat_session/getChatSession.js";
import changeChatSessionName from "../api/chat_session/changeChatSessionName.js";
import {useGlobalContext} from "./GlobalContext.jsx";
import {RenderFile} from "./RenderFile.jsx";


const ChattingContainer = ({initialMessageHistory}) => {

    const [messageHistory, setMessageHistory] = useState(initialMessageHistory);
    const {id} = useParams();
    const [hasInput, setHasInput] = useState(false);
    const [chatName, setChatName] = useState("");
    const [hasClickedChatName, setClickedChatName] = useState(false);
    const [isLoading, setIsLoading] = useState(false);


    useEffect(() => {
        const getName = async () => {
            const response = await getChatSession(id);
            setChatName(response.chatSessionName);
        }
        getName()
    }, [id]);

    const updateChatName = async () => {
        const chatSessionName = document.getElementById("chatSessionName").value;
        if (chatSessionName !== "") {
            setChatName(chatSessionName);
            await changeChatSessionName(id, chatSessionName);
        }
        handleNameClick()

    }

    const handleNameClick = () => {
        setClickedChatName(!hasClickedChatName);
    }

    const createMessageRequest = {
        "message": null,
        "author": "USER"
    }

    const handleInputChange = () => {
        setHasInput(true);
    }

    const handleKeyPress = async (e) => {
        if (e.key === "Enter") {
            await handleSubmit();
        }
    }

    /*
     * submits user message to both db and as the prompt for the ai for a response
     */
    const handleSubmit = async () => {
        const userMessage = document.getElementById("input").value;
        createMessageRequest.message = userMessage;
        clearInput()
        setIsLoading(true);
        await createMessage(createMessageRequest, id, );
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
        const response = await requestAiResponse(aiRequest, );

        const newResponse = {
            messageId : Math.random(),
            body : response.response,
            author : "ASSISTANT",
            chatSessionId: id,
        }
        setIsLoading(false);
        setMessageHistory((prevMessages) => [...prevMessages, newResponse]);
    }

    const clearInput = () => {
        document.getElementById("input").value = "";
        setHasInput(false);
    }

    /*
     * scroll effect, so the newest message is seen when it is put on the ui
     */
    useEffect(() => {
        const messagesContainer = document.getElementById("messages")
            messagesContainer.scrollTop = messagesContainer.scrollHeight;

    }, [messageHistory])

    return (
        <main className="flex flex-col items-center h-full px-1">
                <div className="text-center text-4xl py-4 cursor-pointer pb-4">
                    {hasClickedChatName?
                        <div className="flex items-center justify-center flex-1 gap-3">
                            <input type="text" placeholder={chatName} className="input w-full input-ghost max-w-xl text-4xl" id="chatSessionName"/>
                            <button className="btn btn-sm" onClick={updateChatName}>Save</button>
                        </div>
                        : <h1 onClick={handleNameClick}>{chatName}</h1>
                    }
                </div>
            <div className="flex flex-col flex-1 gap-2.5 overflow-y-auto max-h-[600px] px-1 mb-3" id="messages">
                {messageHistory.slice(1).map((message) => (
                    <MessageBubble key={message.messageId} message={message}/>
                    ))}
                {isLoading ? <div className="chat chat-start flex-1">
                    <div
                        className="chat-bubble chat-bubble-accent">
                        <span className="loading loading-dots w-8"></span>
                    </div>
                </div> : null}
            </div>
            <div className="flex justify-center items-center gap-4 w-full pt-4">
                <RenderFile chatSessionId={id} />
                <input type="text" placeholder="Message AI" className="input input-bordered w-full max-w-xl" onChange={handleInputChange} onKeyDown={handleKeyPress} id="input" />
                    <button onClick={handleSubmit}  className={`btn ${hasInput ? "btn-primary" : "btn-disabled"}`}>Send</button>
                </div>
        </main>
    )


}

export default ChattingContainer;