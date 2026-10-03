import React, { useEffect, useState } from 'react'
import { getAllOffices } from '../../services/OfficeService';

const ListOfficeComponent = () => {

    const [offices, setOffices] = useState([]);

    const fetchAllOffices = () => {
        getAllOffices().then((response) => {
            setOffices(response.data);
        }).catch((error) => {
            console.log(error);
        });
    }

    useEffect(() => {
        fetchAllOffices();
    }, []);

    return (
        <div className='container'>
            <div className="row">

                <div className="col-md-10 offset-md-1">

                    <div className="card">

                        <div className="card-header bg-info-subtle">
                            <h3>List of Offices</h3>
                        </div>

                        <div className="card-body">
                            <div className="table-responsive">
                                <table className="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Id</th>
                                            <th>Office Name</th>
                                            <th>Office Email</th>
                                            <th>Seating Capacity</th>
                                            <th>Point of Contact</th>
                                            <th>Address</th>
                                            <th colSpan={3}>Actions</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        {
                                            offices.map(eachOffice => (
                                                <tr key={eachOffice.officeId}>
                                                    <td>{eachOffice.officeId}</td>
                                                    <td>{eachOffice.name}</td>
                                                    <td>{eachOffice.officeEmail}</td>
                                                    <td>{eachOffice.seatingCapacity}</td>
                                                    <td>{eachOffice.pointOfContact}</td>
                                                    <td>
                                                        {eachOffice.address.city},{" "}
                                                        {eachOffice.address.country}
                                                    </td>
                                                    <td><button className='btn btn-sm btn-outline-success'>View</button></td>
                                                    <td><button className='btn btn-sm btn-outline-primary'>Edit</button></td>
                                                    <td><button className='btn btn-sm btn-outline-danger'>Delete</button></td>
                                                </tr>
                                            ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    )
}

export default ListOfficeComponent