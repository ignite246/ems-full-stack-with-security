import React, { useEffect, useState } from 'react';
import { getHealth } from '../services/HealthService';

const HealthDashboardComponent = () => {

    const [healthData, setHealthData] = useState({
        components: {
            db: {
                details: {
                    database: '',
                    validationQuery: ''
                },
                status: 'UNKNOWN'
            },
            diskSpace: {
                details: {
                    total: 0,
                    free: 0,
                    threshold: 0,
                    path: '',
                    exists: false
                },
                status: 'UNKNOWN'
            },
            livenessState: {
                status: 'UNKNOWN'
            },
            ping: {
                status: 'UNKNOWN'
            },
            readinessState: {
                status: 'UNKNOWN'
            },
            ssl: {
                status: 'UNKNOWN'
            }
        },
        status: 'UNKNOWN'
    });

    const [lastUpdated, setLastUpdated] = useState(null);
    const [loading, setLoading] = useState(false);

    const fetchHealth = () => {

        setLoading(true);

        getHealth()
            .then((successResponse) => {

                console.log(successResponse.data);

                setHealthData(successResponse.data);
                setLastUpdated(new Date());

            })
            .catch((errorResponse) => {

                console.log(errorResponse);

            })
            .finally(() => {

                setLoading(false);

            });
    };

    useEffect(() => {
        fetchHealth();
    }, []);

    const getStatusBadge = (status) => {

        if (status === 'UP') {
            return (
                <span className="badge rounded-pill text-bg-success px-3 py-2">
                    ● UP
                </span>
            );
        }

        if (status === 'DOWN') {
            return (
                <span className="badge rounded-pill text-bg-danger px-3 py-2">
                    ● DOWN
                </span>
            );
        }

        return (
            <span className="badge rounded-pill text-bg-secondary px-3 py-2">
                ● UNKNOWN
            </span>
        );
    };

    const formatBytes = (bytes) => {

        if (!bytes) {
            return '0 Bytes';
        }

        const units = ['Bytes', 'KB', 'MB', 'GB', 'TB'];

        const index = Math.floor(
            Math.log(bytes) / Math.log(1024)
        );

        return `${(bytes / Math.pow(1024, index)).toFixed(2)} ${units[index]}`;
    };

    const calculateDiskUsage = () => {

        const total = healthData.components.diskSpace.details.total;
        const free = healthData.components.diskSpace.details.free;

        if (!total) {
            return 0;
        }

        return ((free / total) * 100).toFixed(1);
    };

    const diskFreePercentage = calculateDiskUsage();

    return (

        <div className="container py-4 px-4 bg-success-subtle">

            {/* Dashboard Header */}
            <div className="d-flex justify-content-between align-items-center mb-4">

                <div>
                    <h2 className="fw-bold mb-1">
                        Monitoring Dashboard
                    </h2>

                    <p className="text-muted mb-0">
                        Application health and infrastructure status
                    </p>
                </div>

                <button
                    className="btn btn-outline-primary"
                    onClick={fetchHealth}
                    disabled={loading}
                >
                    {loading ? (
                        <>
                            <span
                                className="spinner-border spinner-border-sm me-2"
                                role="status"
                            />
                            Refreshing...
                        </>
                    ) : (
                        <>
                            ↻ Refresh
                        </>
                    )}
                </button>

            </div>


            {/* Last Updated */}
            {lastUpdated && (
                <div className="text-end text-muted small mb-3">
                    Last checked: {lastUpdated.toLocaleTimeString()}
                </div>
            )}


            {/* Main Status Cards */}
            <div className="row g-4">


                {/* Overall Status */}
                <div className="col-md-6 col-xl-3">

                    <div className="card shadow-sm h-100 border-0">

                        <div className="card-body">

                            <div className="d-flex justify-content-between align-items-start">

                                <div>
                                    <h6 className="text-muted mb-2">
                                        Application Status
                                    </h6>

                                    <h3 className="fw-bold mb-1">
                                        {healthData.status}
                                    </h3>

                                    <small className="text-muted">
                                        Overall system health
                                    </small>
                                </div>

                                <div className="fs-2">
                                    🖥️
                                </div>

                            </div>

                            <div className="mt-3">
                                {getStatusBadge(healthData.status)}
                            </div>

                        </div>

                    </div>

                </div>


                {/* Database */}
                <div className="col-md-6 col-xl-3">

                    <div className="card shadow-sm h-100 border-0">

                        <div className="card-body">

                            <div className="d-flex justify-content-between">

                                <div>
                                    <h6 className="text-muted mb-2">
                                        Database
                                    </h6>

                                    <h3 className="fw-bold mb-1">
                                        {healthData.components.db.details.database}
                                    </h3>

                                    <small className="text-muted">
                                        Database connectivity
                                    </small>
                                </div>

                                <div className="fs-2">
                                    🗄️
                                </div>

                            </div>

                            <div className="mt-3">
                                {getStatusBadge(
                                    healthData.components.db.status
                                )}
                            </div>

                        </div>

                    </div>

                </div>


                {/* Disk Space */}
                <div className="col-md-6 col-xl-3">

                    <div className="card shadow-sm h-100 border-0">

                        <div className="card-body">

                            <div className="d-flex justify-content-between">

                                <div>
                                    <h6 className="text-muted mb-2">
                                        Disk Space
                                    </h6>

                                    <h3 className="fw-bold mb-1">
                                        {diskFreePercentage}%
                                    </h3>

                                    <small className="text-muted">
                                        Available
                                    </small>
                                </div>

                                <div className="fs-2">
                                    💾
                                </div>

                            </div>

                            <div className="progress mt-3" style={{ height: '8px' }}>

                                <div
                                    className="progress-bar bg-success"
                                    role="progressbar"
                                    style={{
                                        width: `${diskFreePercentage}%`
                                    }}
                                />

                            </div>

                            <div className="d-flex justify-content-between mt-2">

                                <small className="text-muted">
                                    Free: {
                                        formatBytes(
                                            healthData.components.diskSpace.details.free
                                        )
                                    }
                                </small>

                                <small className="text-muted">
                                    Total: {
                                        formatBytes(
                                            healthData.components.diskSpace.details.total
                                        )
                                    }
                                </small>

                            </div>

                        </div>

                    </div>

                </div>


                {/* Application State */}
                <div className="col-md-6 col-xl-3">

                    <div className="card shadow-sm h-100 border-0">

                        <div className="card-body">

                            <div className="d-flex justify-content-between">

                                <div>
                                    <h6 className="text-muted mb-3">
                                        Application State
                                    </h6>

                                    <div className="mb-2">
                                        <span className="text-muted me-3">
                                            Liveness
                                        </span>

                                        {getStatusBadge(
                                            healthData.components.livenessState.status
                                        )}
                                    </div>

                                    <div>
                                        <span className="text-muted me-3">
                                            Readiness
                                        </span>

                                        {getStatusBadge(
                                            healthData.components.readinessState.status
                                        )}
                                    </div>

                                </div>

                                <div className="fs-2">
                                    ❤️
                                </div>

                            </div>

                        </div>

                    </div>

                </div>


            </div>


            {/* Component Health */}
            <div className="card shadow-sm border-0 mt-4">

                <div className="card-header bg-white py-3">

                    <h5 className="mb-0 fw-bold">
                        Component Health
                    </h5>

                    <small className="text-muted">
                        Status of individual application components
                    </small>

                </div>


                <div className="card-body">

                    <div className="row g-3">


                        {/* Database */}
                        <div className="col-md-6 col-lg-3">

                            <div className="border rounded p-3">

                                <div className="d-flex justify-content-between">

                                    <span className="fw-semibold">
                                        🗄️ Database
                                    </span>

                                    {getStatusBadge(
                                        healthData.components.db.status
                                    )}

                                </div>

                                <small className="text-muted">
                                    {healthData.components.db.details.database}
                                </small>

                            </div>

                        </div>


                        {/* Disk */}
                        <div className="col-md-6 col-lg-3">

                            <div className="border rounded p-3">

                                <div className="d-flex justify-content-between">

                                    <span className="fw-semibold">
                                        💾 Disk Space
                                    </span>

                                    {getStatusBadge(
                                        healthData.components.diskSpace.status
                                    )}

                                </div>

                                <small className="text-muted">
                                    Storage availability
                                </small>

                            </div>

                        </div>


                        {/* Ping */}
                        <div className="col-md-6 col-lg-3">

                            <div className="border rounded p-3">

                                <div className="d-flex justify-content-between">

                                    <span className="fw-semibold">
                                        📡 Ping
                                    </span>

                                    {getStatusBadge(
                                        healthData.components.ping.status
                                    )}

                                </div>

                                <small className="text-muted">
                                    Application connectivity
                                </small>

                            </div>

                        </div>


                        {/* SSL */}
                        <div className="col-md-6 col-lg-3">

                            <div className="border rounded p-3">

                                <div className="d-flex justify-content-between">

                                    <span className="fw-semibold">
                                        🔐 SSL
                                    </span>

                                    {getStatusBadge(
                                        healthData.components.ssl.status
                                    )}

                                </div>

                                <small className="text-muted">
                                    SSL certificate status
                                </small>

                            </div>

                        </div>


                    </div>

                </div>

            </div>


            {/* Disk Details */}
            <div className="card shadow-sm border-0 mt-4">

                <div className="card-header bg-white py-3">

                    <h5 className="mb-0 fw-bold">
                        Disk Space Details
                    </h5>

                </div>

                <div className="card-body">

                    <div className="row">

                        <div className="col-md-4">
                            <small className="text-muted">
                                Total Space
                            </small>

                            <div className="fw-semibold">
                                {formatBytes(
                                    healthData.components.diskSpace.details.total
                                )}
                            </div>
                        </div>


                        <div className="col-md-4">
                            <small className="text-muted">
                                Free Space
                            </small>

                            <div className="fw-semibold">
                                {formatBytes(
                                    healthData.components.diskSpace.details.free
                                )}
                            </div>
                        </div>


                        <div className="col-md-4">
                            <small className="text-muted">
                                Minimum Threshold
                            </small>

                            <div className="fw-semibold">
                                {formatBytes(
                                    healthData.components.diskSpace.details.threshold
                                )}
                            </div>
                        </div>

                    </div>

                </div>

            </div>

        </div>
    );
};

export default HealthDashboardComponent;