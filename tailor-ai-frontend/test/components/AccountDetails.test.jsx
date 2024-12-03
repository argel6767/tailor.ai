import { describe, it, vi, expect, beforeEach, afterEach } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import { AccountDetails } from "../../src/components/AccountDetails.jsx";

// Mock the `changePassword` API call
vi.mock("../../src/api/auth/changePassword.js", () => ({
    default: vi.fn(() => Promise.resolve(true)), // Mock success response
}));

describe("AccountDetails Component", () => {
    let mockStartLoading;

    beforeAll(() => {
        global.alert = vi.fn(); // Mock window.alert
    });


    beforeEach(() => {
        mockStartLoading = vi.fn();
        localStorage.setItem("email", "test@example.com");
    });

    afterEach(() => {
        vi.clearAllMocks();
        vi.restoreAllMocks();
        localStorage.clear();
    });

    it("renders the email from localStorage", () => {
        render(<AccountDetails startLoading={mockStartLoading} />);

        expect(screen.getByText("Email: test@example.com")).toBeInTheDocument();
    });

    it("renders the password field as disabled by default", () => {
        render(<AccountDetails startLoading={mockStartLoading} />);

        const passwordField = screen.getByPlaceholderText("********");
        expect(passwordField).toBeDisabled();
    });

    it("shows the change password form when the 'Change Password' button is clicked", () => {
        render(<AccountDetails startLoading={mockStartLoading} />);

        const changePasswordButton = screen.getByText("Change Password");
        fireEvent.click(changePasswordButton);

        expect(screen.getByPlaceholderText("Enter password")).toBeEnabled();
        expect(screen.getByPlaceholderText("Enter new password")).toBeInTheDocument();
    });

    it("hides the form when the 'Cancel' button is clicked", () => {
        render(<AccountDetails startLoading={mockStartLoading} />);

        // Show the form
        const changePasswordButton = screen.getByText("Change Password");
        fireEvent.click(changePasswordButton);

        // Hide the form
        const cancelButton = screen.getByText("Cancel");
        fireEvent.click(cancelButton);

        const passwordField = screen.getByPlaceholderText("********");
        expect(passwordField).toBeDisabled();
        const newPasswordDiv = screen.getByTestId("newPasswordDiv");
        expect(newPasswordDiv.className).toContain("invisible");
    });

    it("calls `changePassword` and shows success alert on successful password change", async () => {
        const mockChangePassword = (await import("../../src/api/auth/changePassword.js")).default;

        render(<AccountDetails startLoading={mockStartLoading} />);

        // Show the form
        const changePasswordButton = screen.getByText("Change Password");
        fireEvent.click(changePasswordButton);

        // Fill out the form
        const oldPasswordField = screen.getByPlaceholderText("Enter password");
        const newPasswordField = screen.getByPlaceholderText("Enter new password");
        const submitButton = screen.getByText("Submit");

        fireEvent.change(oldPasswordField, { target: { value: "old-password" } });
        fireEvent.change(newPasswordField, { target: { value: "new-password" } });
        fireEvent.click(submitButton);

        // Verify API call
        expect(mockChangePassword).toHaveBeenCalledWith({
            email: "test@example.com",
            oldPassword: "old-password",
            newPassword: "new-password",
        });

        // Verify alerts
        expect(global.alert).toHaveBeenCalledWith("Successfully changed password");
    });

    it("shows an error alert on password change failure", async () => {
        const mockChangePassword = (await import("../../src/api/auth/changePassword.js")).default;
        mockChangePassword.mockImplementationOnce(() => Promise.resolve(false)); // Simulate failure

        render(<AccountDetails startLoading={mockStartLoading} />);

        // Show the form
        const changePasswordButton = screen.getByText("Change Password");
        fireEvent.click(changePasswordButton);

        // Fill out the form
        const oldPasswordField = screen.getByPlaceholderText("Enter password");
        const newPasswordField = screen.getByPlaceholderText("Enter new password");
        const submitButton = screen.getByText("Submit");

        fireEvent.change(oldPasswordField, { target: { value: "old-password" } });
        fireEvent.change(newPasswordField, { target: { value: "new-password" } });
        fireEvent.click(submitButton);

        // Verify API call
        expect(mockChangePassword).toHaveBeenCalledWith({
            email: "test@example.com",
            oldPassword: "old-password",
            newPassword: "new-password",
        });

        // Verify alerts
        expect(global.alert).toHaveBeenCalledWith("An error occurred. Please try again.");
    });

    it("calls `startLoading` twice during password change", async () => {
        const mockChangePassword = (await import("../../src/api/auth/changePassword.js")).default;

        render(<AccountDetails startLoading={mockStartLoading} />);

        // Show the form
        const changePasswordButton = screen.getByText("Change Password");
        fireEvent.click(changePasswordButton);

        // Fill out the form
        const oldPasswordField = screen.getByPlaceholderText("Enter password");
        const newPasswordField = screen.getByPlaceholderText("Enter new password");
        const submitButton = screen.getByText("Submit");

        fireEvent.change(oldPasswordField, { target: { value: "old-password" } });
        fireEvent.change(newPasswordField, { target: { value: "new-password" } });
        fireEvent.click(submitButton);

        // Verify `startLoading` calls
        expect(mockStartLoading).toHaveBeenCalledTimes(2);
    });
});
