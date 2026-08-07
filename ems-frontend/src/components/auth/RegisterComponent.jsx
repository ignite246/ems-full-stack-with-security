import React, { useState } from 'react'
import { registerUser } from '../../services/AuthService';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';

const RegisterComponent = () => {

    const navigator = useNavigate();

    const [name, setName] = useState("test-name");
    const [username, setUsername] = useState("test-username");
    const [email, setEmail] = useState("test-email");
    const [password, setPassword] = useState("test-password");

    const handleRegistrationForm = (e) => {
        e.preventDefault();
        const register = { name, username, email, password };
        console.log("registration form data", register);

        registerUser(register)
            .then((success) => {
                console.log(success.data);
                Swal.fire({
                    title: "Success!",
                    text: success.data.message,
                    icon: "success",
                    confirmButtonText: "OK"
                });
                navigator("/login")
            })
            .catch((failure) => {
                console.log(failure);
                if (failure.response) {
                    Swal.fire({
                        title: "Failure!",
                        text: failure.response.data.message,
                        icon: "error",
                        confirmButtonText: "OK"
                    });

                } else {
                    Swal.fire({
                        title: "Network Error!",
                        text: failure.message,
                        icon: "error",
                        confirmButtonText: "OK"
                    });

                }
            });
    }

    return (
        <div className='container my-2'>
            <div className='row'>
                <div className="col-lg-6 offset-lg-3">
                    <div className="card">
                        <div className="card-header text-center bg-dark text-white">
                            <h3>User Registration Form</h3>
                        </div>
                        <div className="card-body bg-info-subtle">
                            <form>
                                <div className="row mb-2">
                                    <label className='col-md-3 control-label'>Name:</label>
                                    <div className="col-md-9">
                                        <input
                                            type='text'
                                            name='name'
                                            className='form-control'
                                            placeholder='Enter name'
                                            value={name}
                                            onChange={(e) => setName(e.target.value)}
                                        />
                                    </div>
                                </div>

                                <div className="row mb-2">
                                    <label className='col-md-3 control-label'>Username:</label>
                                    <div className="col-md-9">
                                        <input
                                            type='text'
                                            name='name'
                                            className='form-control'
                                            placeholder='Enter username'
                                            value={username}
                                            onChange={(e) => setUsername(e.target.value)}
                                        />
                                    </div>
                                </div>

                                <div className="row mb-2">
                                    <label className='col-md-3 control-label'>Email:</label>
                                    <div className="col-md-9">
                                        <input
                                            type='text'
                                            name='name'
                                            className='form-control'
                                            placeholder='Enter email id'
                                            value={email}
                                            onChange={(e) => setEmail(e.target.value)}
                                        />
                                    </div>
                                </div>

                                <div className="row mb-2">
                                    <label className='col-md-3 control-label'>Password:</label>
                                    <div className="col-md-9">
                                        <input
                                            type='text'
                                            name='name'
                                            className='form-control'
                                            placeholder='Enter password'
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className="form-group mb-3">
                                    <button className='btn btn-sm btn-primary' onClick={(e) => handleRegistrationForm(e)}>Submit</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default RegisterComponent