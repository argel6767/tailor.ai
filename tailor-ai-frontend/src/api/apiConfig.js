import axios from 'axios';

export const apiClient = axios.create({
    baseURL: import.meta.env.VITE_BACKEND_ENDPOINT,
    // Add this line to ensure cookies are sent with cross-origin requests
    withCredentials: true
});


export const failedCallMessage = (error) => {
    return `Something went wrong and the api call failed: ${error}`
};