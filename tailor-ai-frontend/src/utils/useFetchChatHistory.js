import {useEffect} from "react";
import {getChatHistory} from "../api/message/getChatHistory.js";

export const useFetchChatHistory = (id, ) => {
    let chats = []

    useEffect(() => {
        const fetchChatHistory = async () => {
            chats = await getChatHistory(id);
        }
        fetchChatHistory();
    },[id]);

    return chats;
}