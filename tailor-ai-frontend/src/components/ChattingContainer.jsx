import {
    ChatContainer,
    ConversationHeader,
    MainContainer,
    Message,
    MessageInput,
    MessageList
} from "@chatscope/chat-ui-kit-react";

const ChattingContainer = ({messageHistory}) => {
    const messages = messageHistory;

    const determineDirection = (message) => {
        if (message.author === "ASSISTANT") {
            return "incoming";
        }
        return "outgoing";
    }

    return (
        <main className="p-3">
            <div className="text-center">
                <h1>This is the header where the chat name will go</h1>
            </div>
            <div>
            {messages.slice(1).map((message, index) => (
                <div className="chat chat-end">
                    <div className="chat-bubble chat-bubble-primary" key={index}>{message.body}</div>
                </div>))}
            </div>
        </main>
    )


}

export default ChattingContainer;