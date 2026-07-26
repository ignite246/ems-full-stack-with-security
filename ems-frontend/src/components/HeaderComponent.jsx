import React from 'react'
import { NavLink } from 'react-router-dom'

const HeaderComponent = () => {
    return (
        <div>
            <header>
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark">

                    <NavLink className="navbar-brand" to="/">
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
                            <li className="nav-item">
                                <NavLink to="/employees" className="nav-link">
                                    Employees
                                </NavLink>
                            </li>

                            <li className="nav-item">
                                <NavLink to="/departments" className="nav-link">
                                    Departments
                                </NavLink>
                            </li>

                            <li className="nav-item">
                                <NavLink to="/register" className="nav-link">
                                    Register
                                </NavLink>
                            </li>

                            <li className="nav-item">
                                <NavLink to="/login" className="nav-link">
                                    Login
                                </NavLink>
                            </li>
                        </ul>
                    </div>

                </nav>
            </header>
        </div>
    )
}

export default HeaderComponent