// src/components/PizzaDisplay.test.tsx

import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import PizzaDisplay from './PizzaDisplay';

describe('PizzaDisplay', () => {
  it('should render a list of pizzas', () => {
    const pizzas = [
      { id_tamanio_pizza: 1, nombre: 'Grande', cant_porciones: 8 },
      { id_tamanio_pizza: 2, nombre: 'Chica', cant_porciones: 4 },
    ];
    render(<PizzaDisplay pizzas={pizzas} error={null} />);

    expect(screen.getByText('Grande')).toBeInTheDocument();
    expect(screen.getByText('Chica')).toBeInTheDocument();
  });

  it('should render an error message', () => {
    render(<PizzaDisplay pizzas={[]} error="Failed to load pizzas" />);

    expect(screen.getByText('Error: Failed to load pizzas')).toBeInTheDocument();
  });

  it('should render a message when no pizzas are available', () => {
    render(<PizzaDisplay pizzas={[]} error={null} />);

    expect(screen.getByText('No pizzas available.')).toBeInTheDocument();
  });
});
