import verifyUser from "../api/verifyUser.js";
import resendVerificationEmail from "../api/resendVerificationEmail.js";
import {Navigate} from "react-router-dom";
import {useState} from "react";

const VerifyPage = () => {

    const[sentToken, setSentToken] = useState(false);

    const handleSentToken = () => {
        setSentToken(!sentToken);
    }

    const verifyRequest = {
        "email":localStorage.getItem("email"),
        "verificationToken":null,
    }

    const submitVerificationRequest = async () => {
        const verificationToken = document.getElementById("verify-input").value;
        verifyRequest.verificationToken = verificationToken;
        console.log(verifyRequest);
        await verifyUser(verifyRequest)
        localStorage.clear()
        window.location.href = "/auth";
    }

    const handleResendTokenRequest = async () => {
        handleSentToken();
        await resendVerificationEmail(verifyRequest.email);
        await sleep(2000)
        handleSentToken()
    }

    const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));


    return (<div className="flex justify-center items-center pt-20">
        <div className="flex flex-col  justify-center items-center p-2 space-y-7 w-2/5  ">
            <h1 className="text-3xl font-bold">Enter your verification code below.</h1>
            <label className="input input-bordered flex items-center gap-2 bg-primary">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 16 16"
                    fill="currentColor"
                    className="h-4 w-4 opacity-70">
                    <path
                        fillRule="evenodd"
                        d="M14 6a4 4 0 0 1-4.899 3.899l-1.955 1.955a.5.5 0 0 1-.353.146H5v1.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-2.293a.5.5 0 0 1 .146-.353l3.955-3.955A4 4 0 1 1 14 6Zm-4-2a.75.75 0 0 0 0 1.5.5.5 0 0 1 .5.5.75.75 0 0 0 1.5 0 2 2 0 0 0-2-2Z"
                        clipRule="evenodd"/>
                </svg>
                <input type="tel" maxLength={6} pattern={"/d*"}  className="grow" placeholder="Verification Code" id={"verify-input"}/>
            </label>
            <button className="btn btn-active btn-primary" onClick={submitVerificationRequest}>Verify Account</button>
            <p className="pt-2.5">Need another verification code? <button className="underline" onClick={handleResendTokenRequest}>Request Another</button></p>
            <p className={`pt-2.5 ${sentToken ? 'visible' : 'invisible'}`}>Verification code resent! Check your email.</p>
        </div>
    </div>)
}

export default VerifyPage;