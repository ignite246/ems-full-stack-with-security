import React, { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { createEmployee, getEmployee, updateEmployee } from '../services/EmployeeService'
import { useNavigate, useParams } from 'react-router-dom'
import Swal from 'sweetalert2';

const CreateEmployeeComponent = () => {

    const { id } = useParams();

    //for holding individual form field data
    const [currentFirstName, setUpdatedFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");


    //for holding each field validation message
    const [currentErrorMessages, setUpdatedErrorMessages] = useState({
        errorFirstNameMsg: '',
        errorLastNameMsg: '',
        errorEmailMsg: ''
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
            errorMessagesCopy.errorEmailMsg = "";
        } else {
            errorMessagesCopy.errorEmailMsg = "Email id is required";
            isValid = false;
        }

        setUpdatedErrorMessages(errorMessagesCopy);
        return isValid;
    }

    //to redirect the control
    const navigator = useNavigate();

    {/* Create or Update employee */ }
    const handleFormSubmit = (e) => {
        e.preventDefault();
        if (validateFormData()) {
            const employee = { firstName: currentFirstName, lastName, email };
            console.log("employee data from form:", employee);

            if (id) {
                updateEmployee(id, employee)
                    .then((response) => {
                        console.log(response.data);
                        navigator("/employees");
                    })
                    .catch((error) => {
                        console.log(error);
                    });
            } else {
                createEmployee(employee).then((apiResponse) => {
                    console.log("Create Employee API Response:", apiResponse);
                    navigator("/employees");
                }).catch((error) => {
                    console.log("Something went wrong while creating an employee:", error);
                });
            }
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

    const pageTitle = () => {
        if (id) {
            return <h3 className='bg-warning text-light p-1 m-0'>Update Employee</h3>
        }
        else {
            return <h3 className='bg-primary text-light p-1 m-0'>Create Employee</h3>
        }
    }

    useEffect(() => {
        console.log("Employee id:", id);
        if (id) {
            getEmployee(id)
                .then((response) => {
                    console.log(response);
                    setUpdatedFirstName(response.data.firstName);
                    setLastName(response.data.lastName);
                    setEmail(response.data.email);
                }).catch((error) => {
                    console.log(error);
                });
        }
    }, [id]);

    return (
        <div className='container-fluid'>
            <div className="row my-2">
                <div className="card col-md-4 offset-md-4 p-0 border-3 border-primary">
                    <div className="card-header text-center p-0">
                        {pageTitle()}
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
                                    className={`form-control ${currentErrorMessages.errorEmailMsg ? 'is-invalid' : ''}`}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                                {currentErrorMessages.errorEmailMsg && <div className='invalid-feedback'>{currentErrorMessages.errorEmailMsg}</div>}
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