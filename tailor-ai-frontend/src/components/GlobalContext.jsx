import {createContext, useContext, useMemo, useState} from "react";

const GlobalContext = createContext();

export const GlobalProvider = ({children}) => {

    const [token, setToken] = useState(null);

    const contextValue = useMemo(() => ({ token, setToken }), [token]);


    return (
        <GlobalContext.Provider value={contextValue}>
            {children}
        </GlobalContext.Provider>
    )
}

export const useGlobalContext = () => useContext(GlobalContext);