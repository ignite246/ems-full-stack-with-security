import React, { useEffect, useState } from 'react'
import { createDepartment, getDepartmentById, updateDepartment } from '../services/DepartmentService';
import { useNavigate, useParams } from 'react-router-dom';
import Swal from 'sweetalert2';

const DepartmentComponent = () => {

    const navigator = useNavigate();
    const { id } = useParams();

    //text state for form data
    const [currentDepartmentName, setDepartmentName] = useState("");
    const [currentDepartmentDescription, setDepartmentDescription] = useState("");

    //text state for error message
    const [nameErrorMsg, setNameErrorMsg] = useState("Department name cannot be blank!");
    const [descriptionErrorMsg, setDescriptionErrorMsg] = useState("Department description cannot be blank!");

    //boolean state for error is present or not
    const [isNameError, setIsNameError] = useState(false);
    const [isDescriptionError, setIsDescriptionError] = useState(false);


    const handleFormSubmit = (e) => {
        e.preventDefault();
        if (currentDepartmentName == null || currentDepartmentName.trim() == "") {
            setIsNameError(true);
        }

        if (currentDepartmentDescription == null || currentDepartmentDescription.trim() == "") {
            setIsDescriptionError(true);
        }

        console.log(isNameError, isDescriptionError)

        if (!isNameError && !isDescriptionError) {
            const department = { departmentName: currentDepartmentName, departmentDescription: currentDepartmentDescription };

            if (id) {
                updateDepartment(id, department).then((response) => {
                    console.log(response);
                    navigator("/departments");
                }).catch((error) => {
                    console.log(error)
                })
            } else {
                createDepartment(department)
                    .then((response) => {
                        console.log(response.data);
                        navigator("/departments");
                    }).catch((error) => {
                        console.log(error);
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
            alert("Fill the form correctly...");
        }
    }

    const operationName = () => {
        if (id) {
            return <h3>Update Department Form</h3>
        }
        else {
            return <h3>Add Department Form</h3>
        }
    }

    useEffect(() => {
        if (id) {
            getDepartmentById(id).then((response) => {
                console.log(response);
                setDepartmentName(response.data.departmentName);
                setDepartmentDescription(response.data.departmentDescription);
            }).catch((error) => {

            });
        }
    }, [id]);

    return (
        <div className='container my-2'>
            <div className="row">
                <div className="col-lg-8 offset-lg-2">
                    <div className="card border-3 border-danger-subtle">
                        <div className="card-header text-center bg-danger-subtle">
                            {operationName()}
                        </div>
                        <div className="card-body">
                            <form>
                                <div className="form-group mb-3">
                                    <label className='form-label'>Name:</label>
                                    <input
                                        type='text'
                                        placeholder='Enter department name'
                                        name='departmentName'
                                        className='form-control'
                                        value={currentDepartmentName}
                                        onChange={(e) => setDepartmentName(e.target.value)}
                                    />
                                    {
                                        isNameError &&
                                        <span className='text-danger fst-italic'>{nameErrorMsg}</span>
                                    }
                                </div>

                                <div className="form-group mb-3">
                                    <label className='form-label'>Description:</label>
                                    <input
                                        type='text'
                                        placeholder='Enter department description'
                                        name='departmentDescription'
                                        value={currentDepartmentDescription}
                                        className='form-control'
                                        onChange={(e) => setDepartmentDescription(e.target.value)}
                                    />
                                    {
                                        isDescriptionError &&
                                        <span className='text-danger fst-italic'>{descriptionErrorMsg}</span>
                                    }
                                </div>
                                <button
                                    className="btn btn-success"
                                    onClick={handleFormSubmit}>Submit</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default DepartmentComponent