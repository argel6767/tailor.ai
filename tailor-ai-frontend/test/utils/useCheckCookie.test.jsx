import { renderHook } from "@testing-library/react";
import { MemoryRouter, useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import useCheckCookie from "../../src/utils/useCheckCookie.js";
import {isCookieExpired} from "../../src/config/cookieConfig.js";

// Mock react-router-dom's useNavigate
vi.mock("react-router-dom", async () => {
    const original = await vi.importActual("react-router-dom");
    return {
        ...original, // Preserve all original exports
        useNavigate: vi.fn(), // Mock only the `useNavigate` function
    };
});

// Mock js-cookie
vi.mock("js-cookie", () => ({
    default: {
        get: vi.fn(),
    },
}));


describe("useCheckCookie", () => {
    it("should navigate to /auth if the cookie is missing", () => {
        // Mock the cookie to be missing
        Cookies.get.mockReturnValue(undefined);
        const navigate = vi.fn();
        useNavigate.mockReturnValue(navigate);

        renderHook(() => useCheckCookie(), {
            wrapper: MemoryRouter, // Needed for React Router context
        });

        // Ensure navigation happens to /auth
        expect(navigate).toHaveBeenCalledWith("/");
    });

    it("should not navigate if the cookie exists", () => {
        // Mock the cookie to exist
        Cookies.get.mockReturnValue("valid-cookie");
        const navigate = vi.fn();
        useNavigate.mockReturnValue(navigate);

        renderHook(() => useCheckCookie(), {
            wrapper: MemoryRouter,
        });

        // Ensure navigation does not happen
        expect(navigate).not.toHaveBeenCalled();
    });

    it("should check the cookie at 15-minute intervals", () => {
        vi.useFakeTimers();

        // Mock the cookie to be missing initially
        Cookies.get.mockReturnValueOnce(undefined);

        const navigate = vi.fn();
        useNavigate.mockReturnValue(navigate);

        renderHook(() => useCheckCookie(), {
            wrapper: MemoryRouter,
        });

        // Ensure the first check navigates
        expect(navigate).toHaveBeenCalledWith("/auth");

        // Advance time by 15 minutes (900,000ms)
        vi.advanceTimersByTime(15 * 60 * 1000);

        // Ensure the check runs again
        expect(navigate).toHaveBeenCalledTimes(1); // Once for initial and once for the interval

        vi.useRealTimers();
    });
});
