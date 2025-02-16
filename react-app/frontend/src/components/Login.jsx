// components/Login.js
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie'; // For handling cookies
import {jwtDecode} from 'jwt-decode'; // To decode JWT
import { useAuth } from '../context/AuthContext';
import './Login.css';

//const API_URL = 'http://localhost:8080/auth/login';
const API_URL = 'http://localhost:80/user-service/auth/login';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { login } = useAuth();

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            console.log('Form submission started');

            // Send login request to the backend
            const response = await axios.post(API_URL, { email, password });

            console.log('Response received:', response);

            if (response.status === 200 && response.data.jwtToken) {
                console.log('Login successful, processing token...');

                // Extract the JWT token from the response body
                const token = response.data.jwtToken;

                // Optionally, save the token in a cookie
                Cookies.set('jwt', token, { secure: true, httpOnly: false });
                console.log('JWT token saved in cookies');

                // Decode the JWT token
                const decodedToken = jwtDecode(token);
                const username = decodedToken.sub; // or decodedToken.username based on your payload
                const roles = decodedToken.role || []; // or decodedToken.authorities

                const id = response.data.id;

                console.log('Username:', username);
                console.log('Role:', roles);

                // Update the auth context with user information
                console.log('Updating auth context with username:', username);
                login(username, roles, token, id);

                // Navigate based on the user role
                console.log('User roles:', roles);
                if (roles.includes('ROLE_ADMIN')) {
                    console.log('User is an admin, navigating to /admin/devices');
                    navigate('/admin/devices');
                } else if (roles.includes('ROLE_CLIENT')) {
                    console.log('User is a client, navigating to /client/devices');
                    navigate(`/client/devices`);
                } else {
                    console.log('User has no recognized role, staying on current page');
                }

            } else {
                console.error('Token missing in response');
                setError('Login failed. Please try again.');
            }
        } catch (err) {
            console.error('Login error:', err);
            setError('Invalid email or password. Please try again.');
        }
    };

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <button type="submit">Login</button>
            </form>
        </div>
    );
};

export default Login;
