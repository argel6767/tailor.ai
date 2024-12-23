import {useState} from "react";
import VerifyPage from "./VerifyPage.jsx";
import {InputEmailRequest} from "../components/InputEmailRequest.jsx";
import sendForgetPasswordVerificationEmail from "../api/auth/sendForgetPasswordVerificationEmail.js";
import {sleep} from "../utils/sleep.js";
import {useNavigate} from "react-router-dom";

export const ForgotPasswordPage = () => {

    const [email, setEmail] = useState("");
    const [failedRequest, setFailedRequest] = useState(false);
    const [successfulRequest, setSuccessfulRequest] = useState(false);
    const navigate = useNavigate();

    const submitResetPasswordVerification = async () => {
        try {
            await sendForgetPasswordVerificationEmail(email);
            sessionStorage.setItem("email", email);
            navigate("/reset-password")
        }
        catch (error) {
            setFailedRequest(true);
            await sleep(3000);
            setFailedRequest(false);
        }
    }

    return (
        <main>
            <VerifyPage title={"Forgot your password? Enter your email below."} renderResentComponent={false} button={"Send Email"} children={<InputEmailRequest setEmail={setEmail}/>} request={submitResetPasswordVerification}/>
            {failedRequest && <p className="text-center text-red-600">Request failed. Likely no account is tied to the email submitted. Try again</p>}
        </main>
    )
}