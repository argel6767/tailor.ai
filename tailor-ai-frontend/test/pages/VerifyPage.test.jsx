import {describe, it, expect, vi} from 'vitest';
import {render, screen, fireEvent} from '@testing-library/react';
import VerifyPage from '../../src/pages/VerifyPage.jsx';
import verifyUser from "../../src/api/auth/verifyUser.js";
import resendVerificationEmail from "../../src/api/auth/resendVerificationEmail.js";

//Mock verify function
vi.mock('../../src/api/verifyUser.js', () => ({
    default: vi.fn()
}));

vi.mock('../../src/api/resendVerificationEmail.js', () => ({
    default: vi.fn()
}));

describe ('VerifyPage', () => {
    it('renders correct', () => {
        render(<VerifyPage/>);
        expect(screen.getByText(/Enter your verification code below./i)).toBeInTheDocument();
    });

    it('calls verifyUser with  details submitted', () => {
        vi.spyOn(Storage.prototype, 'getItem').mockImplementation((key) => {
            if (key === 'email') return 'test@example.com';
            return null;
        });

        // Mock localStorage.setItem
        vi.spyOn(window.localStorage.__proto__, 'setItem').mockImplementation((key, value) => {
        });
        render(<VerifyPage />);

        const verificationInput = screen.getByPlaceholderText('Verification Code');
        const verifyAccount = screen.getByText('Verify Account');
        fireEvent.change(verificationInput, { target: { value: '565656' } });
        fireEvent.click(verifyAccount);
        expect(verifyUser).toHaveBeenCalledWith({
            "email": "test@example.com",
            "verificationToken": "565656"
        })

    })

    it('has verification resent message hidden', () => {
        // Mock localStorage.getItem
        vi.spyOn(Storage.prototype, "getItem").mockImplementation((key) => {
            if (key === "email") return "test@example.com";
            return null;
        });

        // Mock window.location
        delete window.location;
        window.location = { href: "" };
        render(<VerifyPage />);
        expect(screen.getByText(/Verification code resent! Check your email./i)).toHaveClass('pt-2.5 invisible');
    })

    it('calls resendVerificationEmail with email submitted', () => {
        vi.spyOn(Storage.prototype, 'getItem').mockImplementation((key) => {
            if (key === 'email') return 'test@example.com';
            return null;
        });

        // Mock localStorage.setItem
        vi.spyOn(window.localStorage.__proto__, 'setItem').mockImplementation((key, value) => {
            // You can track or assert on this if needed
        });
        render(<VerifyPage />);

        const resend = screen.getByText('Request Another');
        fireEvent.click(resend);

        expect(resendVerificationEmail).toHaveBeenCalledWith("test@example.com");
        expect(localStorage.clear).toHaveBeenCalled();
        expect(window.location.href).toBe("/auth");
    })

    it('displays verification resent message after user requests a new one', () => {
        render(<VerifyPage />);
        const resend = screen.getByText('Request Another');
        fireEvent.click(resend);
        expect(screen.getByText(/Verification code resent! Check your email./i)).toHaveClass('pt-2.5 visible')
    })
});
