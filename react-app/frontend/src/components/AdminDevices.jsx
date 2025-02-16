import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './AdminDevices.css';
import { useAuth } from '../context/AuthContext'; // Adjust according to your path

const DEVICE_API_URL = 'http://localhost:80/device-service/device';
const USER_API_URL = 'http://localhost:80/user-service/person';

//const DEVICE_API_URL = 'http://localhost:8081/device';
//const USER_API_URL = 'http://localhost:8080/person';




const AdminDevices = () => {

    const { user } = useAuth();
    const token = user.token;


    const [devices, setDevices] = useState([]);
    const [users, setUsers] = useState([]);
    const [formData, setFormData] = useState({
        deviceId: '',
        personId: '',
        description: '',
        address: '',
        maximumHourlyEnergyConsumption: ''
    });
    const [isEditing, setIsEditing] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        fetchDevices();
        fetchUsers();
    }, []);

    const fetchDevices = async () => {
        try {
            const response = await axios.get(DEVICE_API_URL, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setDevices(response.data);
        } catch (error) {
            console.error('Error fetching devices:', error);
        }
    };

    const fetchUsers = async () => {
        try {
            const response = await axios.get(USER_API_URL, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            setUsers(response.data);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditing) {
                await axios.put(DEVICE_API_URL, formData, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
            } else {
                await axios.post(DEVICE_API_URL, formData, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
            }
            fetchDevices();
            resetForm();
        } catch (error) {
            console.error('Error saving device:', error);
        }
    };

    const resetForm = () => {
        setFormData({
            deviceId: '',
            personId: '',
            description: '',
            address: '',
            maximumHourlyEnergyConsumption: ''
        });
        setIsEditing(false);
    };

    const handleEdit = (device) => {
        setFormData(device);
        setIsEditing(true);
    };

    const handleDelete = async (deviceId) => {
        try {
            await axios.delete(`${DEVICE_API_URL}/${deviceId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            fetchDevices();
        } catch (error) {
            console.error('Error deleting device:', error);
        }
    };

    const goToAdminUsers = () => {
        navigate('/admin/users');
    };

    return (
        <div>
            <h1>Admin Devices</h1>
            <form onSubmit={handleSubmit}>
                <input type="hidden" name="deviceId" value={formData.deviceId} />

                <div>
                    <label>User:</label>
                    <select
                        name="personId"
                        value={formData.personId}
                        onChange={handleInputChange}
                        required
                    >
                        <option value="">Select User</option>
                        {users.map(user => (
                            <option key={user.personId} value={user.personId}>
                                {user.personName} - {user.email} - {user.phone}
                            </option>
                        ))}
                    </select>
                </div>

                <div>
                    <label>Description:</label>
                    <input
                        type="text"
                        name="description"
                        value={formData.description}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <div>
                    <label>Address:</label>
                    <input
                        type="text"
                        name="address"
                        value={formData.address}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <div>
                    <label>Maximum Hourly Energy Consumption (kWh):</label>
                    <input
                        type="number"
                        name="maximumHourlyEnergyConsumption"
                        value={formData.maximumHourlyEnergyConsumption}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <button type="submit">
                    {isEditing ? 'Update Device' : 'Add Device'}
                </button>
                {isEditing && (
                    <button type="button" onClick={resetForm}>
                        Cancel
                    </button>
                )}
            </form>

            <h2>Devices List</h2>
            <ul>
                {devices.map(device => (
                    <li key={device.deviceId}>
                        <strong>{device.description}</strong> -
                        {users.find(user => user.personId === device.personId)?.personName || 'Unknown User'}
                        - Address: {device.address} - Max Consumption: {device.maximumHourlyEnergyConsumption} kWh
                        <button onClick={() => handleEdit(device)}>Edit</button>
                        <button onClick={() => handleDelete(device.deviceId)}>Delete</button>
                    </li>
                ))}
            </ul>

            {/* Buton pentru navigarea la AdminUsers */}
            <button onClick={goToAdminUsers} style={{ marginTop: '20px', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
                Go to Admin Users
            </button>
        </div>
    );
};

export default AdminDevices;
