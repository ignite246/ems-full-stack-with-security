import React, { useEffect, useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { getLoggedInUser, isLoggedInUserAdmin, isUserLoggedIn, logoutUser } from '../services/AuthService'

const HeaderComponent = () => {

    const navigator = useNavigate();
    const isAuth = isUserLoggedIn();
    const isAdmin = isLoggedInUserAdmin();
    const [username, setUsername] = useState("test-username");

    const handleLogout = () => {
        logoutUser();
        console.log("user logged out successfully !");
        navigator("/login");
    }

    useEffect(() => {
        setUsername(getLoggedInUser());
    }, []);

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
                                isAuth &&
                                <li className="nav-item">
                                    <NavLink to="/employees" className="nav-link">
                                        Employees
                                    </NavLink>
                                </li>
                            }

                            {
                                (isAuth && isAdmin) &&
                                <li className="nav-item">
                                    <NavLink to="/departments" className="nav-link">
                                        Departments
                                    </NavLink>
                                </li>
                            }

                            {
                                isAuth &&
                                <li className="nav-item">
                                    <NavLink to="/health" className="nav-link">
                                        Health Dashboard
                                    </NavLink>
                                </li>
                            }

                            {
                                !isAuth &&
                                <li className="nav-item">
                                    <NavLink to="/register" className="nav-link">
                                        Register
                                    </NavLink>
                                </li>
                            }

                            {
                                !isAuth &&
                                <li className="nav-item">
                                    <NavLink to="/login" className="nav-link">
                                        Login
                                    </NavLink>
                                </li>
                            }

                            {
                                isAuth &&
                                <li className="nav-item">
                                    <button
                                        className="nav-link"
                                        onClick={handleLogout}
                                    >
                                        Logout
                                    </button>
                                </li>
                            }
                            {
                                isAuth &&
                                <li className="nav-item">
                                    <span className="nav-link text-bg-success" title='Username'>
                                        ({username})
                                    </span>
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