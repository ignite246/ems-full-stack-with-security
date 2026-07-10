import React, { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { createEmployee } from '../services/EmployeeService'
import { useNavigate } from 'react-router-dom'
import Swal from 'sweetalert2';

const CreateEmployeeComponent = () => {

    //for holding individual form field data
    const [currentFirstName, setUpdatedFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");


    //form validation
    const [currentErrorMessages, setUpdatedErrorMessages] = useState({
        errorFirstNameMsg: '',
        errorLastNameMsg: '',
        erroEmailMsg: ''
    });

    const validateFormData = () => {
        let isValid = true;
        const errorMessagesCopy = { ...currentErrorMessages };

        if (currentFirstName.trim()) {
            errorMessagesCopy.errorFirstNameMsg = "";
        } else {
            errorMessagesCopy.errorFirstNameMsg = "First name is required";
            isValid = false;
        }

        if (lastName.trim()) {
            errorMessagesCopy.errorLastNameMsg = "";
        } else {
            errorMessagesCopy.errorLastNameMsg = "Last name is required";
            isValid = false;
        }

        if (email.trim()) {
            errorMessagesCopy.erroEmailMsg = "";
        } else {
            errorMessagesCopy.erroEmailMsg = "Email id is required";
            isValid = false;
        }

        setUpdatedErrorMessages(errorMessagesCopy);
        return isValid;
    }




    //to redirect the control
    const navigator = useNavigate();
    const handleFormSubmit = (e) => {
        e.preventDefault();
        if (validateFormData()) {
            const employee = { firstName: currentFirstName, lastName, email };
            console.log("employee to be created:", employee);
            createEmployee(employee).then((apiResponse) => {
                console.log("Create Employee API Response:", apiResponse);
                navigator("/employees");
            }).catch((error) => {
                console.log("Something went wrong while creating an employee:", error);
            });
        }
        else {
            Swal.fire({
                title: 'Error!',
                text: 'Form is incomplete.',
                icon: 'error',
                confirmButtonText: 'Form=>'
            });
        }
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
                                    value={currentFirstName}
                                    className={`form-control ${currentErrorMessages.errorFirstNameMsg ? 'is-invalid' : ''}`}
                                    onChange={(e) => setUpdatedFirstName(e.target.value)}
                                />
                                {currentErrorMessages.errorFirstNameMsg && <div className='invalid-feedback'>{currentErrorMessages.errorFirstNameMsg}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className='form-label'>Last Name:</label>
                                <input
                                    type='text'
                                    placeholder='Enter employee last name'
                                    name='lastName'
                                    value={lastName}
                                    className={`form-control ${currentErrorMessages.errorLastNameMsg ? 'is-invalid' : ''}`}
                                    onChange={(e) => setLastName(e.target.value)}
                                />
                                {currentErrorMessages.errorLastNameMsg && <div className='invalid-feedback'>{currentErrorMessages.errorLastNameMsg}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className='form-label'>Email Id:</label>
                                <input
                                    type='email'
                                    placeholder='Enter a valid email id'
                                    name='email'
                                    value={email}
                                    className={`form-control ${currentErrorMessages.erroEmailMsg ? 'is-invalid' : ''}`}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                                {currentErrorMessages.erroEmailMsg && <div className='invalid-feedback'>{currentErrorMessages.erroEmailMsg}</div>}
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