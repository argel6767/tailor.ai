import {describe, it, expect, beforeEach, vi} from 'vitest';
import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import {FilePopUp} from '../../src/components/FilePopUp.jsx';

// Mocks
vi.mock('react-router-dom', () => ({
    useParams: vi.fn(),
}));

vi.mock('../../src/components/GlobalContext.jsx', () => ({
    useGlobalContext: vi.fn(),
}));

vi.mock('../../src/api/chat_session/getPdfFile.js', () => ({
    __esModule: true,
    default: vi.fn(),
}));

import {useParams} from 'react-router-dom';
import {useGlobalContext} from '../../src/components/GlobalContext.jsx';
import getPDFFile from '../../src/api/chat_session/getPDFFile.js';



describe('FilePopUp Component', () => {
    beforeAll(() => {
        global.URL.createObjectURL = vi.fn(() => 'blob:http://localhost/fake-url');
    });
    let closePopUp;

    beforeEach(() => {
        // Reset mocks before each test
        vi.resetAllMocks();

        closePopUp = vi.fn();

        // Mocking context and params
        useParams.mockReturnValue({id: '123'});
        useGlobalContext.mockReturnValue({token: 'test-token'});

        // Mock the PDF fetching
        // This simulates a Blob response, e.g. a PDF file
        const mockBlob = new Blob(['PDF content'], {type: 'application/pdf'});
        getPDFFile.mockResolvedValue(mockBlob);
    });

    it('displays a loading state initially', async () => {
        render(<FilePopUp closePopUp={closePopUp} />);

        // The loading message should be visible right after render
        expect(screen.getByText(/Grabbing file.../i)).toBeInTheDocument();

        // Wait for the iframe to appear after fetch resolves
        await waitFor(() => {
            expect(iframe).toBeInTheDocument();
        });
    });

    it('fetches and displays the PDF after loading', async () => {
        render(<FilePopUp closePopUp={closePopUp} />);

        // Wait for the iframe to appear
        const iframe = await screen.findByTitle(/Resume for chat 123/i);
        expect(iframe).toBeInTheDocument();

        // Check that getPdfFile was called correctly with the right arguments
        expect(getPDFFile).toHaveBeenCalledWith('123', 'test-token');
    });

    it('calls closePopUp when the "Close" button is clicked', async () => {
        render(<FilePopUp closePopUp={closePopUp} />);

        // Wait for the iframe to appear
        await screen.findByTitle(/Resume for chat 123/i);

        const closeButton = screen.getByRole('button', { name: /close/i });
        fireEvent.click(closeButton);

        expect(closePopUp).toHaveBeenCalled();
    });

    it('caches the PDF URL and does not re-fetch if ID stays the same', async () => {
        const {rerender} = render(<FilePopUp closePopUp={closePopUp} />);
        await screen.findByTitle(/Resume for chat 123/i);

        // Reset mock calls to see if fetch is called again
        getPDFFile.mockClear();

        // Rerender with same ID should not refetch
        rerender(<FilePopUp closePopUp={closePopUp} />);
        await screen.findByTitle(/Resume for chat 123/i);

        expect(getPDFFile).not.toHaveBeenCalled();
    });

    it('fetches again if the ID changes', async () => {
        const {rerender} = render(<FilePopUp closePopUp={closePopUp} />);
        await screen.findByTitle(/Resume for chat 123/i);

        // Change the ID
        useParams.mockReturnValue({id: '456'});
        const mockBlob2 = new Blob(['PDF content 2'], {type: 'application/pdf'});
        getPDFFile.mockResolvedValueOnce(mockBlob2);

        rerender(<FilePopUp closePopUp={closePopUp} />);

        await screen.findByTitle(/Resume for chat 123/i);
        expect(getPDFFile).toHaveBeenCalledWith('456', 'test-token');
    });
});
