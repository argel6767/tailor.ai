import { createRoot } from 'react-dom/client'
import { BrowserRouter as Router } from 'react-router-dom'
import './index.css'
import App from './App.jsx'
import {GlobalProvider} from "./components/GlobalContext.jsx";

createRoot(document.getElementById('root')).render(
      <Router>
          <GlobalProvider>
              <App />
          </GlobalProvider>
      </Router>
)
