import {useState} from "react";
import changePassword from "../api/auth/changePassword.js";

/**
 * holds the changing of password component/logic
 */
export const AccountDetails = ({startLoading}) => {

    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");

    /*
     * request object
     */
    const changePasswordRequest = {
        "email":localStorage.getItem("email"),
        "oldPassword":oldPassword,
        "newPassword":newPassword,
    }

    const [isChangingPassword, setIsChangingPassword] = useState(false);

    const handleIsChangingPassword = () => {
        setIsChangingPassword(!isChangingPassword);
    }

    /*
     * handles deletion
     */
    const handleChangePassword = async () => {
        startLoading();
        const response = await changePassword(changePasswordRequest);
        if (response) {
            alert("Successfully changed password");  //TODO change this later to better looking alerting
        }
        else {
            alert("An error occurred. Please try again.");
        }
        startLoading();

    }

    return (
        <>
            <p>Email: {localStorage.getItem("email")}</p>
            <div className="flex justify-start">
                <p>Password: <input type="password" placeholder={isChangingPassword ? "Enter password" : "********"}
                        className="input input-sm w-7/12" disabled={!isChangingPassword} value={oldPassword} onChange={(e) => setOldPassword(e.target.value)}/></p>
                <button className={`btn-sm hover:underline ${isChangingPassword ? "invisible" : "visible"}`} onClick={handleIsChangingPassword}>Change Password</button>
            </div>
            <div className={`flex flex-col w-3/12 gap-3 ${isChangingPassword ? "visible" : "invisible"}`} data-testId={"newPasswordDiv"}>
                <input type="password" placeholder="Enter new password"
                       className="input input-sm" value={newPassword} onChange={(e) => setNewPassword(e.target.value)}/>
                <div className="flex justify-center items-center gap-3">
                    <button className="btn btn-primary flex-1"
                            onClick={handleIsChangingPassword}>Cancel
                    </button>
                    <button className="btn btn-primary w-6/12"
                            onClick={handleChangePassword}>Submit
                    </button>
                </div>
            </div>
        </>
    )
}