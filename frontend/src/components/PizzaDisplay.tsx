// src/components/PizzaDisplay.tsx

import React from 'react';

interface PizzaData {
  id_tamanio_pizza: number;
  nombre: string;
  cant_porciones: number;
}

interface PizzaDisplayProps {
  pizzas: PizzaData[];
  error: string | null;
}

/**
 * A component that displays a list of pizzas or an error message.
 *
 * @param {PizzaDisplayProps} props - The component props.
 * @param {PizzaData[]} props.pizzas - The list of pizzas to display.
 * @param {string | null} props.error - An error message to display, if any.
 * @returns {JSX.Element} The rendered PizzaDisplay component.
 *
 * @example
 * ```tsx
 * <PizzaDisplay pizzas={pizzas} error={null} />
 * ```
 */
const PizzaDisplay: React.FC<PizzaDisplayProps> = ({ pizzas, error }) => {
  if (error) {
    return <div className="text-red-500">Error: {error}</div>;
  }

  return (
    <div className="p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-4 text-gray-800">Available Pizzas</h2>
      {pizzas.length > 0 ? (
        <ul>
          {pizzas.map((pizza) => (
            <li key={pizza.id_tamanio_pizza} className="mb-4">
              <h3 className="text-xl font-semibold text-gray-700">{pizza.nombre}</h3>
              <p>ID: {pizza.id_tamanio_pizza}</p>
              <p>Slices: {pizza.cant_porciones}</p>
            </li>
          ))}
        </ul>
      ) : (
        <p>No pizzas available.</p>
      )}
    </div>
  );
};

export default PizzaDisplay;
