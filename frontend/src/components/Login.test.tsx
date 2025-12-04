import { render, screen } from '@testing-library/react';
import Login from './Login';
import { BrowserRouter } from 'react-router-dom';

describe('Login Component', () => {
  it('renders login button', () => {
    render(
      <BrowserRouter>
        <Login />
      </BrowserRouter>
    );

    // Check for the OAuth2 login button
    expect(screen.getByRole('button', { name: /Login with Spring Authorization Server/i })).toBeInTheDocument();
  });
});
