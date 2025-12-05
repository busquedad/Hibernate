// src/services/pizzaService.ts

const API_URL = '/api';

export interface PizzaData {
  id_tamanio_pizza: number;
  nombre: string;
  cant_porciones: number;
}

/**
 * Fetches the list of pizza sizes from the API.
 *
 * @returns {Promise<PizzaData[]>} A promise that resolves to an array of pizza data.
 * @throws {Error} If the API request fails.
 *
 * @example
 * ```ts
 * const pizzas = await fetchPizzas();
 * console.log(pizzas);
 * ```
 */
import axios from 'axios';

export const fetchPizzas = async (): Promise<PizzaData[]> => {
  const token = localStorage.getItem('access_token');
  const response = await axios.get(`${API_URL}/pizzas`, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
  return response.data;
};

// Simple DTO for creating an order
export interface CreateOrderDto {
  // Assuming these are the fields required by the backend
  idVariedadPizza: number;
  idTamanioPizza: number;
  notas: string;
}

// Data structure for an order returned by the API
export interface Order {
  id_orden: number;
  fecha_alta: string;
  estatus: string;
  // Add other relevant fields
}

/**
 * Fetches the list of orders from the API.
 * The backend will filter orders based on the user's role.
 * @returns {Promise<Order[]>} A promise that resolves to an array of orders.
 */
export const fetchOrders = async (): Promise<Order[]> => {
  const token = localStorage.getItem('access_token');
  const response = await axios.get('/oms/ordenes', { // Adjusted to the correct endpoint
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
  return response.data;
};

/**
 * Creates a new order.
 * Expects an HTTP 202 Accepted response.
 * @param {CreateOrderDto} orderData The data for the new order.
 * @returns {Promise<void>} A promise that resolves when the request is accepted.
 */
export const createOrder = async (orderData: CreateOrderDto): Promise<void> => {
  const token = localStorage.getItem('access_token');
  await axios.post('/oms/ordenes', orderData, { // Adjusted to the correct endpoint
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
};
