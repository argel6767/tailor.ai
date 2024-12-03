import {useState} from "react";
import {DeleteAccount} from "../components/DeleteAccount.jsx";
import Loading from "../components/Loading.jsx";

/**
 * Profile settings for users
 * as of now, just where a user can change their password
 */
export const ProfilePage = () => {

    const [isChangingPassword, setIsChangingPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    const handleLoading = () => {
        setIsLoading((prevState) => !prevState);
    }

    const handleIsChangingPassword = () => {
        setIsChangingPassword(!isChangingPassword);
    }

    return (
        isLoading ? <Loading loadingMessage={"Deleting Account"}/> : (<main className="flex flex-col items-center justify-center w-full h-full">
                <div className="flex-1 bg-accent p-4 w-1/2">
                    <h1 className="text-4xl pb-2">User Settings</h1>
                    <hr/>
                    <h2 className="text-xl pt-4 pb-2 italic">Account Details</h2>
                    <span className="flex flex-col  gap-3  p-2">
                    <p>Email: {localStorage.getItem("email")}</p>
                    <div className="flex justify-start">
                        <p>Password: <input type="password"
                                            placeholder={isChangingPassword ? "Enter password" : "********"}
                                            className="input input-sm w-7/12" disabled={!isChangingPassword}/> </p>
                            <button className="btn-sm hover:underline" onClick={handleIsChangingPassword}>Change Password</button>
                    </div>
                        <div className={`flex flex-col w-3/12 gap-3 ${isChangingPassword ? "visible" : "invisible"}`}>
                            <input type="password" placeholder="Enter new password"
                                   className="input input-sm"/>
                            <div className="flex justify-center items-center gap-3">
                                <button className="btn btn-primary flex-1"
                                        onClick={handleIsChangingPassword}>Cancel</button>
                                <button className="btn btn-primary w-6/12"
                                        onClick={handleIsChangingPassword}>Submit</button>
                            </div>
                        </div>
                    <div className="pt-4">
                        <DeleteAccount startLoading={handleLoading}/>
                    </div>

                </span>
                </div>
            </main>)

    )
}