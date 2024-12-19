import {createContext, useContext, useMemo, useState} from "react";

const GlobalContext = createContext();

export const GlobalProvider = ({children}) => {

    const [token, setToken] = useState(null);
    const [expiration, setExpiration] = useState(0);
    const [user, setUser] = useState(null);

    const contextValue = useMemo(() => ({ token,
        setToken,
        expiration,
        setExpiration,
        user,
        setUser}), [token, expiration, user]);


    return (
        <GlobalContext.Provider value={contextValue}>
            {children}
        </GlobalContext.Provider>
    )
}

export const useGlobalContext = () => useContext(GlobalContext);