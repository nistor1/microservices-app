import React from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import './Nvbar.css';

const Navbar = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const goToMessages = () => {
        navigate('/messages');
    };

    return (
        <nav className="navbar">
            {user && (
                <>
                    <button className="logout-button" onClick={logout}>
                        Logout
                    </button>
                    <button className="messages-button" onClick={goToMessages}>Go to messages</button>
                </>
            )}
        </nav>
    );
};

export default Navbar;