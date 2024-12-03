import {useState} from "react";
import {DeleteAccount} from "../components/DeleteAccount.jsx";
import Loading from "../components/Loading.jsx";
import {AccountDetails} from "../components/AccountDetails.jsx";

/**
 * Profile settings for users
 * as of now, just where a user can change their password
 */
export const ProfilePage = () => {

    const [isLoading, setIsLoading] = useState(false);

    const handleLoading = () => {
        setIsLoading((prevState) => !prevState);
    }

    return (
        isLoading ? <div className="flex justify-center items-center pt-6">
            <div className="w-3/4 text-3xl">
                <Loading loadingMessage={"Handling Request"}/>
            </div>
        </div>
            : (<main className="flex flex-col items-center justify-center w-full h-full">
                <div className="flex-1 bg-accent p-4 w-1/2">
                    <h1 className="text-4xl pb-2">User Settings</h1>
                    <hr/>
                    <h2 className="text-xl pt-4 pb-2 italic">Account Details</h2>
                    <span className="flex flex-col  gap-3  p-2">
                        <AccountDetails startLoading={handleLoading}/>
                    <div className="pt-4">
                        <DeleteAccount startLoading={handleLoading}/>
                    </div>
                </span>
                </div>
            </main>)

    )
}