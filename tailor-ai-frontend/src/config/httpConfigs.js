

export const jwtHeader = (token) =>  {
    return  {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    };
};

export const fileHeader = (token) => {
    return {
        headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${token}`
        }
    };
};

export const blobHeader = (token) => {
    return {
        headers: {
            'Authorization': `Bearer ${token}`
        },
        responseType: 'blob'
    }
}