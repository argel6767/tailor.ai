import image from "../assets/pexels-jopwell-2422293.jpg"
import addUserProfession from "../api/user/addUserProfession.js";
import {useNavigate} from "react-router-dom";
import {useGlobalContext} from "../components/GlobalContext.jsx";
import {useState} from "react";

const AddingProfessionPage = () => {

    const [profession, setProfession] = useState("");
    const navigate = useNavigate();
    const {token} = useGlobalContext();

    const handleProfessionRequest = async () => {
        const addProfessionRequest = {
            "email": sessionStorage.getItem("email"),
            "profession": profession
        }
        addProfessionRequest.profession = profession;
        await addUserProfession(addProfessionRequest, token);
        navigate("/chats");
    }

    return (
        <div className="flex p-8">
            <section className="flex items-center justify-center flex-1 w-1/3 ">
                <div className="flex flex-col w-2/4 gap-6">
                    <h1 className="text-5xl">Enter your desired profession.</h1>
                    <input type="text" placeholder="Profession" className="input input-bordered w-full max-w-xs" id="profession-input"
                           onChange={(e) => setProfession(e.target.value)} />
                    <button className="btn bg-primary" onClick={handleProfessionRequest}>Submit</button>
                </div>
            </section>
            <section className="flex-1 w-2/3">
                <img src={image} alt={"Workers"}/>
            </section>
        </div>
    )
}
export default AddingProfessionPage