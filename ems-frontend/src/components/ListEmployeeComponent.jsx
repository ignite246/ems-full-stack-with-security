import React from 'react'

const ListEmployeeComponent = () => {


    const dummyData = [
        {
            "id": 1,
            "firstName": "Rahul",
            "lastName": "Kumar",
            "email": "rkumar11@gmail.com"
        },

        {
            "id": 2,
            "firstName": "Mohit",
            "lastName": "Sharma",
            "email": "mmsharma@gmail.com"
        },

        {
            "id": 3,
            "firstName": "Rahul",
            "lastName": "Kumar",
            "email": "rkumar11@gmail.com"
        }
    ]

    return (
        <div className='container'>
            <h2>List of Employees</h2>
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
                        dummyData.map(employee =>
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
    )
}

export default ListEmployeeComponent