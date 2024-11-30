import {useState} from "react";
import tutorial from "../../public/Get_Job_Url.mov"
import {sleep} from "../utils/sleep.js";

/**
 * returns a pop-up component that houses a submission input and video tutorial for getting desired LinkedIn URL
 * for easier user experience
 * also uses a callback to send back up the entered link if the user submitted one
 */
const UploadJob = ({sendUpUrl}) => {

    const [isPoppedUp, setIsPoppedUp] = useState(false);
    const [isValidJobGiven, setIsValidJobGiven] = useState(true);
    const [hasSubmittedUrl, setHasSubmittedUrl] = useState(false);

    const handlePopUp = () => {
        setIsPoppedUp(!isPoppedUp);
    }

    const handleSubmit = async () => {
        const urlInput = document.getElementById("url-input");
        const urlValue = urlInput.value;
        if (isValidUrl(urlValue)) {
            setHasSubmittedUrl(true);
            sendUrl(urlValue);
            await sleep(2000);
            setIsPoppedUp(false);
            setHasSubmittedUrl(false);
        }
        else {
            setIsValidJobGiven(false);
            await sleep(3000);
            setIsValidJobGiven(true);
            urlInput.value = "";
        }
    }

    const isValidUrl = (url) => {
        const regex = /^https:\/\/www\.linkedin\.com\/jobs\/view\/\d+$/;
        return regex.test(url);
    }

    const sendUrl = (url) => {
        sendUpUrl(url);
    }

    return (
        <main>
            <div className="flex flex-col justify-center items-center w-full gap-2">
                <button className="btn btn-primary flex-1" onClick={handlePopUp}>Submit a LinkedIn Job</button>
                <p className="text-sm text-gray-500 italic">-Optional</p>
            </div>
            {isPoppedUp && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="p-6 rounded shadow-lg flex flex-col gap-5 bg-background">
                        <h1 className="text-2xl">Paste the job URL below.</h1>
                        <div className="flex-1 flex justify-center items-center">
                            <video data-testId="video-tutorial" className="flex-1 max-w-lg" autoPlay loop muted controls>
                                <source src={tutorial}/>
                                This will have a video tutorial
                            </video>
                        </div>
                        <div className="flex justify-center items-center gap-6">
                            <input type="text" placeholder="https://www.linkedin.com/jobs/views/job_id"
                                   className="input input-bordered w-full max-w-lg" id="url-input"/>
                            <button className="btn btn-primary" onClick={handleSubmit}>Submit</button>
                        </div>
                        {!isValidJobGiven && (
                            <p className="text-center text-red-600">Not a valid LinkedIn URL given. Try again</p>
                        )}
                        {hasSubmittedUrl && (
                            <div className="alert bg-primary text-center">
                                <span className="text-center">URL Submitted</span>
                            </div>
                        )}
                        <button className="btn btn-primary" onClick={handlePopUp}>Close</button>
                    </div>
                </div>
            )}
        </main>
    )
}

export default UploadJob;