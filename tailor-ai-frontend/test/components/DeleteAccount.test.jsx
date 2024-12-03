import { describe, it, vi, expect, beforeEach, afterEach } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { DeleteAccount } from "../../src/components/DeleteAccount.jsx";
import { MemoryRouter } from "react-router-dom";


const { mockNavigate, mockDeleteUser } = vi.hoisted(() => ({
    mockNavigate: vi.fn(),
    mockDeleteUser: vi.fn(() => Promise.resolve()),
}));

vi.mock("react-router-dom", async (importOriginal) => {
    const actual = await importOriginal();
    return {
        ...actual,
        useNavigate: () => mockNavigate,
        MemoryRouter: actual.MemoryRouter
    };
});

vi.mock("../../src/api/user/deleteUser.js", () => ({
    default: mockDeleteUser,
}));

describe("DeleteAccount Component", () => {
    let mockStartLoading;

    beforeEach(() => {
        mockStartLoading = vi.fn();
        localStorage.clear();
    });

    afterEach(() => {
        vi.clearAllMocks();
    });

    it("renders the Delete Account button", () => {
        render(
            <MemoryRouter>
                <DeleteAccount startLoading={mockStartLoading} />
            </MemoryRouter>
        );

        const deleteButton = screen.getByText("Delete Account");
        expect(deleteButton).toBeInTheDocument();
    });

    it("displays the confirmation dialog when Delete Account is clicked", () => {
        render(
            <MemoryRouter>
                <DeleteAccount startLoading={mockStartLoading} />
            </MemoryRouter>
        );

        const deleteButton = screen.getByText("Delete Account");
        fireEvent.click(deleteButton);

        const confirmationText = screen.getByText("Are you sure? This cannot be undone.");
        expect(confirmationText).toBeInTheDocument();
    });

    it("hides the confirmation dialog when Cancel is clicked", () => {
        render(
            <MemoryRouter>
                <DeleteAccount startLoading={mockStartLoading} />
            </MemoryRouter>
        );

        const deleteButton = screen.getByText("Delete Account");
        fireEvent.click(deleteButton);

        const cancelButton = screen.getByText("Cancel");
        fireEvent.click(cancelButton);

        const confirmationText = screen.queryByText("Are you sure? This cannot be undone.");
        expect(confirmationText).not.toBeInTheDocument();
    });

    it("calls deleteUser and navigates when Confirm is clicked", async () => {
        const mockEmail = "test@example.com";
        localStorage.setItem("email", mockEmail);

        render(
            <MemoryRouter>
                <DeleteAccount startLoading={mockStartLoading} />
            </MemoryRouter>
        );

        const deleteButton = screen.getByText("Delete Account");
        fireEvent.click(deleteButton);

        const confirmButton = screen.getByText("Confirm");
        await fireEvent.click(confirmButton);

        expect(mockStartLoading).toHaveBeenCalledTimes(1);
        expect(mockDeleteUser).toHaveBeenCalledWith(mockEmail);
        expect(mockNavigate).toHaveBeenCalledWith("/");
    });

    it("does not call deleteUser if Cancel is clicked", () => {
        render(
            <MemoryRouter>
                <DeleteAccount startLoading={mockStartLoading} />
            </MemoryRouter>
        );

        const deleteButton = screen.getByText("Delete Account");
        fireEvent.click(deleteButton);

        const cancelButton = screen.getByText("Cancel");
        fireEvent.click(cancelButton);

        expect(mockDeleteUser).not.toHaveBeenCalled();
        expect(mockNavigate).not.toHaveBeenCalled();
    });
});
