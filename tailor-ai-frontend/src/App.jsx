import { useState } from 'react'
import './App.css'
import {Route, Routes} from "react-router-dom";
import LandingPage from "./pages/LandingPage.jsx";
import AddingProfessionPage from "./pages/AddingProfessionPage.jsx";
import ChatPage from "./pages/ChatPage.jsx";
import AuthPage from "./pages/AuthPage.jsx";

function App() {

  return (
      <Routes>
        <Route path="/" component={LandingPage}/>
        <Route path="/auth" component={AuthPage}/>
        <Route path="/profession" component={AddingProfessionPage}/>
        <Route path"/chats" component={ChatPage}/>
      </Routes>
  )
}

export default App
