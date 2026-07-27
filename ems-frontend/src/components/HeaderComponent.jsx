import React from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { isUserLoggedIn, logoutUser } from '../services/AuthService'

const HeaderComponent = () => {

    const navigator = useNavigate();
    const isAuth = isUserLoggedIn();

    const handleLogout = () => {
        logoutUser();
        console.log("user logged out successfully !");
        navigator("/login");
    }

    return (
        <div>
            <header>
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark">

                    <NavLink className="navbar-brand mx-1" to="/">
                        Employee Management System
                    </NavLink>

                    <button
                        className="navbar-toggler"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent"
                        aria-expanded="false"
                        aria-label="Toggle navigation"
                    >
                        <span className="navbar-toggler-icon"></span>
                    </button>

                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav ms-auto">
                            {
                                isAuth && <li className="nav-item">
                                    <NavLink to="/employees" className="nav-link">
                                        Employees
                                    </NavLink>
                                </li>
                            }

                            {
                                isAuth && <li className="nav-item">
                                    <NavLink to="/departments" className="nav-link">
                                        Departments
                                    </NavLink>
                                </li>
                            }

                            {
                                !isAuth && <li className="nav-item">
                                    <NavLink to="/register" className="nav-link">
                                        Register
                                    </NavLink>
                                </li>
                            }

                            {
                                !isAuth && <li className="nav-item">
                                    <NavLink to="/login" className="nav-link">
                                        Login
                                    </NavLink>
                                </li>
                            }

                            {
                                isAuth && <li className="nav-item">
                                    <button
                                        className="nav-link btn btn-link text-white"
                                        onClick={handleLogout}
                                    >
                                        Logout
                                    </button>
                                </li>
                            }
                        </ul>
                    </div>

                </nav>
            </header>
        </div>
    )
}

export default HeaderComponent