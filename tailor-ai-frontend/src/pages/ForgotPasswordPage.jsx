import {useState} from "react";
import VerifyPage from "./VerifyPage.jsx";
import {InputEmailRequest} from "../components/InputEmailRequest.jsx";
import sendForgetPasswordVerificationEmail from "../api/auth/sendForgetPasswordVerificationEmail.js";
import {sleep} from "../utils/sleep.js";
import {useNavigate} from "react-router-dom";
import Loading from "../components/Loading.jsx";

export const ForgotPasswordPage = () => {

    const [email, setEmail] = useState("");
    const [failedRequest, setFailedRequest] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const submitResetPasswordVerification = async () => {
        try {
            setIsLoading(true);
            await sendForgetPasswordVerificationEmail(email);
            sessionStorage.setItem("email", email);
            navigate("/reset-password")
        }
        catch (error) {
            setIsLoading(false);
            console.log(error);
            setFailedRequest(true);
            await sleep(3000);
            setFailedRequest(false);
        }
    }

    if (isLoading) {
        return (
            <div className="flex justify-center items-center pt-10">
            <div className="w-1/2"><Loading loadingMessage={"Submitting request..."} /></div>
            </div>);
    }

    return (
        <main>
            <VerifyPage title={"Forgot your password? Enter your email below."} renderResentComponent={false} button={"Send Email"} children={<InputEmailRequest setEmail={setEmail}/>} request={submitResetPasswordVerification}/>
            {failedRequest && <p className="text-center text-red-600">Request failed. Likely no account is tied to the email submitted. Try again</p>}
        </main>
    )
}