import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import {BrowserRouter, Navigate, Route, Routes} from "react-router";
import Home from "./pages/Home.jsx";

createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <Routes>
            <Route path="" element={<App/>}>
                <Route index={true} element={<Home/>}/>
            </Route>
        </Routes>
    </BrowserRouter>
)
