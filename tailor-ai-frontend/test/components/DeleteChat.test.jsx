import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { DeleteChat } from '../../src/components/DeleteChat.jsx';
import deleteChatSession from "../../src/api/chat_session/deleteChatSession.js";

// Mock the deleteChatSession function
vi.mock('../../src/api/chat_session/deleteChatSession', () => ({
    default: vi.fn(() => Promise.resolve(/* mock response */)),
}));

// Import the images for checking
import cancelButton from '../../src/assets/cancel_button.svg';

describe('DeleteChat Component', () => {
    // Utility function to create a mock component with props
    const createMockComponent = (props = {}) => {
        const defaultProps = {
            chatSessionId: '123',
            onDelete: vi.fn(),
        };
        return render(<DeleteChat {...defaultProps} {...props} />);
    };

    // Test initial render
    it('renders delete button with correct initial state', () => {
        createMockComponent();

        const deleteButton = screen.getByRole('button', { class: /btn-circle/ });
        expect(deleteButton).toBeInTheDocument();
        expect(deleteButton).toHaveClass('bg-secondary');
    });

    // Test delete confirmation toggle
    it('toggles delete confirmation when delete button is clicked', () => {
        createMockComponent();

        const deleteButton = screen.getByRole('button', { class: /btn-circle/ });

        // First click - should change to red and show confirm button
        fireEvent.click(deleteButton);

        // Check if button turned red
        expect(deleteButton).toHaveClass('bg-red-500');

        // Check if confirm button appears
        const confirmDeleteButton = screen.getByTestId("confirm-button")
        expect(confirmDeleteButton).toBeInTheDocument();
    });

    // Test deletion process
    it('calls onDelete and deleteChatSession when confirm button is clicked', () => {
        const mockOnDelete = vi.fn();
        const mockChatSessionId = '456';

        createMockComponent({
            chatSessionId: mockChatSessionId,
            onDelete: mockOnDelete
        });

        // First click to activate confirmation
        const deleteButton = screen.getByRole('button', { class: /btn-circle/ });
        fireEvent.click(deleteButton);

        // Click confirm button
        const confirmDeleteButton = screen.getByTestId("confirm-button");
        fireEvent.click(confirmDeleteButton);

        // Verify functions were called
        expect(mockOnDelete).toHaveBeenCalled();
        expect(deleteChatSession).toHaveBeenCalledWith(mockChatSessionId);
    });

    // Test image changes
    it('changes button image based on delete confirmation state', () => {
        createMockComponent();

        const deleteButton = screen.getByRole('button', { class: /btn-circle/ });

        // Initial state - should have delete button image
        const initialImage = deleteButton.querySelector('img');
        expect(initialImage).toHaveAttribute('src', "/src/assets/delete_button.svg");

        // Click to activate confirmation
        fireEvent.click(deleteButton);

        // Should now have cancel button image
        const confirmationImage = deleteButton.querySelector('img');
        expect(confirmationImage).toHaveAttribute('src', cancelButton);
    });

    // Test cancel functionality
    it('allows canceling delete confirmation', () => {
        const mockOnDelete = vi.fn();

        createMockComponent({ onDelete: mockOnDelete });

        const deleteButton = screen.getByRole('button', { class: /btn-circle/ });

        // First click to activate confirmation
        fireEvent.click(deleteButton);

        // Confirm button should now be visible
        const confirmDeleteButton = screen.getByTestId("confirm-button");
        expect(confirmDeleteButton).toBeInTheDocument();

        // Second click should return to initial state
        fireEvent.click(deleteButton);

        // Confirm button should no longer be visible
        expect(screen.getAllByRole('button', {class: /btn btn-circle bg-secondary hover:bg-accent/})).length.lessThanOrEqual(1); //there will be only one button in dom
        expect(mockOnDelete).not.toHaveBeenCalled();
    });
});