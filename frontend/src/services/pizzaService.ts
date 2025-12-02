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
export const fetchPizzas = async (): Promise<PizzaData[]> => {
  const response = await fetch(`${API_URL}/pizzas`);
  if (!response.ok) {
    throw new Error('Failed to fetch pizzas');
  }
  return response.json();
};
