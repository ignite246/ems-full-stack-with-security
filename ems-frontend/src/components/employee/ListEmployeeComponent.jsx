import React, { useEffect } from 'react';
import { useState } from 'react';
import { listEmployees, deleteEmployee } from '../../services/EmployeeService';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import { isLoggedInUserAdmin } from '../../services/AuthService';

const ListEmployeeComponent = () => {

    const navigator = useNavigate();
    const [employees, setEmployees] = useState([]);

    //1. Fetch all employees
    const getAllEmployees = () => {
        listEmployees().then((response) => {
            setEmployees(response.data);
        }).catch((error) => {
            console.log(error);
        });
    }

    useEffect(() => {
        getAllEmployees();
    }, []);


    //2. Add a new employee
    const addNewEmployee = () => {
        navigator("/add-employee");
    }


    //3. Update an employee
    const updateEmployee = (id) => {
        console.log("employee to be updated...", id);

        navigator(`/update-employee/${id}`);
    }

    //4. Remove an employee
    const removeEmployee = (id) => {
        console.log("employee to be deleted with id:", id);

        deleteEmployee(id)
            .then((response) => {
                console.log(response.data);
                getAllEmployees();

            })
            .catch((error) => {
                if (error.response) {
                    Swal.fire({
                        icon: "error",
                        title: `${error.response.data.status} ${error.response.data.error}`,
                        text: error.response.data.message
                    });

                } else if (error.request) {
                    Swal.fire({
                        icon: "error",
                        title: "Service Temporarily Unavailable",
                        text: "We're unable to connect to the server right now. Please try again in a few moments. If the problem persists, contact your administrator."
                    });
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Unexpected Error",
                        text: error.message
                    });

                }

            });
    };

    //6. View full details of an employee
    const viewEmployee = () => {
        Swal.fire({
            icon: "info",
            title: "WIP",
            text: "Feature coming soon..."
        });
    }

    // Check if loggedInUser is Admin or Not
    const isAdmin = isLoggedInUserAdmin();

    return (
        <div className='container-fluid my-2'>
            <div className="row">
                <div className="col-lg-8 offset-lg-2 p-0">
                    <div className="card border-3 border-info-subtle">
                        <div className="card-header text-center bg-primary-subtle">
                            <h3>List of Employees : {employees.length}</h3>
                        </div>
                        <div className="card-body p-0">
                            {
                                isAdmin && <button className='btn btn-sm btn-primary m-2' onClick={addNewEmployee}>Add Employee</button>
                            }

                            <table className='table table-striped table-bordered table-responsive table-hover'>
                                <thead>
                                    <tr>
                                        <th>Employee ID</th>
                                        <th>First Name</th>
                                        <th>Last Name</th>
                                        <th>Email Id</th>
                                        <th colSpan={3} className='text-center'>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                        employees.map(employee =>
                                            <tr key={employee.id}>
                                                <td>{employee.id}</td>
                                                <td>{employee.firstName}</td>
                                                <td>{employee.lastName}</td>
                                                <td>{employee.email}</td>
                                                <td className='text-center'><button className="btn btn-sm btn-info" onClick={() => viewEmployee(employee.id)}>View</button></td>
                                                {
                                                    isAdmin &&
                                                    <td className='text-center'><button className="btn btn-sm btn-warning" onClick={() => updateEmployee(employee.id)}>Update</button></td>
                                                }

                                                {
                                                    isAdmin &&
                                                    <td className='text-center'><button className="btn btn-sm btn-danger" onClick={() => removeEmployee(employee.id)}>Delete</button></td>
                                                }
                                            </tr>
                                        )
                                    }
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    )
}

export default ListEmployeeComponent