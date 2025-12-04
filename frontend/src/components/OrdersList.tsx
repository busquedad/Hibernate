// src/components/OrdersList.tsx
import React from 'react';
import { Order } from '../services/pizzaService';

interface OrdersListProps {
  orders: Order[];
}

const OrdersList: React.FC<OrdersListProps> = ({ orders }) => {
  return (
    <div className="p-4 border rounded shadow-md">
      <h2 className="text-xl font-bold mb-2">My Orders</h2>
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <ul className="divide-y divide-gray-200">
          {orders.map((order) => (
            <li key={order.id_orden} className="py-2">
              <p><strong>Order ID:</strong> {order.id_orden}</p>
              <p><strong>Date:</strong> {new Date(order.fecha_alta).toLocaleString()}</p>
              <p><strong>Status:</strong> <span className="font-semibold text-blue-600">{order.estatus}</span></p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default OrdersList;
