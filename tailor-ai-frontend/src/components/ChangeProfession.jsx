import {useState} from "react";
import confirmButton from "../assets/confirm_button.svg"
import addUserProfession from "../api/user/addUserProfession.js";
import {useGlobalContext} from "./GlobalContext.jsx";


export const ChangeProfession = ({profession}) => {

    const [currentProfession, setCurrentProfession] = useState(profession);
    const [isChangingProfession, setIsChangingProfession] = useState(false);
    const {token} = useGlobalContext();
    const {setUser} = useGlobalContext();


    const request = {
        "email": sessionStorage.getItem("email"),
        "profession": null,
    }

    const handleChangingProfession = () => {
        setIsChangingProfession(!isChangingProfession);
    }

    const submitNewProfession =  () => {
        const professionValue = document.getElementById("professionInput").value;
        if (professionValue.length > 0) {
            request.profession = professionValue;
            setCurrentProfession(professionValue);
            setUser((prev) => ({
                ...prev,
                profession: professionValue, // Update the profession field
            }));
            setIsChangingProfession(false);
            addUserProfession(request, token);
        }
    }

    return (
        <main>
            {isChangingProfession ?
                <div className="flex justify-start items-center gap-3">
                    <div className="flex justify-start items-center">
                        <p>Profession: <input placeholder={profession} className="input input-sm w-7/12" id={"professionInput"}/></p>
                        <button className="btn-sm bg-primary btn-circle" onClick={submitNewProfession}><img src={confirmButton} alt={"confirm button"}/></button>
                    </div>
                    <button className="btn-sm hover:underline" onClick={handleChangingProfession}> Cancel</button>
                </div>
                : <div className="flex justify-start items-center gap-[6.5rem]">
                    <p>Profession: {currentProfession}</p>
                    <button className="btn-sm hover:underline" onClick={handleChangingProfession}> Change Profession
                    </button>
                </div>}
        </main>
    )
}