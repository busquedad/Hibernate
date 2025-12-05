// src/App.tsx

import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import PizzaDisplay from './components/PizzaDisplay';
import Login from './components/Login';
import Callback from './components/Callback';
import Dashboard from './components/Dashboard'; // Import Dashboard
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
        <Route path="/dashboard" element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        } />
        <Route path="/" element={
          <PrivateRoute>
            <Navigate to="/dashboard" />
          </PrivateRoute>
        } />
      </Routes>
    </Router>
  );
};

export default App;
