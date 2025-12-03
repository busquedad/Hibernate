// src/App.tsx

import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import PizzaDisplay from './components/PizzaDisplay';
import Login from './components/Login';
import Callback from './components/Callback';
import { fetchPizzas } from './services/pizzaService';
import type { PizzaData } from './services/pizzaService';
import axios from 'axios';

const PrivateRoute: React.FC<{ children: React.ReactElement }> = ({ children }) => {
  const isAuthenticated = !!localStorage.getItem('access_token');
  return isAuthenticated ? children : <Navigate to="/login" />;
};


const App: React.FC = () => {
  const [pizzas, setPizzas] = useState<PizzaData[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadPizzaData = async () => {
      try {
        const pizzaData = await fetchPizzas();
        setPizzas(pizzaData);
      } catch (err) {
        if (axios.isAxiosError(err) && err.response?.status === 401) {
          // The PrivateRoute will handle the redirect
        } else {
          setError('Failed to load pizza data. Please try again later.');
        }
      }
    };

    if (localStorage.getItem('access_token')) {
      loadPizzaData();
    }
  }, []);

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/callback" element={<Callback />} />
        <Route path="/" element={
          <PrivateRoute>
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
          </PrivateRoute>
        } />
      </Routes>
    </Router>
  );
};

export default App;
