import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import { vi } from 'vitest';
import { UserAvatar } from '../../src/components/UserAvatar.jsx';
import * as cookieConfig from '../../src/config/cookieConfig.js';

vi.mock('../../src/config/cookieConfig.js', () => ({
    isCookieExpired: vi.fn(),
}));

describe('UserAvatar Component', () => {
    beforeEach(() => {
        vi.resetAllMocks();
    });

    it('renders the avatar image', () => {
        render(
            <Router>
                <UserAvatar />
            </Router>
        );

        const avatarImg = screen.getByAltText('Profile Avatar');
        expect(avatarImg).toBeInTheDocument();
        expect(avatarImg).toHaveAttribute(
            'src',
            'https://upload.wikimedia.org/wikipedia/commons/a/ac/Default_pfp.jpg'
        );
    });

    it('displays "Sign In" when the user is not signed in', async () => {
        cookieConfig.isCookieExpired.mockReturnValue(true); // Simulate expired token

        render(
            <Router>
                <UserAvatar />
            </Router>
        );

        const avatarButton = screen.getByRole('button');
        fireEvent.click(avatarButton); // Simulate clicking the avatar to trigger handleIsSignedIn

        const signInLink = await screen.findByText('Sign In');
        expect(signInLink).toBeInTheDocument();
        expect(signInLink.closest('a')).toHaveAttribute('href', '/auth');
    });

    it('displays "Sign Out" when the user is signed in', async () => {
        cookieConfig.isCookieExpired.mockReturnValue(false); // Simulate valid token

        render(
            <Router>
                <UserAvatar />
            </Router>
        );

        const avatarButton = screen.getByRole('button');
        fireEvent.click(avatarButton); // Simulate clicking the avatar to trigger handleIsSignedIn

        const signOutLink = await screen.findByText('Sign Out');
        expect(signOutLink).toBeInTheDocument();
        expect(signOutLink.closest('a')).toHaveAttribute('href', '/');
    });

    it('calls handleIsSignedIn when the avatar is clicked', () => {
        const handleIsSignedInMock = vi.fn();
        render(
            <Router>
                <UserAvatar />
            </Router>
        );

        const avatarButton = screen.getByRole('button');
        fireEvent.click(avatarButton);

        // No explicit mock for handleIsSignedIn in the component, but this test ensures interaction works
        expect(cookieConfig.isCookieExpired).toHaveBeenCalled();
    });
});
