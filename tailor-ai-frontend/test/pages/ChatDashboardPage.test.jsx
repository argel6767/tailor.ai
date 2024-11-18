// ChatDashboardPage.test.jsx
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ChatDashboardPage from '../../src/pages/ChatDashboardPage.jsx';
import { vi } from 'vitest';
import * as ReactRouterDom from 'react-router-dom';
import createChatSession from '../../src/api/createChatSession.js';

// Mock the dependencies
vi.mock('../../src/api/createChatSession.js', () => ({
    default: vi.fn()
}));
vi.mock('react-router-dom', () => ({
    ...vi.importActual('react-router-dom'),
    useNavigate: vi.fn(),
}));

beforeEach(() => {
    vi.clearAllMocks();
    // Mock localStorage.getItem
    Storage.prototype.getItem = vi.fn(() => 'test@example.com');
});

test('Displays pdfReminder when a non-PDF file is selected', async () => {
    render(<ChatDashboardPage />);

    const fileInput = screen.getByTestId('file-input');
    const nonPdfFile = new File(['dummy content'], 'resume.txt', { type: 'text/plain' });


    Object.defineProperty(fileInput, 'files', { value: [nonPdfFile] });

    fireEvent.change(fileInput);

    // Check if the pdfReminder message is visible
    expect(screen.getByText('Must be of type PDF')).toHaveClass('visible');

    // Wait for the reminder to become invisible after 2 seconds
    await waitFor(
        () => expect(screen.getByText('Must be of type PDF')).toHaveClass('invisible'),
        { timeout: 3000 }
    );
});

test('Enables "Begin chat" button when a PDF file is selected', () => {
    render(<ChatDashboardPage />);

    const fileInput = screen.getByTestId('file-input');
    const pdfFile = new File(['dummy content'], 'resume.pdf', { type: 'application/pdf' });

    // Simulate selecting a PDF file
    Object.defineProperty(fileInput, 'files', {
        value: [pdfFile],
    });
    Object.defineProperty(fileInput, 'value', {
        value: 'C:\\fakepath\\resume.pdf',
    });

    fireEvent.change(fileInput);

    const beginChatButton = screen.getByRole('button', { name: /Begin chat/i });

    // Check if the button is enabled and has the correct class
    expect(beginChatButton).toBeEnabled();
    expect(beginChatButton).toHaveClass('btn-primary');
});

test('Calls createChatSession and navigates to the chat page on clicking "Begin chat"', async () => {
    const mockNavigate = vi.fn();
    ReactRouterDom.useNavigate.mockReturnValue(mockNavigate);
    createChatSession.mockResolvedValue({ id: '12345' });

    render(<ChatDashboardPage />);

    const fileInput = screen.getByTestId('file-input');
    const pdfFile = new File(['dummy content'], 'resume.pdf', { type: 'application/pdf' });

    // Simulate selecting a PDF file
    Object.defineProperty(fileInput, 'files', {
        value: [pdfFile],
    });
    Object.defineProperty(fileInput, 'value', {
        value: 'C:\\fakepath\\resume.pdf',
    });

    fireEvent.change(fileInput);

    const beginChatButton = screen.getByRole('button', { name: /Begin chat/i });

    // Click the "Begin chat" button
    fireEvent.click(beginChatButton);

    // Wait for createChatSession to be called
    await waitFor(() => expect(createChatSession).toHaveBeenCalled());

    // Check if createChatSession was called with the correct arguments
    expect(createChatSession).toHaveBeenCalledWith('test@example.com', 'C:\\fakepath\\resume.pdf');

    // Check if navigate was called with the correct route
    expect(mockNavigate).toHaveBeenCalledWith('/chats/12345');
});
