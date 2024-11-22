// ChattingContainer.test.jsx

import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ChattingContainer from "../../src/components/ChattingContainer.jsx";
import { useParams } from 'react-router-dom';
import getChatSession from "../../src/api/chat_session/getChatSession.js";
import changeChatSessionName from "../../src/api/chat_session/changeChatSessionName.js";
import createMessage from "../../src/api/message/createMessage.js";
import requestAiResponse from "../../src/api/ai/requestAiResponse.js";
import { vi } from 'vitest';

// Mock the useParams hook
vi.mock('react-router-dom', () => ({
    useParams: vi.fn(),
}));

// Mock the API calls
vi.mock('../../src/api/chat_session/getChatSession.js');
vi.mock('../../src/api/chat_session/changeChatSessionName.js' , () => ({
    default: vi.fn(),
}));
vi.mock('../../src/api/message/createMessage.js');
vi.mock('../../src/api/ai/requestAiResponse.js');


describe('ChattingContainer', () => {
    beforeEach(() => {
        vi.resetAllMocks();
        // Mock useParams to return an id
        useParams.mockReturnValue({ id: '123' });
    });

    test('renders correctly with initial message history', async () => {
        // Mock getChatSession to return a chat name
        getChatSession.mockResolvedValue({ chatSessionName: 'Test Chat' });

        const initialMessageHistory = [
            {
                messageId: '0',
                body: 'System Message',
                author: 'SYSTEM',
                chatSessionId: '123',
            },
            {
                messageId: '1',
                body: 'Hello',
                author: 'USER',
                chatSessionId: '123',
            },
            {
                messageId: '2',
                body: 'Hi there!',
                author: 'ASSISTANT',
                chatSessionId: '123',
            },
        ];

        render(<ChattingContainer initialMessageHistory={initialMessageHistory} />);

        // Wait for the chat name to be set
        await waitFor(() => {
            expect(screen.getByText('Test Chat')).toBeInTheDocument();
        });

        // Check that the initial messages are rendered
        expect(screen.getByText('Hello')).toBeInTheDocument();
        expect(screen.getByText('Hi there!')).toBeInTheDocument();
    });

    test('allows changing the chat session name', async () => {
        getChatSession.mockResolvedValue({ chatSessionName: 'Test Chat' });
       vi.mocked(changeChatSessionName().mockResolvedValue({}));

        render(<ChattingContainer initialMessageHistory={[ {
            messageId: '0',
            body: 'System Message',
            author: 'SYSTEM',
            chatSessionId: '123',
        },]} />);

        // Wait for the chat name to be set
        await waitFor(() => {
            expect(screen.getByText('Test Chat')).toBeInTheDocument();
        });

        // Click on the chat name to edit
        fireEvent.click(screen.getByText('Test Chat'));

        // Now, the input should be visible
        const input = screen.getByPlaceholderText('Test Chat');
        expect(input).toBeInTheDocument();

        // Change the input value
        fireEvent.change(input, { target: { value: 'New Chat Name' } });

        // Click the Save button
        const saveButton = screen.getByText('Save');
        fireEvent.click(saveButton);

        // Wait for changeChatSessionName to be called
        await waitFor(() => {
            expect(changeChatSessionName).toHaveBeenCalledWith('123', 'New Chat Name');
        });

        // The new chat name should be displayed
        expect(screen.getByText('New Chat Name')).toBeInTheDocument();
    });

    test('sends a message and receives AI response', async () => {
        getChatSession.mockResolvedValue({ chatSessionName: 'Test Chat' });
        createMessage.mockResolvedValue({});
        requestAiResponse.mockResolvedValue({ response: 'This is the AI response.' });

        render(<ChattingContainer initialMessageHistory={[ {
            messageId: '0',
            body: 'System Message',
            author: 'SYSTEM',
            chatSessionId: '123',
        },]} />);

        // Wait for the chat name to be set
        await waitFor(() => {
            expect(screen.getByText('Test Chat')).toBeInTheDocument();
        });

        // Type a message in the input
        const input = screen.getByPlaceholderText('Message AI');
        fireEvent.change(input, { target: { value: 'Hello AI' } });

        // Click the Send button
        const sendButton = screen.getByText('Send');
        fireEvent.click(sendButton);

        // createMessage should be called with the message
        await waitFor(() => {
            expect(createMessage).toHaveBeenCalledWith(
                {
                    message: 'Hello AI',
                    author: 'USER',
                },
                '123'
            );
        });

        // The user's message should be displayed
        expect(screen.getByText('Hello AI')).toBeInTheDocument();

        // requestAiResponse should be called with the message
        expect(requestAiResponse).toHaveBeenCalledWith({
            chatSessionId: '123',
            userMessage: 'Hello AI',
        });

        // Wait for the AI response to be displayed
        await waitFor(() => {
            expect(screen.getByText('This is the AI response.')).toBeInTheDocument();
        });
    });

    test('displays loading indicator while waiting for AI response', async () => {
        getChatSession.mockResolvedValue({ chatSessionName: 'Test Chat' });
        createMessage.mockResolvedValue({});

        // Mock requestAiResponse to delay
        let resolveAiResponse;
        const aiResponsePromise = new Promise((resolve) => {
            resolveAiResponse = resolve;
        });
        requestAiResponse.mockReturnValue(aiResponsePromise);

        render(<ChattingContainer initialMessageHistory={[]} />);

        // Wait for the chat name to be set
        await waitFor(() => {
            expect(screen.getByText('Test Chat')).toBeInTheDocument();
        });

        // Type a message in the input
        const input = screen.getByPlaceholderText('Message AI');
        fireEvent.change(input, { target: { value: 'Hello AI' } });

        // Click the Send button
        const sendButton = screen.getByText('Send');
        fireEvent.click(sendButton);

        // The loading indicator should be displayed
        expect(screen.getByText('', { selector: 'span.loading' })).toBeInTheDocument();

        // Now resolve the AI response
        resolveAiResponse({ response: 'AI response after loading' });

        // Wait for the AI response to be displayed
        await waitFor(() => {
            expect(screen.queryByText('', { selector: 'span.loading' })).not.toBeInTheDocument();
            expect(screen.getByText('AI response after loading')).toBeInTheDocument();
        });
    });
});
