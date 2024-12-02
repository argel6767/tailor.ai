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
        <main className="flex flex-col items-center justify-center w-full">
            <div className="flex-1 bg-accent p-4 w-1/2">
                <h1 className="text-4xl pb-2">User Settings</h1>
                <hr/>
                <h2 className="text-xl pt-4 pb-2">Account Details</h2>
                <span className="flex flex-col max-w-60  gap-3  p-2">
                    <p>Email: {localStorage.getItem("email")}</p>
                    <p>Password: <input type="password" placeholder={isChangingPassword? "Enter password" : "********"
                    } className="input input-sm w-7/12" disabled={!isChangingPassword}/></p>
                    {isChangingPassword ?
                        <div className="flex flex-col w-10/12 gap-3">
                            <input type="password" placeholder="Enter new password"
                                   className="input input-sm"/>
                            <div className="flex justify-center items-center gap-3">
                                <button className="btn btn-primary flex-1" onClick={handleIsChangingPassword}>Cancel</button>
                                <button className="btn btn-primary w-6/12" onClick={handleIsChangingPassword}>Submit</button>
                            </div>

                        </div>
                        :
                        <div className="flex justify-center items-center">
                            <button className="btn btn-primary w-6/12" onClick={handleIsChangingPassword}>Change Password</button>
                        </div>
                    }
                </span>
            </div>
        </main>
    )
}