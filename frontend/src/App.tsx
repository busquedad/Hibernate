// src/App.tsx

import React, { useEffect, useState } from 'react';
import PizzaDisplay from './components/PizzaDisplay';
import { fetchPizzas } from './services/pizzaService';
import type { PizzaData } from './services/pizzaService';

/**
 * The main component of the Pizza Management Dashboard application.
 * It fetches pizza data from the API and displays it using the PizzaDisplay component.
 *
 * @returns {JSX.Element} The rendered App component.
 *
 * @example
 * ```tsx
 * <App />
 * ```
 */
const App: React.FC = () => {
  const [pizzas, setPizzas] = useState<PizzaData[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadPizzaData = async () => {
      try {
        const pizzaData = await fetchPizzas();
        setPizzas(pizzaData);
      } catch (err) {
        setError('Failed to load pizza data. Please try again later.');
      }
    };

    loadPizzaData();
  }, []);

  return (
    <div className="flex justify-center items-center h-screen bg-gray-100">
      <div className="container mx-auto p-4">
        <h1 className="text-4xl font-bold text-center text-blue-600 mb-8">
          Pizza Management Dashboard
        </h1>
        <PizzaDisplay
          pizzas={pizzas}
          error={error}
        />
      </div>
    </div>
  );
};

export default App;
