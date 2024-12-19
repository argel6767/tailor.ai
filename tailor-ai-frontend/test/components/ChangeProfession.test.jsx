import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { render, screen, cleanup, fireEvent } from '@testing-library/react';
import { ChangeProfession } from '../../src/components/ChangeProfession.jsx';
import { useGlobalContext } from '../../src/components/GlobalContext.jsx';
import addUserProfession from '../../src/api/user/addUserProfession';

// Mock dependencies
vi.mock('../../src/components/GlobalContext.jsx', () => ({
    useGlobalContext: vi.fn()
}));

vi.mock('../../src/api/user/addUserProfession', () => ({
    default: vi.fn()
}));

// Mock sessionStorage
const mockSessionStorage = {
    getItem: vi.fn()
};
Object.defineProperty(window, 'sessionStorage', {
    value: mockSessionStorage
});

describe('ChangeProfession', () => {
    const mockToken = 'fake-token';
    const mockSetUser = vi.fn();
    const mockEmail = 'test@example.com';
    const initialProfession = 'Software Engineer';

    beforeEach(() => {
        useGlobalContext.mockReturnValue({
            token: mockToken,
            setUser: mockSetUser
        });
        mockSessionStorage.getItem.mockReturnValue(mockEmail);
    });

    afterEach(() => {
        cleanup();
        vi.clearAllMocks();
    });

    it('renders initial profession correctly', () => {
        render(<ChangeProfession profession={initialProfession} />);
        expect(screen.getByText(`Profession: ${initialProfession}`)).toBeInTheDocument();
        expect(screen.getByText('Change Profession')).toBeInTheDocument();
    });

    it('switches to edit mode when Change Profession is clicked', async () => {
        render(<ChangeProfession profession={initialProfession} />);

        fireEvent.click(screen.getByText('Change Profession'));

        expect(screen.getByPlaceholderText(initialProfession)).toBeInTheDocument();
        expect(screen.getByText('Cancel')).toBeInTheDocument();
    });

    it('returns to view mode when Cancel is clicked', async () => {
        render(<ChangeProfession profession={initialProfession} />);

        // Enter edit mode
        fireEvent.click(screen.getByText('Change Profession'));
        // Click cancel
        fireEvent.click(screen.getByText('Cancel'));

        expect(screen.getByText(`Profession: ${initialProfession}`)).toBeInTheDocument();
        expect(screen.getByText('Change Profession')).toBeInTheDocument();
    });

    it('updates profession when new value is submitted', async () => {
        const newProfession = 'Data Scientist';

        render(<ChangeProfession profession={initialProfession} />);

        // Enter edit mode
        fireEvent.click(screen.getByText('Change Profession'));

        // Type new profession
        const input = screen.getByPlaceholderText(initialProfession);
        fireEvent.input(input, newProfession);

        // Submit new profession
        const confirmButton = screen.getByRole('button', { name: /confirm button/i });
        fireEvent.click(confirmButton);

        // Check if API was called with correct parameters
        expect(addUserProfession).toHaveBeenCalledWith(
            {
                email: mockEmail,
                profession: newProfession
            },
            mockToken
        );

        // Check if user state was updated
        expect(mockSetUser).toHaveBeenCalled();
        expect(screen.getByText(`Profession: ${newProfession}`)).toBeInTheDocument();
    });

    it('does not update profession when empty value is submitted', async () => {
        render(<ChangeProfession profession={initialProfession} />);

        // Enter edit mode
        fireEvent.click(screen.getByText('Change Profession'));

        // Submit without entering new value
        const confirmButton = screen.getByRole('button', { name: /confirm button/i });
        fireEvent.click(confirmButton);

        // API should not be called
        expect(addUserProfession).not.toHaveBeenCalled();
        expect(mockSetUser).not.toHaveBeenCalled();

        // Should still be in edit mode
        expect(screen.getByPlaceholderText(initialProfession)).toBeInTheDocument();
    });

    it('maintains current profession if canceled', async () => {
        render(<ChangeProfession profession={initialProfession} />);

        // Enter edit mode
       fireEvent.click(screen.getByText('Change Profession'));

        // Type new profession but cancel
        const input = screen.getByPlaceholderText(initialProfession);
        fireEvent.input(input, 'New Profession');
        fireEvent.click(screen.getByText('Cancel'));

        // Check if original profession is still displayed
        expect(screen.getByText(`Profession: ${initialProfession}`)).toBeInTheDocument();
        expect(addUserProfession).not.toHaveBeenCalled();
        expect(mockSetUser).not.toHaveBeenCalled();
    });
});