import React, { useEffect, useState } from 'react'
import { getHealth } from '../services/HealthService';

const HealthComponent = () => {

    const [healthData, setHealthData] = useState({
        "components": {
            "db": {
                "details": {
                    "database": "MySQL",
                    "validationQuery": "isValid()"
                },
                "status": "UP"
            },
            "diskSpace": {
                "details": {
                    "total": 475915087872,
                    "free": 374978981888,
                    "threshold": 10485760,
                    "path": "D:\\Projects2026\\javaguide\\ems\\.",
                    "exists": true
                },
                "status": "UP"
            },
            "livenessState": {
                "status": "UP"
            },
            "ping": {
                "status": "UP"
            },
            "readinessState": {
                "status": "UP"
            },
            "ssl": {
                "details": {
                    "expiringChains": [],
                    "invalidChains": [],
                    "validChains": []
                },
                "status": "UP"
            }
        },
        "groups": [
            "liveness",
            "readiness"
        ],
        "status": "UP"
    });

    useEffect(() => {
        getHealth().then((successResponse) => {
            console.log(successResponse.data);
            setHealthData(successResponse.data);
        }).catch((errorResponse) => {
            console.log(errorResponse);
        });
    }, []);


    return (
        <div className='row'>
            <div className="col-md-6 offset-md-3">
                <div className="card">
                    <div className="card-header">
                        <h3>Monitoring Dashboard</h3>
                    </div>
                    <div className="card-body">
                        <table className='table table-responsive table-bordered'>
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Value</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>

                                    <td>Oeverall Status</td>
                                    <td>{healthData.status}</td>

                                </tr>
                                <tr>
                                    <td>DB Name and Status</td>
                                    <td>{healthData.components.db.details.database} & {healthData.components.db.status}</td>
                                </tr>

                                <tr>
                                    <td>DiskSpace Status</td>
                                    <td>{healthData.components.diskSpace.status}</td>
                                </tr>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    )
}

export default HealthComponent