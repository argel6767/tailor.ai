import {useState} from "react";
import {InputEmailRequest} from "../components/InputEmailRequest.jsx";
import {VerificationCode} from "../components/VerificationCode.jsx";
import {PasswordInput} from "../components/PasswordInput.jsx";
import Loading from "../components/Loading.jsx";

import resetPassword from "../api/auth/resetPassword.js";
import {useNavigate} from "react-router-dom";

export const ResetPasswordPage = () => {


    const [newPassword, setNewPassword] = useState("");
    const [verificationCode, setVerificationCode] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleResetPassword = async () => {
        setIsLoading(true);
        const request = {
            "password": newPassword,
            "verificationCode": verificationCode,
        }
        await resetPassword(request);
        setIsLoading(false);
        navigate("/auth")
    }

    if (isLoading) {
        return (
            <div className="flex justify-center items-center pt-10">
                <div className="w-1/2"><Loading loadingMessage={"Resetting password..."} /></div>
            </div>)
    }

    return (
        <main>
            <div className="flex flex-col justify-center items-center gap-6 pt-4">
                <h1 className="text-center text-3xl">Reset password below.</h1>
                <InputEmailRequest setEmail={setEmail} />
                <PasswordInput setPassword={setNewPassword} />
                <VerificationCode setCode={setVerificationCode} />
                <button className="btn btn-primary" onClick={handleResetPassword}>Reset Password</button>
            </div>

        </main>
    )
}