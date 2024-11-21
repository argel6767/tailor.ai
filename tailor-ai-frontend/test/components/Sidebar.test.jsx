import { render, screen, waitFor } from "@testing-library/react";
import { describe, it, vi, expect } from "vitest";
import {BrowserRouter} from "react-router-dom";
import Sidebar from "../../src/components/Sidebar.jsx";
import getUserChatSessions from "../../src/api/chat_session/getUserChatSessions.js";

// Mocked list of objects
const mockChats = [
    { id: 1, name: "General Chat" },
    { id: 2, name: "Project Updates" },
    { id: 3, name: "Team Discussions" },
];


vi.mock('../../src/api/getUserChatSessions.js', () => ({
    default: vi.fn()
}));

describe("ChatList Component", () => {
    beforeEach(() => {
        // Clear all mocks before each test
        vi.clearAllMocks();
        vi.spyOn(Storage.prototype, 'getItem').mockReturnValue('test@example.com');
    });

    it('renders without crashing', () => {
        render(
            <BrowserRouter>
                <Sidebar />
            </BrowserRouter>
        );

        expect(screen.getByText('View previous chats below.')).toBeInTheDocument();
        expect(screen.getByText('Grabbing your previous chats...')).toBeInTheDocument();
    })

    it("renders a list of chats fetched from the API", async () => {
        getUserChatSessions.mockResolvedValue(mockChats)

        render(
            <BrowserRouter>
                <Sidebar />
            </BrowserRouter>
        );

        // Wait for the component to update with the API data
        await waitFor(() => {
            expect(getUserChatSessions).toHaveBeenCalledWith('test@example.com');
            expect(localStorage.getItem).toHaveBeenCalledWith('email');
        });

        // Optionally, verify the rendered content
        await waitFor(() => {
            expect(screen.getByText('General Chat')).toBeInTheDocument();
            expect(screen.getByText('Project Updates')).toBeInTheDocument();
            expect(screen.getByText('Team Discussions')).toBeInTheDocument();
        });
    });

    it('renders loading message initially and continues to if api request fails', async () => {
        getUserChatSessions.mockResolvedValue(null);
        render(
            <BrowserRouter>
                <Sidebar />
            </BrowserRouter>
        );

        expect(screen.getByText('Grabbing your previous chats...')).toBeInTheDocument();
        // Wait for the component to update with the API data
        await waitFor(() => {
            expect(getUserChatSessions).toHaveBeenCalledWith('test@example.com');
            expect(localStorage.getItem).toHaveBeenCalledWith('email');
        });
        expect(screen.getByText('Grabbing your previous chats...')).toBeInTheDocument();
    })
});
