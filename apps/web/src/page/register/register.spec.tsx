import '@testing-library/jest-dom/vitest';

import { fireEvent, render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { RegisterForm } from '../../features/auth/components/register-form';

// Một hàm rỗng giả lập
const resolverMock = vi.fn();

// Mock cho thằng register schema, để test không phụ thuộc vào bản thân thằng zod schema
vi.mock('@hookform/resolvers/zod', () => ({
  zodResolver: () => resolverMock, // Ở trong file test này thì useFrom sẽ dùng resolverMock, thay vì registerSchema như trogn register form
}));

describe('Register Page', () => {
  beforeEach(() => {
    resolverMock.mockReset(); // xóa sạch ký ức về bài test trước (số lần bị gọi, ....)
    resolverMock.mockResolvedValue({ values: {}, errors: {} }); // set giá trị trả về mặc định với mockResolvedValue
  });

  it('should render the registration fields and submit button successfully', () => {
    render(<RegisterForm onSubmitForm={vi.fn()} />);

    expect(screen.getByText('Đăng ký', { selector: 'p' })).toBeInTheDocument();
    expect(screen.getByLabelText('Họ và tên')).toBeInTheDocument();
    expect(screen.getByLabelText('Email')).toBeInTheDocument();
    expect(screen.getByLabelText('Mật khẩu')).toHaveAttribute('type', 'password');
    expect(screen.getByLabelText('Xác thực mật khẩu')).toHaveAttribute('type', 'password');
    expect(screen.getByRole('button', { name: 'Đăng ký' })).toBeInTheDocument();
  });

  it('should show and hide the password when its visibility button is clicked', async () => {
    const user = userEvent.setup();
    render(<RegisterForm onSubmitForm={vi.fn()} />);

    await user.click(screen.getAllByRole('button', { name: 'Hiển thị mật khẩu' })[0]);
    expect(screen.getByLabelText('Mật khẩu')).toHaveAttribute('type', 'text');

    await user.click(screen.getByRole('button', { name: 'Ẩn mật khẩu' }));
    expect(screen.getByLabelText('Mật khẩu')).toHaveAttribute('type', 'password');
  });

  it('should disable fields and submit button while submitting', () => {
    render(<RegisterForm isSubmitting onSubmitForm={vi.fn()} />);

    expect(screen.getByLabelText('Họ và tên')).toBeDisabled();
    expect(screen.getByLabelText('Email')).toBeDisabled();
    expect(screen.getByLabelText('Mật khẩu')).toBeDisabled();
    expect(screen.getByLabelText('Xác thực mật khẩu')).toBeDisabled();
    expect(screen.getByRole('button', { name: 'Đăng ký' })).toBeDisabled();
  });

  describe('full name error messages', () => {
    it.each(['Họ và tên không được để trống', 'Họ và tên không hợp lệ'])(
      'displays "%s"',
      async (message) => {
        resolverMock.mockResolvedValue({
          values: {},
          errors: { fullName: { type: 'validation', message } },
        });
        render(<RegisterForm onSubmitForm={vi.fn()} />);

        fireEvent.blur(screen.getByLabelText('Họ và tên'));

        expect(await screen.findByText(message)).toBeInTheDocument();
      },
    );
  });

  describe('email error messages', () => {
    it.each(['Email không được để trống', 'Email không hợp lệ'])(
      'displays "%s"',
      async (message) => {
        resolverMock.mockResolvedValue({
          values: {},
          errors: { email: { type: 'validation', message } },
        });
        render(<RegisterForm onSubmitForm={vi.fn()} />);

        fireEvent.blur(screen.getByLabelText('Email'));

        expect(await screen.findByText(message)).toBeInTheDocument();
      },
    );
  });

  describe('password error messages', () => {
    it.each(['Mật khẩu không được để trống', 'Mật khẩu không hợp lệ'])(
      'displays "%s"',
      async (message) => {
        resolverMock.mockResolvedValue({
          values: {},
          errors: { password: { type: 'validation', message } },
        });
        render(<RegisterForm onSubmitForm={vi.fn()} />);

        fireEvent.blur(screen.getByLabelText('Mật khẩu'));

        expect(await screen.findByText(message)).toBeInTheDocument();
      },
    );
  });

  describe('confirm password error messages', () => {
    it('displays the password mismatch message', async () => {
      const message = 'Mật khẩu xác nhận không khớp';
      resolverMock.mockResolvedValue({
        values: {},
        errors: { confirmPassword: { type: 'validation', message } },
      });
      render(<RegisterForm onSubmitForm={vi.fn()} />);

      fireEvent.blur(screen.getByLabelText('Xác thực mật khẩu'));

      expect(await screen.findByText(message)).toBeInTheDocument();
    });
  });
});
