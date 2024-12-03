import {useState} from "react";
import changePassword from "../api/auth/changePassword.js";

export const AccountDetails = ({startLoading}) => {

    const changePasswordRequest = {
        "email":localStorage.getItem("email"),
        "oldPassword":null,
        "newPassword":null,
    }


    const [isChangingPassword, setIsChangingPassword] = useState(false);

    const handleIsChangingPassword = () => {
        setIsChangingPassword(!isChangingPassword);
    }

    const handleChangePassword = async () => {
        startLoading();
        const response = await changePassword(changePasswordRequest);
        startLoading();
        if (response) {
            alert("200");
        }
        else {
            alert("Fail!!!")
        }
    }

    return (
        <>
            <p>Email: {localStorage.getItem("email")}</p>
            <div className="flex justify-start">
                <p>Password: <input type="password"
                                    placeholder={isChangingPassword ? "Enter password" : "********"}
                                    className="input input-sm w-7/12" disabled={!isChangingPassword}/></p>
                <button className={`btn-sm hover:underline ${isChangingPassword ? "invisible" : "visible"}`} onClick={handleIsChangingPassword}>Change Password</button>
            </div>
            <div className={`flex flex-col w-3/12 gap-3 ${isChangingPassword ? "visible" : "invisible"}`}>
                <input type="password" placeholder="Enter new password"
                       className="input input-sm"/>
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