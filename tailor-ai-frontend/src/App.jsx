import './App.css'
import {Route, Routes} from "react-router-dom";
import LandingPage from "./pages/LandingPage.jsx";
import AddingProfessionPage from "./pages/AddingProfessionPage.jsx";
import AuthPage from "./pages/AuthPage.jsx";
import Navbar from "./components/Navbar.jsx";
import VerifyPage from "./pages/VerifyPage.jsx";
import ChatDashboardPage from "./pages/ChatDashboardPage.jsx";
import ChatSessionPage from "./pages/ChatSessionPage.jsx";
import Footer from "./components/Footer.jsx";
import useCheckCookie from "./utils/useCheckCookie.js";
import {ProfilePage} from "./pages/ProfilePage.jsx";
import {useEffect, useState} from "react";
import {setCookie} from "./config/cookieConfig.js";

function App() {

    const [appKey, setAppKey] = useState(0);

    const [jwtToken, setToken] = useState("token")

    const refreshAppKey = () => {
        setAppKey((prev) => prev + 1);
    }

    const handleToken = (token) => {
        setCookie(token);
        setToken(token);
    }

    useEffect(() => {
        if (!sessionStorage.getItem('firstLoad')) {
            -sessionStorage.setItem('firstLoad', 'true');
            -window.location.reload();
        }
    }, []); //TODO FIX THIS LATER, THIS IS JUST A TEMP FIX TO VERCEL CACHING TOKENS!!!!

    useCheckCookie("/auth", ["/", "/auth", "", "/verify"], refreshAppKey);

  return (
      <div className="flex flex-col h-screen">
          <Navbar refreshApp={refreshAppKey} />
          <main className="flex-1 overflow-hidden">
              <Routes>
                  <Route path="/" element={<LandingPage/>}/>
                  <Route path="/auth" element={<AuthPage refreshApp={handleToken}/>}/>
                  <Route path="/verify" element={<VerifyPage/>}/>
                  <Route path="/profession" element={<AddingProfessionPage/>}/>
                  <Route path="/chats" element={<ChatDashboardPage/>}/>
                  <Route path="/chats/:id" element={<ChatSessionPage />} />
                  <Route path="/user" element={<ProfilePage/>}/>
              </Routes>
          </main>
          <Footer/>
      </div>
  )
}

export default App
