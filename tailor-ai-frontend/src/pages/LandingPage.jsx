import {Link} from "react-router-dom";

/**
 * Landing Page for Tailor.ai
 */
const LandingPage = () => {

    return (
        <div className="h-full pt-4 px-4">
            <div className="hero bg-primary h-2/3 rounded-lg">
                <div className="hero-content text-center">
                    <div className="max-w-md">
                        <h1 className="text-5xl font-bold py-1.5">Welcome to Tailor.ai</h1>
                        <p className="py-6 text-xl">
                            AI-powered solutions to tailor your resume for any profession.
                        </p>
                        <Link className="btn btn-secondary" to="/auth">Get Started</Link>
                    </div>
                </div>
            </div>
            <span className="grid grid-cols-3 gap-7 w-full text-center p-9 text-background px-3">
                <div className="bg-secondary rounded-xl hover:scale-105 transition-transform duration-300">
                    <h3 className="text-xl font-bold pt-3">Personalized Resumes</h3>
                    <p className="py-6 px-1">Leverage AI to craft resumes tailored to your desired career path.</p>
                </div>
                <div className="bg-secondary rounded-xl hover:scale-105 transition-transform duration-300">
                    <h3 className="text-xl font-bold pt-3">Industry Insights</h3>
                    <p className="py-6 px-1">Optimize your resume with the latest trends and requirements.</p>
                </div>
                <div className="bg-secondary rounded-xl hover:scale-105 transition-transform duration-300">
                    <h3 className="text-xl font-bold pt-3">Effortless Process</h3>
                    <p className="py-6 px-1">Upload, and have your resume reviewed and fixed in just a few clicks.</p>
                </div>
            </span>
        </div>
    )
}

export default LandingPage;