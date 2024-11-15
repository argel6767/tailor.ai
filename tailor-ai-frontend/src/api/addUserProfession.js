import axios from 'axios';

const addUserProfession = async (addProfessionRequest) => {
    try {
        const response = await axios.post('http://localhost:8080/user/profession', addProfessionRequest);
        console.log(response.data);
    }
    catch (error) {
        console.log(error.response? error.response.data : error);
    }
}

export default addUserProfession;