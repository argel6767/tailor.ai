import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import AuthPage from '../../src/pages/AuthPage.jsx';
import loginUser from '../../src/api/auth/loginUser.js';
import registerUser from '../../src/api/auth/registerUser.js';
import * as apiModule from "../../src/api/user/getHasSetProfession.js";
import {handleLoginNavigation} from "../../src/utils/handleLoginNavigation.js";

// Mock the auth functions
vi.mock('../src/api/loginUser', () => ({
    default: vi.fn()
}));

vi.mock('../src/api/registerUser', () => ({
    default: vi.fn()
}));

vi.mock('../src/api/getHasSetProfession', () => ({
    default: vi.fn()
}))


describe('AuthPage', () => {
    let mockNavigate;
    let mockRequest;

    beforeEach(() => {
        mockNavigate = vi.fn();
        mockRequest = { email: 'test@example.com' }; // Adjust based on your request structure

        // Reset all mocks before each test
        vi.resetAllMocks();
    });

    it('renders login form by default', () => {
        render(<AuthPage />);
        expect(screen.getByText(/Welcome back, sign in to your account./i)).toBeInTheDocument();
    });

    it('switches to registration form when Sign Up is clicked', () => {
        render(<AuthPage />);
        const signUpButton = screen.getByText('Sign Up');
        fireEvent.click(signUpButton);
        expect(screen.getByText(/Welcome to Tailor.ai, sign up here./i)).toBeInTheDocument();
    });

    it('calls loginUser with correct data on login form submission', () => {
        render(<AuthPage />);

        // Fill in the form
        const emailInput = screen.getByPlaceholderText('Email');
        const passwordInput = screen.getByPlaceholderText('Password');
        const signInButton = screen.getByText('Sign in');

        fireEvent.change(emailInput, { target: { value: 'test@example.com' } });
        fireEvent.change(passwordInput, { target: { value: 'password123' } });
        fireEvent.click(signInButton);

        expect(loginUser).toHaveBeenCalledWith({
            username: 'test@example.com',
            password: 'password123'
        });
    });

    it('calls registerUser with correct data on registration form submission', async () => {
        // Mock window.location
        delete window.location;
        window.location = { href: "" };

        render(<AuthPage />);

        // Switch to registration form
        const signUpLink = screen.getByText('Sign Up');
        fireEvent.click(signUpLink);

        // Fill in the form
        const emailInput = screen.getByPlaceholderText('Email');
        const passwordInput = screen.getByPlaceholderText('Password');
        const signUpButton = screen.getByText('Sign up');

        fireEvent.change(emailInput, { target: { value: 'newuser@example.com' } });
        fireEvent.change(passwordInput, { target: { value: 'newpassword' } });
        fireEvent.click(signUpButton);

        // Wait for the function to complete
        await new Promise(process.nextTick);

        expect(registerUser).toHaveBeenCalledWith({
            username: 'newuser@example.com',
            password: 'newpassword'
        });
        expect(window.location.href).toBe("/verify");
    });

    it('toggles between login and register forms', () => {
        render(<AuthPage />);

        // Initially shows login form
        expect(screen.getByText(/Welcome back, sign in to your account./i)).toBeInTheDocument();

        // Switch to register
        fireEvent.click(screen.getByText('Sign Up'));
        expect(screen.getByText(/Welcome to Tailor.ai, sign up here./i)).toBeInTheDocument();

        // Switch back to login
        fireEvent.click(screen.getByText('Sign In'));
        expect(screen.getByText(/Welcome back, sign in to your account./i)).toBeInTheDocument();
    });

    it('should navigate to /chats when getHasSetProfession returns true', async () => {
        // Arrange: Mock getHasSetProfession to return true
        vi.spyOn(apiModule, 'default').mockResolvedValue(true);

        // Act
        await handleLoginNavigation(mockNavigate, mockRequest);

        // Assert
        expect(apiModule.default).toHaveBeenCalledWith(mockRequest);
        expect(mockNavigate).toHaveBeenCalledWith('/chats');
    });

    it('should navigate to /profession when getHasSetProfession returns false', async () => {
        // Arrange: Mock getHasSetProfession to return false
        vi.spyOn(apiModule, 'default').mockResolvedValue(false);

        // Act
        await handleLoginNavigation(mockNavigate, mockRequest);

        // Assert
        expect(apiModule.default).toHaveBeenCalledWith(mockRequest);
        expect(mockNavigate).toHaveBeenCalledWith('/profession');
    });

});
