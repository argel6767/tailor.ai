import {createContext, useContext, useMemo, useState} from "react";

const GlobalContext = createContext();

export const GlobalProvider = ({children}) => {

    const [expiration, setExpiration] = useState(0);
    const [user, setUser] = useState(null);
    const [isSignedIn, setIsSignedIn] = useState(false);

    const contextValue = useMemo(() => ({
        expiration,
        setExpiration,
        user,
        setUser,
        isSignedIn,
        setIsSignedIn}), [ expiration, setExpiration, user, setUser, isSignedIn, setIsSignedIn]);


    return (
        <GlobalContext.Provider value={contextValue}>
            {children}
        </GlobalContext.Provider>
    )
}

export const useGlobalContext = () => useContext(GlobalContext);