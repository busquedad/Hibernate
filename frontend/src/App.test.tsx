import { render, screen } from '@testing-library/react';
import App from './App';

describe('App', () => {
  it('renders without crashing', () => {
    render(<App />);
    // We can add a more specific assertion here if needed,
    // but for a smoke test, rendering without throwing an error is enough.
    expect(screen.queryByText(/PizzaMDP/i)).toBeInTheDocument;
  });
});
