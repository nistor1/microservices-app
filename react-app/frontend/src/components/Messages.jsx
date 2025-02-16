import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import './Messages.css';

const API_URL = 'http://localhost:80/user-service/person';
const WS_URL = 'ws://localhost:80/chat-service/ws';

const Messages = () => {
    const { user } = useAuth();
    const token = user.token;
    const id = user.id;
    const [users, setUsers] = useState([]);
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [isMultiSelect, setIsMultiSelect] = useState(false);
    const [messages, setMessages] = useState({});
    const [newMessage, setNewMessage] = useState('');
    const [isSending, setIsSending] = useState(false);
    const messagesEndRef = useRef(null);
    const [socket, setSocket] = useState(null);
    const [isTyping, setIsTyping] = useState(false);

    const fetchUsers = async () => {
        try {
            const response = await axios.get(API_URL, {
                headers: { 'Authorization': `Bearer ${token}` },
            });
            setUsers(response.data);
        } catch (error) {
            console.error('[fetchUsers] Error fetching users:', error);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, [token]);

    useEffect(() => {
        if (selectedUsers.length > 0) {
            initializeWebSocket(selectedUsers);
        }
    }, [selectedUsers]);

    const getConversationKey = (receiverIds) => {
        return receiverIds.sort().join(',');
    };

    const initializeWebSocket = (receiverIds) => {
        const conversationKey = getConversationKey(receiverIds);

        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.close();
        }

        const wsUrl = `${WS_URL}?uuidSender=${user.id}&uuidReceiver=${conversationKey}`;
        const ws = new WebSocket(wsUrl);

        ws.onopen = () => {
            console.log('[initializeWebSocket] WebSocket connected');
            ws.send(
                JSON.stringify({
                    senderId: user.id,
                    receiverIds: selectedUsers,
                    content: 'User is available (read)',
                    timestamp: new Date().toISOString(),
                    seen: true,
                })
            );
        };

        ws.onmessage = (event) => {
            const message = JSON.parse(event.data);
            console.log('[WebSocket onmessage] Message received:', message);


            if (message.senderId === conversationKey) {
                if (message.content === 'User is typing...') {
                    setIsTyping(true);
                } else {
                    setIsTyping(false);
                }

                setMessages((prevMessages) => {
                    const updatedMessages = { ...prevMessages };
                    if (!updatedMessages[conversationKey]) {
                        updatedMessages[conversationKey] = [];
                    }
                    if (
                        message.content !== 'User is typing...' &&
                        !updatedMessages[conversationKey].some(
                            (msg) =>
                                msg.timestamp === message.timestamp &&
                                msg.content === message.content
                        )
                    ) {
                        updatedMessages[conversationKey].push(message);
                    }
                    return updatedMessages;
                });
                scrollToBottom();
            }
        };

        ws.onerror = (error) => {
            console.error('[initializeWebSocket] WebSocket error:', error);
        };

        ws.onclose = () => {
            console.log('[initializeWebSocket] WebSocket closed');
        };

        setSocket(ws);
    };

    const handleSelectUser = (userId) => {
        if (isMultiSelect) {
            setSelectedUsers((prevSelectedUsers) => {
                if (prevSelectedUsers.includes(userId)) {
                    return prevSelectedUsers.filter((id) => id !== userId);
                } else {
                    return [...prevSelectedUsers, userId];
                }
            });
        } else {
            setSelectedUsers([userId]);
        }
    };

    const toggleMultiSelect = () => {
        setIsMultiSelect((prevState) => !prevState);
        if (isMultiSelect) {
            setSelectedUsers([]);
        }
    };

    const handleSendMessage = async () => {
        if (newMessage.trim() && !isSending) {
            setIsSending(true);

            const message = {
                senderId: user.id,
                receiverIds: selectedUsers,
                content: newMessage,
                timestamp: new Date().toISOString(),
            };

            const conversationKey = getConversationKey(selectedUsers);

            if (socket && socket.readyState === WebSocket.OPEN) {
                socket.send(JSON.stringify(message));

                setMessages((prevMessages) => {
                    const updatedMessages = { ...prevMessages };
                    if (!updatedMessages[conversationKey]) {
                        updatedMessages[conversationKey] = [];
                    }
                    if (!updatedMessages[conversationKey].some((msg) => msg.timestamp === message.timestamp)) {
                        updatedMessages[conversationKey].push(message);
                    }
                    setIsTyping(false);
                    return updatedMessages;
                });

                setNewMessage('');
                scrollToBottom();
            }
            setIsSending(false);
        }
    };

    const handleTypingStatus = (isTyping) => {
        const typingMessage = isTyping ? 'User is typing...' : '--------------';

        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.send(
                JSON.stringify({
                    senderId: user.id,
                    receiverIds: selectedUsers,
                    content: typingMessage,
                    timestamp: new Date().toISOString(),
                })
            );
        }
    };

    const handleChangeMessage = (e) => {
        const newValue = e.target.value;
        setNewMessage(newValue);

        if (newValue.trim() !== '') {
            handleTypingStatus(true);
        } else {
            handleTypingStatus(false);
        }
    };

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    return (
        <div className="messages-container">
            <div className="main-content">
                <div className="user-list">
                    <h2>Users</h2>
                    <ul>
                        {users.map((user) => (
                            <li
                                key={user.personId}
                                onClick={() => handleSelectUser(user.personId)}
                                className={selectedUsers.includes(user.personId) ? 'selected' : ''}
                            >
                                {user.personName}
                            </li>
                        ))}
                    </ul>
                </div>

                <div className="central-content">
                    {!isMultiSelect ? (
                        selectedUsers.length > 0 ? (
                            <div className="message-form conversation">
                                <h2>
                                    Conversation with{' '}
                                    {users.find((user) => user.personId === selectedUsers[0])?.personName}
                                </h2>
                                <div className="messages-list">
                                    {messages[getConversationKey(selectedUsers)]?.length > 0 ? (
                                        messages[getConversationKey(selectedUsers)].map((msg, index) => (
                                            msg.content !== 'User is typing...' && (
                                                <div
                                                    key={index}
                                                    className={`message ${msg.senderId === user.id ? 'my-message' : 'user-message'}`}
                                                >
                                                    <p>{msg.content}</p>
                                                    <small>{new Date(msg.timestamp).toLocaleString()}</small>
                                                </div>
                                            )
                                        ))
                                    ) : (
                                        <p>No messages yet.</p>
                                    )}
                                </div>

                                <div ref={messagesEndRef}></div>

                                <textarea
                                    value={newMessage}
                                    onChange={handleChangeMessage}
                                    placeholder="Type a message..."
                                ></textarea>
                                <button onClick={handleSendMessage} disabled={isSending}>
                                    Send
                                </button>
                            </div>
                        ) : (
                            <div className="message-form">
                                <h3>Select a User to Start a Conversation</h3>
                            </div>
                        )
                    ) : (
                        <div className="message-form announcement">
                            <h2>Announcement</h2>
                            <textarea
                                value={newMessage}
                                onChange={handleChangeMessage}
                                placeholder="Write your announcement..."
                                rows="3"
                            />
                            <button onClick={handleSendMessage} disabled={isSending}>
                                Send Announcement
                            </button>
                        </div>
                    )}
                </div>
            </div>

            <div className="typing-indicator">
                {isTyping && (
                    <div className="typing-light">
                        <span>•</span> <span>is typing...</span>
                    </div>
                )}
            </div>

            <button
                className={`multi-select-btn ${isMultiSelect ? 'active' : ''}`}
                onClick={toggleMultiSelect}
            >
                {isMultiSelect ? 'Switch to Single Select' : 'Switch to Multi Select'}
            </button>
        </div>
    );
};

export default Messages;
