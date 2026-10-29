import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { useTheme } from '../../../contexts/theme-provider';
import { ModeToggle } from './mode-toggle';

describe('ModeToggle Component', () => {
  const mockSetTheme = vi.fn();

  beforeEach(() => {
    vi.mocked(useTheme).mockReturnValue({
      theme: 'light',
      setTheme: mockSetTheme,
    });
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should render the toggle button correctly', () => {
    render(<ModeToggle />);

    const button = screen.getByTestId('dropdown-trigger');
    expect(button).toBeInTheDocument();
  });

  it('should display the list of options when clicking the toggle button', async () => {
    const user = userEvent.setup();
    render(<ModeToggle />);

    const button = screen.getByTestId('dropdown-trigger');
    await user.click(button);

    expect(await screen.findByText('Sáng')).toBeInTheDocument();
    expect(screen.getByText('Tối')).toBeInTheDocument();
    expect(screen.getByText('Hệ thống')).toBeInTheDocument();
  });

  it('should call setTheme("light") when clicking the "Sáng" option', async () => {
    const user = userEvent.setup();
    render(<ModeToggle />);

    await user.click(screen.getByTestId('dropdown-trigger'));

    const lightOption = await screen.findByText('Sáng');
    await user.click(lightOption);

    expect(mockSetTheme).toHaveBeenCalledTimes(1);
    expect(mockSetTheme).toHaveBeenCalledWith('light');
  });

  it('should call setTheme("dark") when clicking the "Tối" option', async () => {
    const user = userEvent.setup();
    render(<ModeToggle />);

    await user.click(screen.getByTestId('dropdown-trigger'));

    const darkOption = await screen.findByText('Tối');
    await user.click(darkOption);

    expect(mockSetTheme).toHaveBeenCalledWith('dark');
  });

  it('should call setTheme("system") when clicking the "Hệ thống" option', async () => {
    const user = userEvent.setup();
    render(<ModeToggle />);

    await user.click(screen.getByTestId('dropdown-trigger'));

    const systemOption = await screen.findByText('Hệ thống');
    await user.click(systemOption);

    expect(mockSetTheme).toHaveBeenCalledWith('system');
  });
});
