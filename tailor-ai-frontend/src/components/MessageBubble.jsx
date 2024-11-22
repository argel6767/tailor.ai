
const MessageBubble = ({message}) => {
    const isAuthorAssistant = (message) => {
        return message.author === "ASSISTANT";
    }

    return (
        <div className={`chat ${isAuthorAssistant(message) ? "chat-start" : "chat-end"} flex-1`}>
            <div className="chat-bubble chat-bubble-primary">{message.body}</div>
        </div>
    )
}

export default MessageBubble