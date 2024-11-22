import {Link} from "react-router-dom";

const Navbar = () => {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-base-300 gap-6 text-center p-2">
            <button className="btn bg-primary"> <Link to="/" >Home</Link></button>
            <button className="btn bg-primary"> <Link to="/auth">Login</Link></button>
            <button className="btn bg-primary"> <a href={"https://github.com/argel6767/tailor.ai"}>Source Code</a></button>
        </nav>
    )
}
export default Navbar;