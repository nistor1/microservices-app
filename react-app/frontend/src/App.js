// App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext'; // Import the AuthProvider
import Login from './components/Login';
import ClientDevices from './components/ClientDevices';
import AdminDevices from './components/AdminDevices';
import AdminUsers from './components/AdminUsers';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from "./components/Nvbar";
import ClientDeviceDetails from "./components/ClientDeviceDetails";
import Messages from "./components/Messages";

const App = () => {
    return (

        <AuthProvider>
            <Router>
                <Navbar /> {/* Include Navbar aici pentru a apărea pe toate paginile */}
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route
                        path="/messages"
                        element={<ProtectedRoute roles={['ROLE_CLIENT', 'ROLE_ADMIN']} component={Messages} />}
                    />
                    <Route
                        path="/client/device/:deviceId"
                        element={<ProtectedRoute roles={['ROLE_CLIENT']} component={ClientDeviceDetails} />}
                    />
                    <Route
                        path="/client/devices"
                        element={<ProtectedRoute roles={['ROLE_CLIENT']} component={ClientDevices} />}
                    />
                    <Route
                        path="/admin/devices"
                        element={<ProtectedRoute roles={['ROLE_ADMIN']} component={AdminDevices} />}
                    />
                    <Route
                        path="/admin/users"
                        element={<ProtectedRoute roles={['ROLE_ADMIN']} component={AdminUsers} />}
                    />
                </Routes>
            </Router>
        </AuthProvider>
    );
};

export default App;
