import {useState} from "react";
import {useNavigate} from "react-router-dom";
import registerUser from "../api/auth/registerUser.js";
import loginUser from "../api/auth/loginUser.js";
import {handleLoginNavigation} from "../utils/handleLoginNavigation.js";
import {emailObjectRequest} from "../api/requests/emailObjectRequest.js";
import {validateEmail} from "../utils/validateEmail.js";
import Loading from "../components/Loading.jsx";
import {sleep} from "../utils/sleep.js";
import {useGlobalContext} from "../components/GlobalContext.jsx";

/**
 * The AuthPage (login or sign up)
 * uses useState to handle whether or user has to log in or sign up
 */
const AuthPage = () => {

    const navigate = useNavigate();
    const loginNavigationRequest = emailObjectRequest;
    const {setToken} = useGlobalContext();
    const {setExpiration} = useGlobalContext();

    const goToVerify = () => {
        navigate('/verify');
    }

    const [hasAccount, setHasAccount] = useState(true);
    const[email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const[isValidEmail, setValidEmail] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [authFailed, setAuthFailed] = useState(false);


    const handleAccountChange = () => {
        setHasAccount(!hasAccount);
    }

    const handleFailedAuth = () => {
        setAuthFailed(!authFailed);
    }

    const authRequestValues = {
        "username":null,
        "password":null,
    }

    const handleLoading = () => {
        setIsLoading(!isLoading);
    }


    /**
     * submits the values given by user to the endpoint for logging in or signing up
     * then routes user to appropriate page based on status
     */
    const submitAuthRequestValues = async () => {
        handleLoading();
        authRequestValues.username = email;
        authRequestValues.password = password;
        loginNavigationRequest.email = email;
        sessionStorage.setItem("email", email);
        hasAccount?  await login(authRequestValues) : await register(authRequestValues);
    }

    const login = async (authRequestValues) => {
        const response = await loginUser(authRequestValues);
        if (!response) {
            setIsLoading(false);
            handleFailedAuth();
            await sleep(3000);
            setAuthFailed(false);
        }
        else {
            setToken(response.data.token);
            const jwt = response.data.token;
            setExpiration(Date.now() + response.data.expiresIn);
            await handleLoginNavigation(navigate, email, jwt);
        }
    }

    const register = async (authRequestValues) => {
        const data = await registerUser(authRequestValues);
        if (!data) {
            setIsLoading(false);
            handleFailedAuth();
            await sleep(3000);
            setAuthFailed(false);
        }
        else {
            goToVerify();
        }

    }

    return (
        <div className="flex justify-center items-center pt-20">
            <div className="flex flex-col  justify-center items-center p-2 space-y-6 w-2/5  ">
                <h1 className="text-3xl font-bold text-center pb-2">
                    {hasAccount ? "Welcome back, sign in to your account." : "Welcome to Tailor.ai, sign up here."}
                </h1>
                {isLoading ? (<Loading loadingMessage={"Speaking with the backend..."}/>) : (<>
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
                        <input type="text" className="grow" placeholder="Email" id={"email-input"} value={email}
                               onChange={(e) => {
                                   setEmail(e.target.value.toLowerCase());
                                   setValidEmail(validateEmail(email));
                               }}/>
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
                        <input type="password" className="grow" placeholder="Password" id={"password-input"}
                               value={password}
                               onChange={(e) => setPassword(e.target.value)}/>
                    </label>
                    {authFailed? <p className="text-center text-red-600">Auth request failed. Reload page and try again.</p> :
                    <button className={`btn btn-active btn-primary`}
                            onClick={submitAuthRequestValues} disabled={!email || !isValidEmail || !password}>
                        {hasAccount ? "Sign in" : "Sign up"}
                    </button>}

                    {hasAccount ?
                        <p>Not have an account? <button id={"switchToSignUp"} className="underline"
                                                        onClick={handleAccountChange}>Sign Up</button></p> :
                        <p>Already have an account? <button id={"switchToSignIn"} className="underline"
                                                            onClick={handleAccountChange}>Sign In</button></p>}
                </>)}
            </div>
        </div>
    )
}
export default AuthPage
