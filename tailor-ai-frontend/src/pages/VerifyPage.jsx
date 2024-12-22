import verifyUser from "../api/auth/verifyUser.js";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {VerificationCode} from "../components/VerificationCode.jsx";
import {ResendVerificationEmail} from "../components/ResendVerificationEmail.jsx";

/**
 * Verify page, where user puts in verification code
 */
const VerifyPage = ({title, button, renderResentComponent}) => {

    /*
     * TODO possible at state checking if localstorage has email
     *  ie the user is either from sign up or is pressing the link from email to allow for inputting email too if necessary
     *
     */
    const navigate = useNavigate();

    const [code, setCode] = useState("");

    const handleSentToken = () => {
        setSentToken(!sentToken);
    }

    const verifyRequest = {
        "email":sessionStorage.getItem("email"),
        "verificationToken":null,
    }

    const submitVerificationRequest = async () => {
        const verificationToken = document.getElementById("verify-input").value;
        verifyRequest.verificationToken = verificationToken;
        console.log(verifyRequest);
        await verifyUser(verifyRequest)
        sessionStorage.clear();
        navigate("/auth")
    }

    const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));


    return (<div className="flex justify-center items-center pt-20">
        <div className="flex flex-col  justify-center items-center p-2 space-y-7 w-2/5  ">
            <h1 className="text-3xl font-bold">Enter your verification code below.</h1>
            <VerificationCode setCode={setCode} />
            <button className="btn btn-active btn-primary" onClick={submitVerificationRequest}>Verify Account</button>
            {renderResentComponent && <ResendVerificationEmail/>}
        </div>
    </div>)
}

export default VerifyPage;