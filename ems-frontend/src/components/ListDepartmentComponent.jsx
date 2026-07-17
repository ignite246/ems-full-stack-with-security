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
            console.log(error);
        })

    }

    return (
        <div className='container my-2'>
            <div className="row">
                <div className="col-lg-6 offset-lg-3">
                    <div className="card border-3 border-success container-fluid p-0">
                        <div className="card-header text-center text-bg-success">
                            <h3>List of Departments</h3>
                        </div>
                        <div className="card-body p-0">
                            <button className="btn btn-sm btn-success m-2" onClick={addNewDepartment}>Add Department</button>
                            <table className='table table-striped table-bordered table-hover table-responsive'>
                                <thead>
                                    <tr>
                                        <th>Department Id</th>
                                        <th>Name</th>
                                        <th>Description</th>
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
                                                <td>
                                                    <button
                                                        className="btn btn-sm btn-outline-warning">Edit</button>
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