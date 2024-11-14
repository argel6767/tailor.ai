import {Link} from "react-router-dom";

const Navbar = () => {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark space-x-10">
            <Link to="/" >Landing Page</Link>
            <Link to="/auth">Sign In/Up</Link>
            <Link to="/verify">Verify Page</Link>
        </nav>
    )
}
export default Navbar;