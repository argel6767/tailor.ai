
const MessageBubble = ({message}) => {
    const isAuthorAssistant = (message) => {
        return message.author === "ASSISTANT";
    }

    return (
        <div className={`chat ${isAuthorAssistant(message) ? "chat-start" : "chat-end"} flex-1`}>
            <div className={`chat-bubble ${isAuthorAssistant(message) ? "chat-bubble-accent" : "chat-bubble-primary"}`}>{message.body}</div>
        </div>
    )
}

export default MessageBubble