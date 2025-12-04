// src/context/WebSocketContext.tsx
import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

interface WebSocketContextType {
  stompClient: Client | null;
  isConnected: boolean;
}

const WebSocketContext = createContext<WebSocketContextType>({
  stompClient: null,
  isConnected: false,
});

export const useWebSocket = () => {
  return useContext(WebSocketContext);
};

interface WebSocketProviderProps {
  children: ReactNode;
}

export const WebSocketProvider: React.FC<WebSocketProviderProps> = ({ children }) => {
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('access_token');
    if (!token) {
      // No token, no connection
      return;
    }

    const client = new Client({
      webSocketFactory: () => new SockJS('/ws-pizza'),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      onConnect: () => {
        setIsConnected(true);
        console.log('WebSocket Connected');
      },
      onDisconnect: () => {
        setIsConnected(false);
        console.log('WebSocket Disconnected');
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
    });

    client.activate();
    setStompClient(client);

    return () => {
      if (client) {
        client.deactivate();
      }
    };
  }, []);

  return (
    <WebSocketContext.Provider value={{ stompClient, isConnected }}>
      {children}
    </WebSocketContext.Provider>
  );
};
