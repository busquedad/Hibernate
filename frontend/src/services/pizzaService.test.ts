// src/services/pizzaService.test.ts

import { describe, it, expect, vi, afterEach } from 'vitest';
import axios from 'axios';
import { fetchPizzas } from './pizzaService';

vi.mock('axios');

describe('pizzaService', () => {

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('should fetch pizzas successfully', async () => {
    const mockPizzas = [
      { id_tamanio_pizza: 1, nombre: 'Grande', cant_porciones: 8 },
      { id_tamanio_pizza: 2, nombre: 'Chica', cant_porciones: 4 },
    ];
    (axios.get as vi.Mock).mockResolvedValue({ data: mockPizzas });

    const pizzas = await fetchPizzas();
    expect(pizzas).toEqual(mockPizzas);
    expect(axios.get).toHaveBeenCalledWith('/api/pizzas', {
      headers: {
        Authorization: 'Bearer null'
      }
    });
  });

  it('should throw an error if the fetch fails', async () => {
    (axios.get as vi.Mock).mockRejectedValue(new Error('Network Error'));

    await expect(fetchPizzas()).rejects.toThrow('Network Error');
  });
});
