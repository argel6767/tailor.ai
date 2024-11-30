import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import UploadJob from '../../src/components/UploadJob.jsx';

// Mock the sleep utility
vi.mock('../../src/utils/sleep.js', () => ({
    sleep: vi.fn(() => Promise.resolve())
}));

describe('UploadJob Component', () => {
    // Test initial render
    it('renders the initial submit button', () => {
        render(<UploadJob sendUpUrl={() => {}} />);

        const submitButton = screen.getByText('Submit a LinkedIn Job');
        expect(submitButton).toBeInTheDocument();
    });

    // Test popup functionality
    it('opens and closes the popup when submit button is clicked', () => {
        render(<UploadJob sendUpUrl={() => {}} />);

        const initialSubmitButton = screen.getByText('Submit a LinkedIn Job');
        fireEvent.click(initialSubmitButton);

        // Check popup elements are visible
        expect(screen.getByText('Paste the job URL below.')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('https://www.linkedin.com/jobs/views/job_id')).toBeInTheDocument();

        // Close the popup
        const closeButton = screen.getByText('Close');
        fireEvent.click(closeButton);

        // Verify popup is closed
        expect(screen.queryByText('Paste the job URL below.')).not.toBeInTheDocument();
    });

    // Test URL validation and submission
    it('handles valid and invalid URL submissions', async () => {
        const mockSendUpUrl = vi.fn();
        render(<UploadJob sendUpUrl={mockSendUpUrl} />);

        // Open popup
        const initialSubmitButton = screen.getByText('Submit a LinkedIn Job');
        fireEvent.click(initialSubmitButton);

        const urlInput = screen.getByPlaceholderText('https://www.linkedin.com/jobs/views/job_id');
        const submitButton = screen.getByText('Submit');

        // Test invalid URL
        fireEvent.change(urlInput, { target: { value: 'invalid-url' } });
        fireEvent.click(submitButton);

        // Check error message
        await waitFor(() => {
            expect(screen.getByText('Not a valid LinkedIn URL given. Try again')).toBeInTheDocument();
        });

        // Test valid URL
        const validUrl = 'https://www.linkedin.com/jobs/view/1234567890';
        fireEvent.change(urlInput, { target: { value: validUrl } });
        fireEvent.click(submitButton);

        // Check URL is sent and success message appears
        await waitFor(() => {
            expect(mockSendUpUrl).toHaveBeenCalledWith(validUrl);
            expect(screen.getByText('URL Submitted')).toBeInTheDocument();
        });
    });

    // Test video tutorial presence
    it('renders video tutorial', () => {
        render(<UploadJob sendUpUrl={() => {}} />);

        const initialSubmitButton = screen.getByText('Submit a LinkedIn Job');
        fireEvent.click(initialSubmitButton);

        const videoElement = screen.getByTestId('video-tutorial');
        expect(videoElement).toBeInTheDocument();
        expect(videoElement).toHaveAttribute('autoPlay');
        expect(videoElement).toHaveAttribute('loop');
        expect(videoElement).toHaveAttribute('controls');
    });
});