import React, { useState } from 'react'
import { loginUser, saveLoggedInUserInSessionStorage, storeTokenInLocalStorage } from '../../services/AuthService';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';

const LoginComponent = () => {

    const navigator = useNavigate();

    const [usernameOrEmail, setUsernameOrEmail] = useState("");

    const [password, setPassword] = useState("");

    const handleUserLoginForm = async (e) => {
        e.preventDefault();

        const loginObj = { usernameOrEmail, password };
        console.log("loginObj", loginObj);
        await loginUser(loginObj)
            .then((successResponse) => {
                console.log("login API response:", successResponse.data);


                // Step1: generating Basic auth token & setting into LocalStorage of the browser
                //const token = "Basic " + window.btoa(usernameOrEmail + ":" + password);

                const token = "Bearer " + successResponse.data.accessToken;
                const role = successResponse.data.role;

                storeTokenInLocalStorage(token);
                saveLoggedInUserInSessionStorage(usernameOrEmail, role);

                navigator("/employees");

                window.location.reload(false);

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
        <div className='container'>
            <div className="row">
                <div className="col-lg-6 offset-lg-3">
                    <div className="card">
                        <div className="card-header bg-success text-center text-white">
                            <h3>User Login Form</h3>
                        </div>
                        <div className="card-body">
                            <form>
                                <div className="row mb-2">
                                    <label className='col-md-3 control-label'>Username or Email:</label>
                                    <div className="col-md-9">
                                        <input
                                            type='text'
                                            name='name'
                                            className='form-control'
                                            placeholder='Enter username or email id'
                                            value={usernameOrEmail}
                                            onChange={(e) => setUsernameOrEmail(e.target.value)}
                                        />
                                    </div>
                                </div>

                                <div className="row mb-2">
                                    <label className='col-md-3 control-label'>Password:</label>
                                    <div className="col-md-9">
                                        <input
                                            type='password'
                                            name='name'
                                            className='form-control'
                                            placeholder='Enter password'
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className="form-group mb-3">
                                    <button className='btn btn-sm btn-success' onClick={(e) => handleUserLoginForm(e)}>Submit</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default LoginComponent