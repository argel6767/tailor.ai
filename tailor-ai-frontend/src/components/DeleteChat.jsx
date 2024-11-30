import {Link} from "react-router-dom";
import confirmButton from "../assets/confirm_button.svg";
import cancelButton from "../assets/cancel_button.svg";
import deleteButton from "../assets/delete_button.svg";
import {useState} from "react";

export const DeleteChat = ({chatSessionId, onDelete}) => {

    const [confirmDelete, setConfirmDelete] = useState(false);
    const [chatSessionIdDelete, setChatSessionIdDelete] = useState(null);

    const deleteConfirmation = () => {
        handleChatSessionIdDelete(chatSessionId);
        handleConfirmDelete();
    }

    const handleConfirmDelete = () => {
        setConfirmDelete(!confirmDelete);
    }

    const handleChatSessionIdDelete = () => {
        setChatSessionIdDelete(chatSessionId);
    }

    const isDeleteConfirmationActive = () => {
        return confirmDelete && chatSessionIdDelete === chatSessionId;
    }

    const handleChatDeletion = () => {

    }

    return (
        <div className="flex justify-center items-end gap-3">
            {isDeleteConfirmationActive() && (
                <button className="btn btn-circle bg-secondary hover:bg-accent">
                    <img src={confirmButton} alt="Delete" className="w-6 h-6"/>
                </button>
            )}
            <button onClick={() => {
                deleteConfirmation()
            }}
                    className={`btn btn-circle ${isDeleteConfirmationActive() ? "bg-red-500" : "bg-secondary"} hover:bg-accent`}>
                <img src={isDeleteConfirmationActive() ? cancelButton : deleteButton} alt="Delete"
                     className="w-6 h-6"/>
            </button>
        </div>
    )
}