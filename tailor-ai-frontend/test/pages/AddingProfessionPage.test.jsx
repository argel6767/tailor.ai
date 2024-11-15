import {describe, it, expect, vi,} from 'vitest';
import {render, screen, fireEvent} from '@testing-library/react';
import {MemoryRouter, Route, Routes} from 'react-router-dom';
import addUserProfession from "../../src/api/addUserProfession.js";
import AddingProfessionPage from "../../src/pages/AddingProfessionPage.jsx";

vi.mock('../../src/api/addUserProfession.js', () => ({
    default: vi.fn()
}))

describe("<AddingProfessionPage />", () => {

    it('renders without crashing', () => {
        render(<AddingProfessionPage />);
        expect(screen.getByText(/Enter your desired profession./i)).toBeInTheDocument();
        expect(screen.getByAltText(/Workers/i)).toBeInTheDocument();
        expect(screen.getByText(/Profession/i)).toBeInTheDocument();
        expect(screen.getByText(/Submit/i)).toBeInTheDocument();
    })

    it ('goes to Chats Dashboard After Submission', async () => {
        delete window.location;
        window.location = {href: ""};
        render(<AddingProfessionPage/>)
        const submit = screen.getByRole('button', {name: /Submit/i});
        fireEvent.click(submit);
        await new Promise(process.nextTick);
        expect(window.location.href).toBe('/chats');
    })

    it ('sends api request with email in local storage and profession entered', async () => {
        delete window.location;
        window.location = {href: ""};
        vi.spyOn(Storage.prototype, 'getItem').mockImplementation((key) => {
            if (key === 'email') return 'test@example.com';
            return null;
        });

        // Mock localStorage.setItem
        vi.spyOn(window.localStorage.__proto__, 'setItem').mockImplementation((key, value) => {
        });

        render(<AddingProfessionPage/>);

        const professionInput = screen.getByPlaceholderText('Profession');
        const submit = screen.getByRole('button', {name: /Submit/i});
        fireEvent.change(professionInput, { target: { value: 'Software Engineer' } });
        fireEvent.click(submit);
        expect(addUserProfession).toHaveBeenCalledWith({
            "email": "test@example.com",
            "profession": "Software Engineer"
        })
        await new Promise(process.nextTick);
        expect(window.location.href).toBe('/chats');
    })
})