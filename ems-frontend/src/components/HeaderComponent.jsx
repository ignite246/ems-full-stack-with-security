import React from 'react'
import { NavLink } from 'react-router-dom'

const HeaderComponent = () => {
    return (
        <div>
            <header>
                <nav className='navbar navbar-dark bg-dark navbar-expand-lg'>
                    <a className='navbar-brand' href="https://ignite246.github.io/">Emp. Mgmt. System</a>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav">
                            <li className="nav-item">
                                <NavLink to='/employees' className='nav-link'>Employees</NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink to='/departments' className='nav-link'>Departments</NavLink>
                            </li>
                        </ul>
                    </div>
                </nav>
            </header>
        </div>
    )
}

export default HeaderComponent