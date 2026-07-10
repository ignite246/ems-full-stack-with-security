import React, { useEffect } from 'react';
import { useState } from 'react';
import { listEmployees } from '../services/EmployeeService';
import { useNavigate } from 'react-router-dom';

const ListEmployeeComponent = () => {

    const [employees, setEmployees] = useState([]);

    useEffect(() => {
        listEmployees().then((response) => {
            setEmployees(response.data);
        }).catch((error) => {
            console.log(error);
        });
    }, []);

    const navigator = useNavigate();

    const addNewEmployee = () => {
        navigator("/add-employee");
    }

    return (
        <div className='container my-2'>
            <div className="row">
                <div className="card col-md-8 offset-md-2">
                    <div className="card-header text-center bg-primary-subtle">
                        <h3>List of Employees</h3>
                    </div>
                    <div className="card-body">
                        <button className='btn btn-sm btn-primary mb-2' onClick={addNewEmployee}>Add Employee</button>
                        <table className='table table-striped table-bordered'>
                            <thead>
                                <tr>
                                    <th>Employee ID</th>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Email Id</th>
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