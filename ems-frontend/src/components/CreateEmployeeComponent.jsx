import React, { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { createEmployee } from '../services/EmployeeService'
import { useNavigate } from 'react-router-dom'

const CreateEmployeeComponent = () => {

    //for holding individual form field data
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");

    //to redirect the control
    const navigator = useNavigate();

    const handleFormSubmit = (e) => {
        e.preventDefault();
        const employee = { firstName, lastName, email };
        console.log("employee to be created:", employee);

        createEmployee(employee).then((apiResponse) => {
            console.log("Create Employee API Response:", apiResponse);
            navigator("/employees");
        }).catch((error) => {
            console.log("Something went wrong while creating an employee:", error);
        });

    }

    return (
        <div className='container-fluid'>
            <div className="row my-2">
                <div className="card col-md-4 offset-md-4">
                    <div className="card-header bg-primary-subtle">
                        <h3 className='text-center'>Add Employee</h3>
                    </div>
                    <div className="card-body">
                        <form>
                            <div className="form-group mb-2">
                                <label className='form-label'>First Name:</label>
                                <input
                                    type='text'
                                    placeholder='Enter employee first name'
                                    name='firstName'
                                    value={firstName}
                                    className='form-control'
                                    onChange={(e) => setFirstName(e.target.value)}
                                />
                            </div>

                            <div className="form-group mb-2">
                                <label className='form-label'>Last Name:</label>
                                <input
                                    type='text'
                                    placeholder='Enter employee last name'
                                    name='lastName'
                                    value={lastName}
                                    className='form-control'
                                    onChange={(e) => setLastName(e.target.value)}
                                />
                            </div>

                            <div className="form-group mb-2">
                                <label className='form-label'>Email Id:</label>
                                <input
                                    type='email'
                                    placeholder='Enter a valid email id'
                                    name='email'
                                    value={email}
                                    className='form-control'
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                            </div>

                            <button
                                className="btn btn-success"
                                onClick={handleFormSubmit}>Submit</button>
                        </form>
                    </div>
                    <div className="card-footer">
                        <h2 className='bg-dark text-white'>Footer of the form</h2>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default CreateEmployeeComponent