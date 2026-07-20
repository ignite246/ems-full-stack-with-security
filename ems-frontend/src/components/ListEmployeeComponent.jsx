import React, { useEffect } from 'react';
import { useState } from 'react';
import { listEmployees, deleteEmployee } from '../services/EmployeeService';
import { useNavigate } from 'react-router-dom';

const ListEmployeeComponent = () => {

    const [employees, setEmployees] = useState([{
        "id": "0",
        "firstName": "FirstName",
        "lastName": "LastName",
        "email": "Email Id"
    }
    ]);

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

    const navigator = useNavigate();
    const addNewEmployee = () => {
        navigator("/add-employee");
    }

    const updateEmployee = (id) => {
        console.log("employee to be updated...", id, typeof (id));
        navigator(`/update-employee/${id}`);
    }

    const removeEmployee = (id) => {
        console.log("employee to be deleted with id:", id);
        deleteEmployee(id).then((response) => {
            console.log(response.data);
            getAllEmployees();
        }).catch((error) => {
            console.log(error.response.error);
        });
    }

    return (
        <div className='container-fluid my-2'>
            <div className="row">
                <div className="card col-lg-8 offset-lg-2 p-0 border-3 border-info-subtle">
                    <div className="card-header text-center bg-primary-subtle">
                        <h3>List of Employees</h3>
                    </div>
                    <div className="card-body p-0">
                        <button className='btn btn-sm btn-primary m-2' onClick={addNewEmployee}>Add Employee</button>
                        <table className='table table-striped table-bordered table-responsive'>
                            <thead>
                                <tr>
                                    <th>Employee ID</th>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Email Id</th>
                                    <th colSpan={2}>Actions</th>
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
                                            <td>
                                                <button className="btn btn-sm btn-info" onClick={() => updateEmployee(employee.id)}>Update</button>
                                            </td>

                                            <td>
                                                <button className="btn btn-sm btn-danger" onClick={() => removeEmployee(employee.id)}>Delete</button>
                                            </td>
                                        </tr>
                                    )
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ListEmployeeComponent