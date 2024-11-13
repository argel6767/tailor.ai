import { useState } from 'react'
import './App.css'
import {Route, Routes} from "react-router-dom";
import LandingPage from "./pages/LandingPage.jsx";
import AddingProfessionPage from "./pages/AddingProfessionPage.jsx";
import ChatPage from "./pages/ChatPage.jsx";
import AuthPage from "./pages/AuthPage.jsx";
import Navbar from "./components/Navbar.jsx";

function App() {

  return (
      <>
        <Navbar />
        <Routes>
          <Route path="/" element={<LandingPage/>}/>
          <Route path="/auth" element={<AuthPage/>}/>
          <Route path="/profession" element={<AddingProfessionPage/>}/>
          <Route path="/chats" element={<ChatPage/>}/>
        </Routes>
      </>
  )
}

export default App
