import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import { vi } from 'vitest';
import { UserAvatar } from '../../src/components/UserAvatar.jsx';
import { GlobalProvider } from "../../src/components/GlobalContext.jsx";
import React from 'react';

describe('UserAvatar Component', () => {
    beforeEach(() => {
        vi.resetAllMocks();
    });

    const renderWithGlobalContext = (ui, { token = null, expiration = 0, ...options } = {}) => {
        const mockContextValue = {
            token,
            setToken: vi.fn(),
            expiration,
            setExpiration: vi.fn(),
        };

        return render(
            <GlobalProvider value={mockContextValue}>
                <Router>
                    {ui}
                </Router>
            </GlobalProvider>,
            options
        );
    };

    it('renders the avatar image', () => {
        renderWithGlobalContext(<UserAvatar />);

        const avatarImg = screen.getByAltText('Profile Avatar');
        expect(avatarImg).toBeInTheDocument();
        expect(avatarImg).toHaveAttribute(
            'src',
            'https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg'
        );
    });

    it('displays "Sign In" when the user is not signed in', async () => {
        // No token provided, simulates not signed in
        renderWithGlobalContext(<UserAvatar />);

        const buttons = screen.getAllByRole('button');
        const avatarButton = buttons[0];
        fireEvent.click(avatarButton);

        const signInLink = await screen.findByText('Sign In');
        expect(signInLink).toBeInTheDocument();
        expect(signInLink.closest('a')).toHaveAttribute('href', '/auth');
    });

    it('displays "Sign Out" when the user is signed in', async () => {
        const refreshAppMock = vi.fn();
        const setTokenMock = vi.fn();

        // Provide a token so that Sign Out is available
        renderWithGlobalContext(<UserAvatar refreshApp={refreshAppMock} />, {
            token: 'mock-token',
            setToken: setTokenMock
        });

        const buttons = screen.getAllByRole('button');
        const avatarButton = buttons[0];
        fireEvent.click(avatarButton);

        const signOutButton = await screen.findByText('Sign Out');
        expect(signOutButton).toBeInTheDocument();
    });

    it('calls handleIsSignedIn when the avatar is clicked', () => {
        renderWithGlobalContext(<UserAvatar />);
        const buttons = screen.getAllByRole('button');
        const avatarButton = buttons[0];
        fireEvent.click(avatarButton);

    });

    it("calls handleSignOut when Sign Out is pressed", async () => {
        const refreshAppMock = vi.fn();
        const setTokenMock = vi.fn();

        // Provide a token so that Sign Out is available
        renderWithGlobalContext(<UserAvatar refreshApp={refreshAppMock} />, {
            token: 'mock-token',
            setToken: setTokenMock
        });
        const buttons = screen.getAllByRole('button');
        const avatarButton = buttons[0];
        fireEvent.click(avatarButton);

        const signOutButton = await screen.findByText("Sign Out");
        expect(signOutButton).toBeInTheDocument();
        fireEvent.click(signOutButton);

        expect(refreshAppMock).toHaveBeenCalled();
        expect(window.location.pathname).toEqual("/");
    });

});
