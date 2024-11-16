import axios from "axios";
import jwtToken from "../config/jwtToken.js";

const getHasSetProfession = async () => {
    try {
        const response = await axios.get("https://localhost:8080/profession", jwtToken);
        return response.data;
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default getHasSetProfession;