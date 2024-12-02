import {useState} from "react";

/**
 * Profile settings for users
 * as of now, just where a user can change their password
 */
export const ProfilePage = () => {

    const [isChangingPassword, setIsChangingPassword] = useState(false);

    const handleIsChangingPassword = () => {
        setIsChangingPassword(!isChangingPassword);
    }

    return (
        <main className="flex flex-col items-center justify-center w-full pt-2">
            <div className="flex-1 bg-accent p-4">
                <h1>User Settings</h1>
                <hr/>
                <h2>Account Details</h2>
                <span className="flex flex-col items-center justify-center gap-2">
                    <p>Email: {localStorage.getItem("email")}</p>
                    <p>Password: <input type="password" placeholder="*********" className="input input-bordered input-sm" disabled={!isChangingPassword}/></p>
                    {isChangingPassword ?
                        <input type="password" placeholder="Enter new password" className="input input-bordered input-sm"/> :
                        <button className="btn btn-sm" onClick={handleIsChangingPassword}>Change Password</button>}
                </span>

            </div>
        </main>
    )
}