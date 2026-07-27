import React, { useEffect, useState } from 'react'
import { deleteDepartmentById, getAllDepartments } from '../services/DepartmentService';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';

const ListDepartmentComponent = () => {

    const [currentDepartmentList, setDepartmentList] = useState([{
        "id": "0",
        "departmentName": "Test Department",
        "departmentDescription": "This is a test department description"
    }]);

    const fetchAllDepartments = () => {
        getAllDepartments().then((response) => {
            console.log(response.data);
            setDepartmentList(response.data);
        }).catch((error) => {
            console.log(error);
        });
    }

    useEffect(() => {
        fetchAllDepartments();
    }, []);

    const navigator = useNavigate();
    const addNewDepartment = () => {
        navigator("/add-department");
    }

    const removeDepartment = (id) => {
        console.log("id to be deleted....", id);
        deleteDepartmentById(id).then((response) => {
            console.log(response.data);
            Swal.fire({
                title: 'Success!',
                text: "Department deleted successfully",
                icon: 'success',
                confirmButtonText: 'OK'
            });
            fetchAllDepartments();
        }).catch((error) => {
            console.log(error.response.data);
            Swal.fire({
                title: 'Failure!',
                text: `${error.response.data.message}`,
                icon: 'error',
                confirmButtonText: 'OK'
            })
        })

    }

    const editDepartment = (departmentId) => {
        navigator(`/edit-department/${departmentId}`);
    }

    return (
        <div className='container my-2'>
            <div className="row">
                <div className="col-lg-8 offset-lg-2">
                    <div className="card border-3 border-danger container-fluid p-0">
                        <div className="card-header text-center text-danger bg-black">
                            <h3>List of Departments</h3>
                        </div>
                        <div className="card-body p-0">
                            <button className="btn btn-sm btn-outline-danger m-3" onClick={addNewDepartment}>Add Department</button>
                            <table className='table table-striped table-bordered table-hover table-responsive'>
                                <thead>
                                    <tr>
                                        <th>Department Id</th>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Employee Count</th>
                                        <th colSpan={2}>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                        currentDepartmentList.map(eachDepartment => (
                                            <tr key={eachDepartment.id}>
                                                <td>{eachDepartment.id}</td>
                                                <td>{eachDepartment.departmentName}</td>
                                                <td>{eachDepartment.departmentDescription}</td>
                                                <td>Not Available</td>
                                                <td>
                                                    <button
                                                        className="btn btn-sm btn-outline-warning"
                                                        onClick={() => editDepartment(eachDepartment.id)}>Edit</button>
                                                </td>
                                                <td>
                                                    <button
                                                        className="btn btn-sm btn-outline-danger"
                                                        onClick={() => removeDepartment(eachDepartment.id)}>Delete</button>
                                                </td>
                                            </tr>
                                        ))
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

export default ListDepartmentComponent