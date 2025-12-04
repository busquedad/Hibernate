// src/components/Dashboard.tsx
import React, { useState, useEffect, useCallback, useRef, forwardRef, useImperativeHandle } from 'react';
import { fetchOrders, Order } from '../services/pizzaService';
import OrdersList from './OrdersList';
import OrderForm from './OrderForm';
import { useWebSocket } from '../context/WebSocketContext';
import { getUserRoles } from '../utils/auth';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { IMessage } from '@stomp/stompjs';

const Dashboard: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { stompClient, isConnected } = useWebSocket();
  const orderFormRef = useRef<{ resetProcessingState: () => void }>(null);


  const loadOrders = useCallback(async () => {
    try {
      setIsLoading(true);
      const fetchedOrders = await fetchOrders();
      setOrders(fetchedOrders);
    } catch (err) {
      setError('Failed to fetch orders. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    loadOrders();
  }, [loadOrders]);

  const handleNewOrderMessage = useCallback((message: IMessage) => {
    const newOrder = JSON.parse(message.body) as Order;

    setOrders(prevOrders => {
      // Avoid duplicates
      if (prevOrders.some(o => o.id_orden === newOrder.id_orden)) {
        return prevOrders;
      }
      return [newOrder, ...prevOrders];
    });

    toast.success(`🚀 New order received: #${newOrder.id_orden}`);

    // Reset the form state
    if (orderFormRef.current) {
        orderFormRef.current.resetProcessingState();
    }

  }, []);


  useEffect(() => {
    if (isConnected && stompClient) {
      const roles = getUserRoles();

      const isAdmin = roles.includes('ROLE_ADMINISTRADOR');

      let subscription;
      if (isAdmin) {
        subscription = stompClient.subscribe('/topic/admin/orders', handleNewOrderMessage);
        console.log("Subscribed to /topic/admin/orders");
      } else {
        // For ROLE_CLIENTE and ROLE_RIDER
        subscription = stompClient.subscribe('/user/queue/orders', handleNewOrderMessage);
        console.log("Subscribed to /user/queue/orders");
      }

      return () => {
        if (subscription) {
          subscription.unsubscribe();
        }
      };
    }
  }, [isConnected, stompClient, handleNewOrderMessage]);

  if (isLoading) {
    return <div>Loading orders...</div>;
  }

  if (error) {
    return <div className="text-red-500">{error}</div>;
  }

  // A simple check to show OrderForm only to clients
  const canCreateOrder = getUserRoles().includes('ROLE_CLIENTE');

  return (
    <div className="container mx-auto p-4">
      <ToastContainer position="top-right" autoClose={5000} hideProgressBar={false} />
      <h1 className="text-2xl font-bold mb-4">Order Dashboard</h1>
      {canCreateOrder && <OrderForm ref={orderFormRef} onOrderCreated={() => { /* No-op, handled by WS */ }} />}
      <OrdersList orders={orders} />
    </div>
  );
};

export default Dashboard;
