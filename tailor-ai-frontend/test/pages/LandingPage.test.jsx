import {describe, it, expect, vi} from 'vitest';
import {render, screen, fireEvent} from '@testing-library/react';
import {MemoryRouter, Route, Routes} from 'react-router-dom';
import LandingPage from "../../src/pages/LandingPage.jsx";
import AuthPage from "../../src/pages/AuthPage.jsx";

describe('LandingPage', () => {
    it('renders without crashing', () => {
        render( <MemoryRouter>
            <LandingPage />
        </MemoryRouter>);
        expect(screen.getByText(/Welcome to Tailor.ai/i)).toBeInTheDocument();
        expect(screen.getByText(/Personalized Resumes/i)).toBeInTheDocument();
        expect(screen.getByText(/Industry Insights/i)).toBeInTheDocument();
        expect(screen.getByText(/Effortless Process/i)).toBeInTheDocument();
    });

    it('goes to Login, when get started is pressed', () => {
        render( <MemoryRouter>
            <Routes>
                <Route path="/" element={<LandingPage />} />
                <Route path="/auth" element={<AuthPage />} />
            </Routes>
        </MemoryRouter>);
        const getStarted = screen.getByText(/Get Started/i);
        fireEvent.click(getStarted);
        expect(screen.getByText(/Welcome back, sign in to your account./i)).toBeInTheDocument();
    });

});