import {Link} from "react-router-dom";

const LandingPage = () => {

    return (
        <body className="h-screen pt-4 px-4">
            <div className="hero bg-primary h-2/3 rounded-lg">
                <div className="hero-content text-center">
                    <div className="max-w-md">
                        <h1 className="text-5xl font-bold py-1.5">Welcome to Tailor.ai</h1>
                        <p className="py-6 text-xl">
                            AI-powered solutions to tailor your resume for any profession.
                        </p>
                        <button className="btn btn-secondary"><Link to="/auth">Get Started</Link></button>
                    </div>
                </div>
            </div>
            <span className="grid grid-cols-3 gap-7 w-full text-center p-9 text-background px-3">
                <div className="bg-secondary rounded-xl">
                    <h3 className="text-xl font-bold pt-1">Personalized Resumes</h3>
                    <p className="py-6">Leverage AI to craft resumes tailored to your desired career path.</p>
                </div>
                <div className="bg-secondary rounded-xl">
                    <h3 className="text-xl font-bold pt-1">Industry Insights</h3>
                    <p className="py-6">Optimize your resume with the latest trends and requirements.</p>
                </div>
                <div className="bg-secondary rounded-xl">
                    <h3 className="text-xl font-bold pt-1">Effortless Process</h3>
                    <p className="py-6">Upload, customize, and download your resume in just a few clicks.r</p>
                </div>
            </span>
        </body>
    )
}

export default LandingPage;