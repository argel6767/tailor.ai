import {describe, it, expect, vi} from 'vitest';
import {render, screen, fireEvent} from '@testing-library/react';
import Footer from "../src/components/Footer.jsx";

describe('<Footer />', () => {
    it('renders correctly', () => {
        render(<Footer />);
        expect(screen.getByText(/All right reserved by Tailor.ai/i)).toBeInTheDocument();
    })
})