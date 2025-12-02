// src/services/pizzaService.test.ts

import { describe, it, expect, vi, afterEach } from 'vitest';
import { fetchPizzas } from './pizzaService';

// Mock global fetch
global.fetch = vi.fn();

describe('pizzaService', () => {

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('should fetch pizzas successfully', async () => {
    const mockPizzas = [
      { id_tamanio_pizza: 1, nombre: 'Grande', cant_porciones: 8 },
      { id_tamanio_pizza: 2, nombre: 'Chica', cant_porciones: 4 },
    ];

    (fetch as vi.Mock).mockResolvedValue({
      ok: true,
      json: () => Promise.resolve(mockPizzas),
    });

    const pizzas = await fetchPizzas();
    expect(pizzas).toEqual(mockPizzas);
    expect(fetch).toHaveBeenCalledWith('/api/pizzas');
  });

  it('should throw an error if the fetch fails', async () => {
    (fetch as vi.Mock).mockResolvedValue({
      ok: false,
    });

    await expect(fetchPizzas()).rejects.toThrow('Failed to fetch pizzas');
  });
});
