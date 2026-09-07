import React, { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { createEmployee, getEmployee, updateEmployee } from '../../services/EmployeeService'
import { useNavigate, useParams } from 'react-router-dom'
import Swal from 'sweetalert2';
import { getAllDepartments } from '../../services/DepartmentService';
import { getAllOffices } from '../../services/OfficeService';

const EmployeeComponent = () => {

    const { id } = useParams();

    //for holding individual form field data
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [departmentId, setDepartmentId] = useState("");
    const [departmentList, setDepartmentList] = useState([]);
    const [officeId, setOfficeId] = useState("");
    const [officeList, setOfficeList] = useState([]);
    const [orgName, setOrgName] = useState("");
    const [dateOfJoining, setDateOfJoining] = useState("");
    const [dateOfLeaving, setDateOfLeaving] = useState("");


    //to load all the departments
    useEffect(() => {
        getAllDepartments().then((response) => {
            console.log("---departments---", response.data);
            setDepartmentList(response.data);
        }).catch((error) => {
            console.log(error);
        });
    }, []);


    //to load all the offices
    useEffect(() => {
        getAllOffices().then((response) => {
            console.log("---offices---", response.data);
            setOfficeList(response.data);
        }).catch((error) => {
            console.log(error);
        });
    }, []);

    //for holding each field validation message
    const [currentErrorMessages, setUpdatedErrorMessages] = useState({
        errorFirstNameMsg: '',
        errorLastNameMsg: '',
        errorEmailMsg: '',
        errorDepartmentMsg: '',
        errorOfficeMsg: "",
        errorOrgNameMsg: "",
        errorDateOfJoiningMsg: "",
        errorDateOfLeavingMsg: ""
    });

    const validateFormData = () => {
        let isValid = true;
        const errorMessagesCopy = { ...currentErrorMessages };

        if (firstName.trim()) {
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

        if (departmentId) {
            errorMessagesCopy.errorDepartmentMsg = "";
        } else {
            errorMessagesCopy.errorDepartmentMsg = "Department is required";
            isValid = false;
        }

        if (officeId) {
            errorMessagesCopy.errorOfficeMsg = "";
        } else {
            errorMessagesCopy.errorOfficeMsg = "Office is required";
            isValid = false;
        }

        if (orgName.trim()) {
            errorMessagesCopy.errorOrgNameMsg = "";
        } else {
            errorMessagesCopy.errorOrgNameMsg = "Last org name is required";
            isValid = false;
        }

        if (dateOfJoining) {
            errorMessagesCopy.errorDateOfJoiningMsg = "";
        } else {
            errorMessagesCopy.errorDateOfJoiningMsg = "DOJ is required";
            isValid = false;
        }

        if (dateOfLeaving) {
            errorMessagesCopy.errorDateOfLeavingMsg = "";
        } else {
            errorMessagesCopy.errorDateOfLeavingMsg = "DOL is required";
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
            const employee = { firstName, lastName, email, departmentId, officeId, experiences: [{ orgName, dateOfJoining, dateOfLeaving }] };
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
                    Swal.fire({
                        title: 'Success!',
                        text: `${apiResponse.status + apiResponse.statusText}`,
                        icon: 'success',
                        confirmButtonText: 'OK'
                    })
                    navigator("/employees");
                }).catch((error) => {
                    console.log("Something went wrong while creating an employee:", error.response);
                    Swal.fire({
                        title: 'Failure!',
                        text: `${error.response.data.message}`,
                        icon: 'error',
                        confirmButtonText: 'OK'
                    })
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
        if (id) {
            console.log("Employee id to be updated:", id);
            getEmployee(id)
                .then((response) => {
                    console.log("---getEmployee---", response);
                    setFirstName(response.data.firstName);
                    setLastName(response.data.lastName);
                    setEmail(response.data.email);
                    setDepartmentId(response.data.departmentId);
                }).catch((error) => {
                    console.log(error);
                });
        }
    }, [id]);

    return (
        <div className='container-fluid'>
            <div className="row my-2">
                <div className="card col-md-8 offset-md-2 p-0 border-3 border-primary">
                    <div className="card-header text-center p-0">
                        {pageTitle()}
                    </div>
                    <div className="card-body">
                        <form>
                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group mb-2">
                                        <label className='form-label' htmlFor="firstNameId">First Name:</label>
                                        <input
                                            id="firstNameId"
                                            type='text'
                                            placeholder='Enter employee first name'
                                            name='firstName'
                                            value={firstName}
                                            className={`form-control ${currentErrorMessages.errorFirstNameMsg ? 'is-invalid' : ''}`}
                                            onChange={(e) => setFirstName(e.target.value)}
                                        />
                                        {currentErrorMessages.errorFirstNameMsg && <div className='invalid-feedback'>{currentErrorMessages.errorFirstNameMsg}</div>}
                                    </div>
                                </div>

                                <div className="col-md-6">
                                    <div className="form-group mb-2">
                                        <label className='form-label' htmlFor="lastNameId">Last Name:</label>
                                        <input
                                            id="lastNameId"
                                            type='text'
                                            placeholder='Enter employee last name'
                                            name='lastName'
                                            value={lastName}
                                            className={`form-control ${currentErrorMessages.errorLastNameMsg ? 'is-invalid' : ''}`}
                                            onChange={(e) => setLastName(e.target.value)}
                                        />
                                        {currentErrorMessages.errorLastNameMsg && <div className='invalid-feedback'>{currentErrorMessages.errorLastNameMsg}</div>}
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-md-6">
                                    <div className="form-group mb-2">
                                        <label className='form-label' htmlFor="emailId">Email Id:</label>
                                        <input
                                            id="emailId"
                                            type='text'
                                            placeholder='Enter a valid email id'
                                            name='email'
                                            value={email}
                                            className={`form-control ${currentErrorMessages.errorEmailMsg ? 'is-invalid' : ''}`}
                                            onChange={(e) => setEmail(e.target.value)}
                                        />
                                        {currentErrorMessages.errorEmailMsg &&
                                            <div className='invalid-feedback'>{currentErrorMessages.errorEmailMsg}</div>}
                                    </div>
                                </div>

                                <div className="col-md-6">
                                    <div className="form-group mb-2">
                                        <label className='form-label' htmlFor='departmentId'>Select Department:</label>
                                        <select
                                            id="departmentId"
                                            name="departmentId"
                                            className={`form-select ${currentErrorMessages.errorDepartmentMsg ? 'is-invalid' : ''}`}
                                            value={departmentId}
                                            onChange={(e) => setDepartmentId(e.target.value)}>
                                            <option value="Select department">Open this select menu:</option>
                                            {
                                                departmentList.map(eachDepartment =>
                                                    <option key={eachDepartment.id} value={eachDepartment.id}>
                                                        {eachDepartment.departmentName}
                                                    </option>
                                                )
                                            }
                                        </select>
                                        {
                                            currentErrorMessages.errorDepartmentMsg &&
                                            <div className='invalid-feedback'>{currentErrorMessages.errorDepartmentMsg}</div>
                                        }
                                    </div>
                                </div>

                            </div>

                            <div className="row">
                                <div className="col-md-12">
                                    <div className="form-group mb-2">
                                        <label className='form-label' htmlFor='officeId'>Select Office:</label>
                                        <select
                                            id="officeId"
                                            name="officeId"
                                            className={`form-select ${currentErrorMessages.errorOfficeMsg ? 'is-invalid' : ''}`}
                                            value={officeId}
                                            onChange={(e) => setOfficeId(e.target.value)}>
                                            <option value="Select Office">Open this select menu:</option>
                                            {
                                                officeList.map(eachOffice =>
                                                    <option key={eachOffice.officeId} value={eachOffice.officeId}>
                                                        {eachOffice.name}
                                                    </option>
                                                )
                                            }
                                        </select>
                                        {
                                            currentErrorMessages.errorOfficeMsg &&
                                            <div className='invalid-feedback'>{currentErrorMessages.errorOfficeMsg}</div>
                                        }
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-md-4">
                                    <div className="form-group mb-2">
                                        <label className='form-label' htmlFor="orgNameId">Organization Name:</label>
                                        <input
                                            id="orgNameId"
                                            type='text'
                                            placeholder='Enter your last organization name'
                                            name='orgName'
                                            value={orgName}
                                            className={`form-control ${currentErrorMessages.errorOrgNameMsg ? 'is-invalid' : ''}`}
                                            onChange={(e) => setOrgName(e.target.value)}
                                        />

                                        {
                                            currentErrorMessages.errorOrgNameMsg &&
                                            <div className='invalid-feedback'>{currentErrorMessages.errorOrgNameMsg}</div>
                                        }

                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group mb-2">
                                        <label className='form-label' htmlFor="dateOfJoiningId">Joined On:</label>
                                        <input
                                            id="dateOfJoiningId"
                                            type='date'
                                            placeholder='Select date of joining'
                                            name='dateOfJoining'
                                            value={dateOfJoining}
                                            className={`form-control ${currentErrorMessages.errorDateOfJoiningMsg ? 'is-invalid' : ''}`}
                                            onChange={(e) => setDateOfJoining(e.target.value)}
                                        />

                                        {
                                            currentErrorMessages.errorDateOfJoiningMsg &&
                                            <div className='invalid-feedback'>{currentErrorMessages.errorDateOfJoiningMsg}</div>
                                        }

                                    </div>
                                </div>

                                <div className="col-md-4">
                                    <div className="form-group mb-2">
                                        <label className='form-label' htmlFor="dateOfLeavingId">Left on:</label>
                                        <input
                                            id="dateOfLeavingId"
                                            type='date'
                                            placeholder='Select date of leaving'
                                            name='dateOfLeaving'
                                            value={dateOfLeaving}
                                            className={`form-control ${currentErrorMessages.errorDateOfLeavingMsg ? 'is-invalid' : ''}`}
                                            onChange={(e) => setDateOfLeaving(e.target.value)}
                                        />

                                        {
                                            currentErrorMessages.errorDateOfLeavingMsg &&
                                            <div className='invalid-feedback'>{currentErrorMessages.errorDateOfLeavingMsg}</div>
                                        }

                                    </div>
                                </div>

                            </div>

                            <button
                                className="btn btn-primary"
                                onClick={handleFormSubmit}>Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default EmployeeComponent