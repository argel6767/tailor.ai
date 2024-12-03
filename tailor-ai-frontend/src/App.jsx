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

function App() {

    const [appKey, setAppKey] = useState(0);

    const refreshAppKey = () => {
        setAppKey((prev) => prev + 1);
    }
    useCheckCookie("/auth", ["/", "/auth", "", "verify"], refreshAppKey);

    useEffect(() => {
        if (window.performance && performance.navigation.type === 1) {
            // Force reload when navigating directly to the site
            window.location.reload();
        }
    }, []); //TODO this is a temp solution to caching, FIX IT LATER!!!


  return (
      <div className="flex flex-col h-screen">
          <Navbar refreshApp={refreshAppKey} />
          <main className="flex-1 overflow-hidden">
              <Routes>
                  <Route path="/" element={<LandingPage/>}/>
                  <Route path="/auth" element={<AuthPage refreshApp={refreshAppKey}/>}/>
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
