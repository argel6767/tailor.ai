import { render, screen, waitFor } from '@testing-library/react';
import { vi } from 'vitest';

// Mock the useParams hook
vi.mock('react-router-dom', () => ({
    useParams: vi.fn(),
}));

// Mock the getChatHistory API call
vi.mock('../../src/api/message/getChatHistory.js', () => ({
    default: vi.fn(),
}));

// Mock the Sidebar component
vi.mock('../../src/components/Sidebar.jsx', () => ({
    default: () => <div data-testid="sidebar">Sidebar</div>,
}));

// Mock the ChattingContainer component
vi.mock('../../src/components/ChattingContainer.jsx', () => ({
    default: ({ initialMessageHistory }) => (
        <div data-testid="chatting-container">
            ChattingContainer with {initialMessageHistory.length} messages
        </div>
    ),
}));

// Mock the Loading component
vi.mock('../../src/components/Loading.jsx', () => ({
    default: ({ loadingMessage }) => (
        <div data-testid="loading">{loadingMessage}</div>
    ),
}));


import { useParams } from 'react-router-dom';
import getChatHistory from "../../src/api/message/getChatHistory.js";
import ChatSessionPage from '../../src/pages/ChatSessionPage';

describe('ChatSessionPage', () => {
    beforeEach(() => {
        vi.resetAllMocks();
    });

    test('renders Loading component initially', () => {
        useParams.mockReturnValue({ id: '123' });
        getChatHistory.mockResolvedValue([]);

        render(<ChatSessionPage />);

        // Check that the Loading component is displayed
        expect(screen.getByTestId('loading')).toBeInTheDocument();
        expect(screen.getByText('Grabbing chat history...')).toBeInTheDocument();
    });

    test('fetches chat history and renders ChattingContainer', async () => {
        useParams.mockReturnValue({ id: '123' });
        const mockMessages = [
            {
                messageId: '0',
                body: 'System Message',
                author: 'SYSTEM',
                chatSessionId: '123',
            },
            { messageId: '1', body: 'Hello', author: 'USER', chatSessionId: '123' },
            {
                messageId: '2',
                body: 'Hi there!',
                author: 'ASSISTANT',
                chatSessionId: '123',
            },
        ];
        getChatHistory.mockResolvedValue(mockMessages);

        render(<ChatSessionPage />);

        // Initially, the Loading component should be displayed
        expect(screen.getByTestId('loading')).toBeInTheDocument();

        // Wait for the chat history to be fetched
        await waitFor(() => {
            // The Loading component should no longer be in the document
            expect(screen.queryByTestId('loading')).not.toBeInTheDocument();
            // The ChattingContainer should be displayed
            expect(screen.getByTestId('chatting-container')).toBeInTheDocument();
        });

        // Verify that ChattingContainer received the correct props
        expect(
            screen.getByText('ChattingContainer with 3 messages')
        ).toBeInTheDocument();
    });
});
