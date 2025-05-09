import axios from 'axios';

export const apiClient = axios.create({
    baseURL: '/api',
    // Add this line to ensure cookies are sent with cross-origin requests
    withCredentials: true
});


export const failedCallMessage = (error) => {
    return `Something went wrong and the api call failed: ${error}`
};