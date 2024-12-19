import {useState} from "react";
import {FilePopUp} from "./FilePopUp.jsx";

export const RenderFile = () => {
    const [isPoppedUp, setIsPoppedUp] = useState(false)

    const handlePoppedUp = () => {
        setIsPoppedUp(!isPoppedUp)
    }

    const closePopUp = () => {
        setIsPoppedUp(false)
    }


    return (
        <main>
            {isPoppedUp &&
            <span className="h-screen">
                <FilePopUp closePopUp={closePopUp}/>
            </span> }
            <button className="btn btn-primary" onClick={handlePoppedUp}>View File</button>
        </main>
    )
}