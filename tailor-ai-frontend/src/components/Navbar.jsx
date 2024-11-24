import {Link} from "react-router-dom";

const Navbar = () => {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-base-300 gap-6 text-center p-2">
            <Link className="btn btn-primary" to="/" >Home</Link>
            <Link className="btn btn-primary" to="/auth">Login</Link>
            <button className="btn bg-primary"> <a href={"https://github.com/argel6767/tailor.ai"}>Source Code</a></button>
        </nav>
    )
}
export default Navbar;