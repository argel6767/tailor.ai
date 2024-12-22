import resendVerificationEmail from "../api/auth/resendVerificationEmail.js";
import {sleep} from "../utils/sleep.js";
import {useState} from "react";

export const ResendVerificationEmail = () => {

    const[sentResendRequest, setSentResentRequest] = useState(false);

    const handleResendRequest = () => {
        setSentResentRequest(!sentResendRequest)
    }


    const handleResendTokenRequest = async () => {
        handleResendRequest();
        await resendVerificationEmail({"email":sessionStorage.getItem("email")});
        await sleep(2000)
        handleResendRequest()
    }

    return (
        <>
            <p className="pt-2.5">Need another verification code? <button className="underline"
                                                                          onClick={handleResendTokenRequest}>Request
                Another</button></p>
            <p className={`pt-2.5 ${sentResendRequest ? 'visible' : 'invisible'}`}>Verification code resent! Check your
                email.</p>
        </>
    )
}