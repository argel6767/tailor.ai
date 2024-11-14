import {describe, it, expect, vi} from 'vitest';
import {render, screen, fireEvent} from '@testing-library/react';
import VerifyPage from '../src/pages/VerifyPage.jsx';
import verifyUser from "../src/api/verifyUser.js";

//Mock verify function
vi.mock('../src/api/verifyUser.js', () => ({
    default: vi.fn()
}));

describe ('VerifyPage', () => {
    it('renders correct', () => {
        render(<VerifyPage/>);
        expect(screen.getByText(/Enter your email and verification code below./i)).toBeInTheDocument();
    });

    it('calls verifyUser with  details submitted', () => {
        render(<VerifyPage />);
        const emailInput = screen.getByPlaceholderText('Email');
        const verificationInput = screen.getByPlaceholderText('Verification Code')
        const verifyAccount = screen.getByText('Verify Account');
        fireEvent.change(emailInput, { target: { value: 'test@example.com' } });
        fireEvent.change(verificationInput, { target: { value: '565656' } });
        fireEvent.click(verifyAccount);
        expect(verifyUser).toHaveBeenCalledWith({
            "email": "test@example.com",
            "verificationToken": "565656"
        })
    })
});
