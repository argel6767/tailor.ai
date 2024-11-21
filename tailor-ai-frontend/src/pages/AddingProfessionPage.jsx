import image from "../assets/pexels-jopwell-2422293.jpg"
import addUserProfession from "../api/user/addUserProfession.js";

const AddingProfessionPage = () => {

    const addProfessionRequest = {
        "email": localStorage.getItem("email"),
        "profession": null
    }

    const handleProfessionRequest = async () => {
        const profession = document.getElementById("profession-input").value;
        addProfessionRequest["profession"] = profession;
        console.log(addProfessionRequest);
        await addUserProfession(addProfessionRequest);
        window.location.href = "/chats"
    }

    return (
        <div className="flex p-8">
            <section className="flex items-center justify-center flex-1 w-1/3 ">
                <div className="flex flex-col w-2/4 gap-6">
                    <h1 className="text-5xl">Enter your desired profession.</h1>
                    <input type="text" placeholder="Profession" className="input input-bordered w-full max-w-xs" id="profession-input"/>
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