import {useState} from "react";
import deleteUser from "../api/user/deleteUser.js";
import {useNavigate} from "react-router-dom";

export const DeleteAccount = ({startLoading}) => {

    const [isConfirming, setIsConfirming] = useState(false);

    const navigate = useNavigate();

    const handleIsConfirming = () => {
        setIsConfirming(!isConfirming);
    }

    const handleDelete = async () => {
        startLoading();
        const email = localStorage.getItem("email");
        await deleteUser(email);
        navigate("/");
    }

    return (
        <main className="flex flex-col items-center justify-center w-full h-full gap-6">
            <button className="btn btn-primary w-6/12" onClick={handleIsConfirming}>Delete Account</button>
            {isConfirming && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="p-6 rounded shadow-lg flex flex-col gap-5 bg-background">
                        <p>Are you sure? This cannot be undone.</p>
                        <div className="flex justify-center items-center gap-6">
                            <button className="hover:underline" onClick={handleIsConfirming}>Cancel</button>
                            <button className="hover:underline" onClick={handleDelete}>Confirm</button>
                        </div>
                    </div>

                </div>
            )}
        </main>
    )
}