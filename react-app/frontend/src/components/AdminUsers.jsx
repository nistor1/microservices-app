// components/AdminUsers.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // Importă useNavigate
import './AdminUsers.css';
import {useAuth} from "../context/AuthContext";

const API_URL = 'http://localhost:80/user-service/person';

//const API_URL = 'http://localhost:8080/person';

const AdminUsers = () => {

    const { user } = useAuth();
    const token = user.token;

    const [users, setUsers] = useState([]);
    const [formData, setFormData] = useState({
        personId: '',
        personName: '',
        address: '',
        age: '',
        role: '',
        email: '',
        password: ''
    });
    const [isEditing, setIsEditing] = useState(false);
    const [editingUserId, setEditingUserId] = useState(null);
    const navigate = useNavigate();

    // Fetch users on component mount
    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await axios.get(API_URL, {
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
                // Update user
                await axios.put(API_URL, formData, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
            } else {
                // Create new user
                await axios.post(API_URL, formData, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
            }
            fetchUsers(); // Refresh user list
            resetForm();
        } catch (error) {
            console.error('Error saving user:', error);
        }
    };

    // Reset form
    const resetForm = () => {
        setFormData({
            personId: '',
            personName: '',
            address: '',
            age: '',
            role: '',
            email: '',
            password: ''
        });
        setIsEditing(false);
        setEditingUserId(null);
    };

    // Handle edit user
    const handleEdit = (user) => {
        setFormData(user);
        setIsEditing(true);
        setEditingUserId(user.personId);
    };

    // Handle delete user
    const handleDelete = async (personId) => {
        try {
            await axios.delete(`${API_URL}/${personId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            fetchUsers(); // Refresh user list
        } catch (error) {
            console.error('Error deleting user:', error);
        }
    };

    // Navigare la AdminDevices
    const goToAdminDevices = () => {
        navigate('/admin/devices');
    };

    return (
        <div className="container"> {/* Adăugați o clasă pentru container */}
            <h1>Admin Users</h1>
            <form onSubmit={handleSubmit}>
                <input type="hidden" name="personId" value={formData.personId} />
                <div>
                    <label>Person Name:</label>
                    <input type="text" name="personName" value={formData.personName} onChange={handleInputChange} required />
                </div>
                <div>
                    <label>Address:</label>
                    <input type="text" name="address" value={formData.address} onChange={handleInputChange} required />
                </div>
                <div>
                    <label>Age:</label>
                    <input type="number" name="age" value={formData.age} onChange={handleInputChange} required />
                </div>
                <div>
                    <label>Role:</label>
                    <input type="text" name="role" value={formData.role} onChange={handleInputChange} required />
                </div>
                <div>
                    <label>Email:</label>
                    <input type="email" name="email" value={formData.email} onChange={handleInputChange} required />
                </div>
                <div>
                    <label>Password:</label>
                    <input type="password" name="password" value={formData.password} onChange={handleInputChange} required />
                </div>
                <button type="submit">{isEditing ? 'Update User' : 'Add User'}</button>
                {isEditing && <button type="button" onClick={resetForm}>Cancel</button>}
            </form>

            <h2>Users List</h2>
            <ul>
                {users.map((user) => (
                    <li key={user.personId}>
                        <div className="user-info">
                            <strong>{user.personName}</strong> - {user.role} - {user.email} - {user.address} - Age: {user.age}
                        </div>
                        <div className="user-actions">
                            <button className="edit-button" onClick={() => handleEdit(user)}>Edit</button>
                            <button className="delete-button" onClick={() => handleDelete(user.personId)}>Delete</button>
                        </div>
                    </li>
                ))}
            </ul>

            <div className="navigation-button">
                <button onClick={goToAdminDevices}>Go to Admin Devices</button> {/* Buton pentru navigare */}
            </div>
        </div>
    );
};

export default AdminUsers;
