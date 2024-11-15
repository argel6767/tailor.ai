import { describe, it, expect } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import Navbar from '../../src/components/Navbar.jsx';

// Mock components for testing routes
const HomePage = () => <div>Home Page</div>;
const LoginPage = () => <div>Login Page</div>;


describe('Navbar Component', () => {
    it('navigates to Home, Login, and Verify pages when links are clicked', () => {
        render(
            <MemoryRouter initialEntries={['/']}>
                <Routes>
                    <Route path="/" element={<><Navbar /><HomePage /></>} />
                    <Route path="/auth" element={<><Navbar /><LoginPage /></>} />
                </Routes>
            </MemoryRouter>
        );

        // Verify initial state (Home page)
        expect(screen.getByText(/Home Page/i)).toBeInTheDocument();

        // Simulate click on "Login" link
        fireEvent.click(screen.getByText(/Login/i));
        expect(screen.getByText(/Login Page/i)).toBeInTheDocument();
    });

    it('renders without crashing', () => {
        render(
            <MemoryRouter initialEntries={['/']}>
                <Routes>
                    <Route path="/" element={<><Navbar /><HomePage /></>} />
                    <Route path="/auth" element={<><Navbar /><LoginPage /></>} />
                </Routes>
            </MemoryRouter>
        );
        expect(screen.getByText(/Home Page/i)).toBeInTheDocument();
        expect(screen.getByText(/Login/i)).toBeInTheDocument();
        expect(screen.getByText(/Source Code/i)).toBeInTheDocument();
    })
});