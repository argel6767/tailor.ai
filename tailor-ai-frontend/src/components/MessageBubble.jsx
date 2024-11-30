import Markdown from "react-markdown";
const MessageBubble = ({message}) => {
    const isAuthorAssistant = (message) => {
        return message.author === "ASSISTANT";
    }

    return (
        <div className={`chat ${isAuthorAssistant(message) ? "chat-start" : "chat-end"} flex-1`}>
            {
                isAuthorAssistant(message) ?
                    <div className="chat-bubble chat-bubble-accent"><Markdown>{message.body}</Markdown></div>
                    :
                    <div
                        className={"chat-bubble chat-bubble-primary"}>{message.body}</div>
            }
        </div>
    )
}

export default MessageBubble