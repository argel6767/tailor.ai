import './App.css'
import {Route, Routes} from "react-router-dom";
import LandingPage from "./pages/LandingPage.jsx";
import AddingProfessionPage from "./pages/AddingProfessionPage.jsx";
import AuthPage from "./pages/AuthPage.jsx";
import Navbar from "./components/Navbar.jsx";
import VerifyPage from "./pages/VerifyPage.jsx";
import ChatDashboardPage from "./pages/ChatDashboardPage.jsx";
import ChatSessionPage from "./pages/ChatSessionPage.jsx";
import {ForgotPasswordPage} from "./pages/ForgotPasswordPage.jsx";
import Footer from "./components/Footer.jsx";
import {ProfilePage} from "./pages/ProfilePage.jsx";
import {ResetPasswordPage} from "./pages/ResetPasswordPage.jsx";

function App() {

  return (
          <main className="flex flex-col h-screen">
              <Navbar/>
              <div className="flex-1 overflow-hidden">
                  <Routes>
                      <Route path="/" element={<LandingPage/>}/>
                      <Route path="/auth" element={<AuthPage/>}/>
                      <Route path="/verify" element={<VerifyPage title={"Enter your verification code below."} button={"Verify Account"} renderResentComponent={true}/>}/>
                      <Route path="/forgot" element={<ForgotPasswordPage/>}/>
                      <Route path="/reset-password" element={<ResetPasswordPage/>}/>
                      <Route path="/profession" element={<AddingProfessionPage/>}/>
                      <Route path="/chats" element={<ChatDashboardPage/>}/>
                      <Route path="/chats/:id" element={<ChatSessionPage/>}/>
                      <Route path="/user" element={<ProfilePage/>}/>
                  </Routes>
              </div>
              <Footer/>
          </main>
  );
}

export default App
