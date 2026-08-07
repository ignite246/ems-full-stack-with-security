import { Link } from "react-router-dom";

const PageNotFoundComponent = () => {
    return (
        <div className="container text-center mt-5">
            <h1 className="display-3">404</h1>
            <h3>Page Not Found</h3>

            <p>
                The page you are looking for doesn't exist.
            </p>

            <Link to="/" className="btn btn-primary">
                Go to Home
            </Link>
        </div>
    );
};

export default PageNotFoundComponent;