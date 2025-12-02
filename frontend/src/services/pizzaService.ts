// src/services/pizzaService.ts

const API_URL = '/api';

export interface PizzaData {
  id_tamanio_pizza: number;
  nombre: string;
  cant_porciones: number;
}

export const fetchPizzas = async (): Promise<PizzaData[]> => {
  const response = await fetch(`${API_URL}/pizzas`);
  if (!response.ok) {
    throw new Error('Failed to fetch pizzas');
  }
  return response.json();
};
