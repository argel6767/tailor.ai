import axios from 'axios';

export const apiClient = axios.create({
    baseURL: import.meta.env.VITE_BACKEND_ENDPOINT,
    withCredentials: true
});


export const failedCallMessage = (error) => {
    return `Something went wrong and the api call failed: ${error}`
};