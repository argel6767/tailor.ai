import {Link} from "react-router-dom";

const Navbar = () => {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
            <Link to="/" >Landing Page</Link>
            <Link to="/auth">Sign In/Up</Link>
        </nav>
    )
}
export default Navbar;