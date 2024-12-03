import { describe, it, vi, expect, beforeEach, afterEach } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { ProfilePage } from "../../src/pages/ProfilePage.jsx";
import { AccountDetails } from "../../src/components/AccountDetails.jsx";
import { DeleteAccount } from "../../src/components/DeleteAccount.jsx";
import Loading from "../../src/components/Loading.jsx";

// Mock child components
vi.mock("../../src/components/AccountDetails.jsx", () => ({
    AccountDetails: ({ startLoading }) => (
        <button onClick={startLoading}>Mock AccountDetails</button>
    ),
}));

vi.mock("../../src/components/DeleteAccount.jsx", () => ({
    DeleteAccount: ({ startLoading }) => (
        <button onClick={startLoading}>Mock DeleteAccount</button>
    ),
}));

vi.mock("../../src/components/Loading.jsx", () => ({
    __esModule: true,
    default: ({ loadingMessage }) => <div>{loadingMessage}</div>,
}));

describe("ProfilePage Component", () => {
    afterEach(() => {
        vi.clearAllMocks();
    });

    it("renders the main page correctly when not loading", () => {
        render(<ProfilePage />);

        expect(screen.getByText("User Settings")).toBeInTheDocument();
        expect(screen.getByText("Account Details")).toBeInTheDocument();
        expect(screen.getByText("Mock AccountDetails")).toBeInTheDocument();
        expect(screen.getByText("Mock DeleteAccount")).toBeInTheDocument();
    });

    it("renders the loading screen when `isLoading` is true", () => {
        render(<ProfilePage />);

        // Simulate loading state by clicking a child button that triggers `handleLoading`
        const deleteAccountButton = screen.getByText("Mock DeleteAccount");
        fireEvent.click(deleteAccountButton);

        // Verify the loading state
        expect(screen.getByText("Handling Request")).toBeInTheDocument();
    });

    it("toggles between loading and main content", () => {
        render(<ProfilePage />);

        // Verify main content is initially displayed
        expect(screen.getByText("User Settings")).toBeInTheDocument();

        // Trigger loading state
        const accountDetailsButton = screen.getByText("Mock AccountDetails");
        fireEvent.click(accountDetailsButton);

        // Verify loading state
        expect(screen.queryByText("User Settings")).not.toBeInTheDocument();
        expect(screen.getByText("Handling Request")).toBeInTheDocument();

        // Simulate stopping the loading state
        const loadingButton = screen.getByText("Handling Request");
        fireEvent.click(loadingButton);

        // Verify main content is displayed again
        expect(screen.getByText("Account Details")).toBeInTheDocument();
    });

    it("handles loading state triggered by DeleteAccount", () => {
        render(<ProfilePage />);

        // Trigger loading state
        const deleteAccountButton = screen.getByText("Mock DeleteAccount");
        fireEvent.click(deleteAccountButton);

        // Verify loading state
        expect(screen.getByText("Handling Request")).toBeInTheDocument();
    });

    it("handles loading state triggered by AccountDetails", () => {
        render(<ProfilePage />);

        // Trigger loading state
        const accountDetailsButton = screen.getByText("Mock AccountDetails");
        fireEvent.click(accountDetailsButton);

        // Verify loading state
        expect(screen.getByText("Handling Request")).toBeInTheDocument();
    });
});
