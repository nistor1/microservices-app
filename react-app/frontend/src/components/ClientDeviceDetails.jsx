import React, { useRef, useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import { useNavigate, useParams } from 'react-router-dom';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import './ClientDevices.css';

// Registering the Chart.js components
ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

//const API_URL = 'http://localhost:8082/measurement';
const API_URL = 'http://localhost:80/measurement-service/measurement';

const ClientDeviceDetails = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const { deviceId } = useParams();
    const myRef = useRef();

    const [webSocketMessage, setWebSocketMessage] = useState(null);
    const [wsConnected, setWsConnected] = useState(false);
    const [historicalData, setHistoricalData] = useState([]);
    const [selectedDate, setSelectedDate] = useState('');

    const token = user?.token;

    // Redirect unauthorized users
    useEffect(() => {
        if (!user?.logged) {
            navigate('/login');
            return;
        }
        if (!user?.role?.includes('ROLE_CLIENT')) {
            alert('You do not have permission to access this page.');
            navigate('/');
            return;
        }
    }, [user, navigate]);

    // WebSocket connection setup
    useEffect(() => {
        if (!deviceId) return;

        const socket = new WebSocket(`ws://localhost:80/measurement-service/measurement/websocket/${deviceId}`);

        socket.onopen = () => setWsConnected(true);
        socket.onmessage = (event) => setWebSocketMessage(event.data);
        socket.onerror = (error) => console.error('WebSocket error:', error);
        socket.onclose = () => setWsConnected(false);

        return () => socket.close();
    }, [deviceId]);

    // Fetch historical data
    const fetchHistoricalData = async (date) => {
        if (!token || !deviceId || !date) return;

        try {
            const response = await axios.get(`${API_URL}/history/${deviceId}`, {
                params: { date },
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setHistoricalData(response.data);
        } catch (error) {
            if (error.response?.status === 403) {
                alert('You do not have access to this data.');
            } else {
                console.error('Error fetching historical data:', error);
            }
        }
    };

    useEffect(() => {
        if (selectedDate) fetchHistoricalData(selectedDate);
    }, [selectedDate]);

    const handleDateChange = (event) => {
        setSelectedDate(event.target.value);
    };

    const renderDataList = () => {
        if (!historicalData.length) return <p>No data available for the selected date.</p>;

        return (
            <ul style={{ listStyleType: 'none', padding: 0 }}>
                {historicalData.map((item) => (
                    <li key={item.measurementId} style={{ marginBottom: '10px' }}>
                        <strong>Time:</strong> {new Date(item.timestamp).toLocaleTimeString()} <br />
                        <strong>Consumption:</strong> {item.hourlyConsumption} kWh
                    </li>
                ))}
            </ul>
        );
    };

    const renderChart = () => {
        if (!historicalData.length) return <p>No data available for the selected date.</p>;

        const data = {
            labels: historicalData.map((item) => new Date(item.timestamp).toLocaleTimeString()),
            datasets: [
                {
                    label: 'Energy Consumption (kWh)',
                    data: historicalData.map((item) => item.hourlyConsumption),
                    borderColor: 'rgba(75,192,192,1)',
                    backgroundColor: 'rgba(75,192,192,0.2)',
                    tension: 0.4,
                },
            ],
        };

        const options = {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: `Energy Consumption for ${selectedDate}`,
                },
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Time',
                    },
                },
                y: {
                    title: {
                        display: true,
                        text: 'Consumption (kWh)',
                    },
                    beginAtZero: true,
                },
            },
        };

        return <Line data={data} options={options} />;
    };

    return (
        <div ref={myRef}>
            <h1>Device for User: {user?.email || 'Loading...'}</h1>
            <div>
                <label htmlFor="date-picker">Select Day:</label>
                <input
                    type="date"
                    id="date-picker"
                    value={selectedDate || ''}
                    onChange={handleDateChange}
                />
            </div>
            {webSocketMessage && (
                <div
                    style={{
                        marginTop: '20px',
                        backgroundColor: '#f8d7da',
                        color: '#721c24',
                        padding: '10px',
                        borderRadius: '5px',
                    }}
                >
                    {webSocketMessage}
                </div>
            )}
            {!wsConnected && (
                <div
                    style={{
                        marginTop: '20px',
                        backgroundColor: '#f8d7da',
                        color: '#721c24',
                        padding: '10px',
                        borderRadius: '5px',
                    }}
                >
                    WebSocket connection is not established.
                </div>
            )}
            <div style={{ marginTop: '20px' }}>
                <h2>Energy Consumption for {selectedDate || 'Selected Date'}</h2>
                {renderDataList()}
                <div style={{ marginTop: '30px' }}>{renderChart()}</div>
            </div>
        </div>
    );
};

export default ClientDeviceDetails;
