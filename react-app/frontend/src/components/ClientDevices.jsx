import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './ClientDevices.css';

const API_URL = 'http://localhost:80/device-service/client';

const ClientDevices = () => {
    const { user } = useAuth();
    const token = user?.token;
    const id = user?.id || user?.personId;
    const [devices, setDevices] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        if (!user?.logged) {
            navigate('/login');
            return;
        }

        fetchDevices(id);
    }, [user, navigate, id]);

    const fetchDevices = async (id) => {
        if (!id) {
            console.error('Invalid ID:', id);
            return;
        }

        try {
            console.log('Fetching devices for ID:', id);
            const response = await axios.get(`${API_URL}/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setDevices(response.data);
        } catch (error) {
            console.error('Error fetching devices:', error);
        }
    };

    const viewDeviceDetails = (deviceId) => {
        console.log('Viewing device details:', deviceId);
        navigate(`/client/device/${deviceId}`);
    };

    return (
        <div>
            <h1>Welcome, {user?.email}</h1>
            <h2>Devices:</h2>
            <table>
                <thead>
                <tr>
                    <th>Description</th>
                    <th>Address</th>
                    <th>Max Hourly Energy Consumption</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {devices && devices.length > 0 ? (
                    devices.map((device) => (
                        <tr key={device.deviceId}>
                            <td>{device.description}</td>
                            <td>{device.address}</td>
                            <td>{device.maximumHourlyEnergyConsumption}</td>
                            <td>
                                <button onClick={() => viewDeviceDetails(device.deviceId)}>
                                    View Details
                                </button>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="4">No devices found.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default ClientDevices;
