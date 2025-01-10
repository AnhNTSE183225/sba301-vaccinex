import {Outlet} from "react-router";
import Header from "./common/Header.jsx";
import Footer from "./common/Footer.jsx";


function App() {

    return (
        <div className="app">
            <Header/>
            <Outlet/>
            <Footer/>
        </div>
    );
}

export default App
