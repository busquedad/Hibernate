// src/components/OrderForm.tsx
import React, { useState, forwardRef, useImperativeHandle } from 'react';
import { createOrder, CreateOrderDto } from '../services/pizzaService';

interface OrderFormProps {
  onOrderCreated: () => void; // Callback to notify parent component
}

export interface OrderFormHandle {
  resetProcessingState: () => void;
}

const OrderForm = forwardRef<OrderFormHandle, OrderFormProps>(({ onOrderCreated }, ref) => {
  const [idVariedadPizza, setIdVariedadPizza] = useState('');
  const [idTamanioPizza, setIdTamanioPizza] = useState('');
  const [notas, setNotas] = useState('');
  const [isProcessing, setIsProcessing] = useState(false); // New state for async processing
  const [error, setError] = useState<string | null>(null);
  const [lastOrderMessage, setLastOrderMessage] = useState<string | null>(null);

  useImperativeHandle(ref, () => ({
    resetProcessingState: () => {
      setIsProcessing(false);
      setLastOrderMessage('Order confirmed and created!');
    }
  }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsProcessing(true);
    setError(null);
    setLastOrderMessage(null);

    const orderData: CreateOrderDto = {
      idVariedadPizza: parseInt(idVariedadPizza, 10),
      idTamanioPizza: parseInt(idTamanioPizza, 10),
      notas,
    };

    try {
      await createOrder(orderData);
      // The request was accepted (HTTP 202), now we wait for WebSocket confirmation.
      setLastOrderMessage('Order request received. Processing...');
      // Clear form
      setIdVariedadPizza('');
      setIdTamanioPizza('');
      setNotas('');
      // The parent component will handle the final state update via WebSocket.
      // No need to call onOrderCreated() here anymore.
    } catch (err) {
      setError('Failed to submit order. Please try again.');
      setIsProcessing(false); // Stop processing on submission failure
    }
    // We intentionally leave isProcessing as true until a WebSocket message arrives.
    // A more robust implementation might add a timeout.
    // The parent component (`Dashboard`) will be responsible for resetting this state.
  };

  return (
    <div className="p-4 border rounded shadow-md mb-4">
      <h2 className="text-xl font-bold mb-2">Create New Order</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-2">
          <label htmlFor="variedad" className="block">Variety ID:</label>
          <input
            id="variedad"
            type="number"
            value={idVariedadPizza}
            onChange={(e) => setIdVariedadPizza(e.target.value)}
            className="w-full p-2 border rounded"
            required
          />
        </div>
        <div className="mb-2">
          <label htmlFor="tamanio" className="block">Size ID:</label>
          <input
            id="tamanio"
            type="number"
            value={idTamanioPizza}
            onChange={(e) => setIdTamanioPizza(e.target.value)}
            className="w-full p-2 border rounded"
            required
          />
        </div>
        <div className="mb-2">
          <label htmlFor="notas" className="block">Notes:</label>
          <textarea
            id="notas"
            value={notas}
            onChange={(e) => setNotas(e.target.value)}
            className="w-full p-2 border rounded"
          />
        </div>
        <button
          type="submit"
          disabled={isProcessing}
          className="bg-blue-500 text-white p-2 rounded disabled:bg-gray-400"
        >
          {isProcessing ? 'Processing...' : 'Create Order'}
        </button>
        {error && <p className="text-red-500 mt-2">{error}</p>}
        {lastOrderMessage && <p className="text-green-500 mt-2">{lastOrderMessage}</p>}
      </form>
    </div>
  );
});

export default OrderForm;
