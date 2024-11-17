import {useState} from "react";
import {useNavigate} from "react-router-dom";
import registerUser from "../api/registerUser.js";
import loginUser from "../api/loginUser.js";
import {handleLoginNavigation} from "../utils/handleLoginNavigation.js";

/**
 * The AuthPage (login or sign up)
 * uses useState to handle whether or user has to log in or sign up
 */
const AuthPage = () => {

    const navigate = useNavigate();

    const goToVerify = () => {
        navigate('/verify');
    }

    const [hasAccount, setHasAccount] = useState(true);


    const handleAccountChange = () => {
        setHasAccount(!hasAccount);
    }

    const authRequestValues = {
        "username":null,
        "password":null,
    }

    /**
     * submits the values given by user to the endpoint for logging in or signing up
     * then routes user to appropriate page based on status
     */
    const submitAuthRequestValues = async () => {
        const email = document.getElementById("email-input").value;
        const password = document.getElementById("password-input").value;
        authRequestValues.username = email;
        authRequestValues.password = password;
        localStorage.setItem("email", email);
        console.log(authRequestValues);
        hasAccount?  await login(authRequestValues) : await register(authRequestValues);
    }

    const login = async (authRequestValues) => {
        await loginUser(authRequestValues);
        await handleLoginNavigation(navigate);
    }

    const register = async (authRequestValues) => {
        await registerUser(authRequestValues);
        goToVerify();
    }

    return (
        <div className="flex justify-center items-center pt-20">
            <div className="flex flex-col  justify-center items-center p-2 space-y-6 w-2/5  ">
                <h1 className="text-3xl font-bold text-center pb-1">
                    {hasAccount ? "Welcome back, sign in to your account." : "Welcome to Tailor.ai, sign up here."}
                </h1>
                <label className="input input-bordered flex items-center gap-2 bg-primary">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 16 16"
                        fill="currentColor"
                        className="h-4 w-4 opacity-70">
                        <path
                            d="M2.5 3A1.5 1.5 0 0 0 1 4.5v.793c.026.009.051.02.076.032L7.674 8.51c.206.1.446.1.652 0l6.598-3.185A.755.755 0 0 1 15 5.293V4.5A1.5 1.5 0 0 0 13.5 3h-11Z"/>
                        <path
                            d="M15 6.954 8.978 9.86a2.25 2.25 0 0 1-1.956 0L1 6.954V11.5A1.5 1.5 0 0 0 2.5 13h11a1.5 1.5 0 0 0 1.5-1.5V6.954Z"/>
                    </svg>
                    <input type="text" className="grow" placeholder="Email" id={"email-input"}/>
                </label>
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
                    <input type="password" className="grow" placeholder="Password" id={"password-input"}/>
                </label>
                <button className="btn btn-active btn-primary" onClick={submitAuthRequestValues}>
                    {hasAccount ? "Sign in" : "Sign up"}
                </button>
                {hasAccount ?
                    <p>Not have an account? <button id={"switchToSignUp"} className="underline" onClick={handleAccountChange}>Sign Up</button></p> :
                    <p>Already have an account? <button id={"switchToSignIn"} className="underline" onClick={handleAccountChange}>Sign In</button></p>
                }
            </div>
        </div>
    )
}
export default AuthPage