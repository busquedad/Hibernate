import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import setupAxiosInterceptors from './services/axios-interceptor.ts';
import { WebSocketProvider } from './context/WebSocketContext.tsx';

setupAxiosInterceptors();

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <WebSocketProvider>
      <App />
    </WebSocketProvider>
  </StrictMode>,
)
