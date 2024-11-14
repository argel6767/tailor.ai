import {Link} from "react-router-dom";

const Navbar = () => {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark space-x-10">
            <button> <Link to="/" >Landing Page</Link></button>
            <button> <Link to="/auth">Sign In/Up</Link></button>

            <Link to="/verify">Verify Page</Link>
        </nav>
    )
}
export default Navbar;