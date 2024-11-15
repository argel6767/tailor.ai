import './App.css'
import {Route, Routes} from "react-router-dom";
import LandingPage from "./pages/LandingPage.jsx";
import AddingProfessionPage from "./pages/AddingProfessionPage.jsx";
import AuthPage from "./pages/AuthPage.jsx";
import Navbar from "./components/Navbar.jsx";
import VerifyPage from "./pages/VerifyPage.jsx";
import ChatDashboardPage from "./pages/ChatDashboardPage.jsx";
import Footer from "./components/Footer.jsx";

function App() {

  return (
      <div className="flex flex-col h-screen">
          <Navbar />
          <main className="flex-1">
              <Routes>
                  <Route path="/" element={<LandingPage/>}/>
                  <Route path="/auth" element={<AuthPage/>}/>
                  <Route path="/verify" element={<VerifyPage/>}/>
                  <Route path="/profession" element={<AddingProfessionPage/>}/>
                  <Route path="/chats" element={<ChatDashboardPage/>}/>
              </Routes>
          </main>
          <Footer/>
      </div>
  )
}

export default App
