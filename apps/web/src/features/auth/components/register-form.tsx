import { useState } from 'react';
import { type UseFormReturn, Controller, useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Eye, EyeOff, KeyRound, Loader2, Mail, User } from 'lucide-react';

import {
  BrandLogoBackgroundWhite,
  Button,
  Field,
  FieldError,
  FieldGroup,
  FieldLabel,
  FieldHintTooltip,
  InputGroup,
  InputGroupInput,
  InputGroupAddon,
  // FieldSeparator,
  // GoogleIcon,
} from '@evovocab/ui';

import { normalizeFullName } from '@evovocab/shared-utils';

import { registerSchema, type RegisterFormValues } from '../schemas/register.schema';
// import { Link } from '@tanstack/react-router';
import { PasswordRequirements } from './password-requirements';

interface RegisterFormProps {
  onSubmitForm: (
    form: UseFormReturn<RegisterFormValues>,
    payload: Omit<RegisterFormValues, 'confirmPassword'>,
  ) => Promise<void> | void;
  onGoogleLogin?: () => void;
  isSubmitting?: boolean;
}

export function RegisterForm({
  onSubmitForm,
  // onGoogleLogin,
  isSubmitting = false,
}: RegisterFormProps) {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const form = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      fullName: '',
      email: '',
      password: '',
      confirmPassword: '',
    },
    mode: 'onTouched',
  });

  const handleSubmit = async (values: RegisterFormValues) => {
    await onSubmitForm(form, {
      fullName: normalizeFullName(values.fullName),
      email: values.email.trim().toLowerCase(),
      password: values.password,
    });
  };

  const pending = isSubmitting || form.formState.isSubmitting;

  return (
    <>
      <BrandLogoBackgroundWhite className="h-24 w-auto rounded-full shadow-sm sm:h-32 md:h-40" />
      <p className="text-foreground text-3xl font-bold tracking-tight">Đăng ký</p>

      <form noValidate className="w-full" onSubmit={form.handleSubmit(handleSubmit)}>
        <FieldGroup className="gap-6">
          {/* Họ và tên */}
          <Controller
            control={form.control}
            name="fullName"
            render={({ field, fieldState }) => (
              <Field data-invalid={fieldState.invalid}>
                <FieldLabel className="text-xs md:text-sm" htmlFor={field.name}>
                  Họ và tên
                </FieldLabel>
                <InputGroup className="h-10">
                  <InputGroupAddon>
                    <User className="pointer-events-none size-4" />
                  </InputGroupAddon>
                  <InputGroupAddon align="inline-end">
                    <FieldHintTooltip content="Từ 2–100 ký tự, chỉ bao gồm chữ cái và khoảng trắng." />
                  </InputGroupAddon>
                  <InputGroupInput
                    {...field}
                    aria-invalid={fieldState.invalid}
                    autoComplete="name"
                    className="h-full text-sm font-medium md:text-base"
                    disabled={pending}
                    id={field.name}
                    placeholder="Nhập họ và tên của bạn"
                  />
                </InputGroup>
                {fieldState.invalid && (
                  <FieldError className="text-xs" errors={[fieldState.error]} />
                )}
              </Field>
            )}
          />

          {/* Email */}
          <Controller
            control={form.control}
            name="email"
            render={({ field, fieldState }) => (
              <Field data-invalid={fieldState.invalid}>
                <FieldLabel className="text-xs md:text-sm" htmlFor={field.name}>
                  Email
                </FieldLabel>
                <InputGroup className="h-10">
                  <InputGroupAddon>
                    <Mail className="pointer-events-none size-4" />
                  </InputGroupAddon>
                  <InputGroupAddon align="inline-end">
                    <FieldHintTooltip content="Dùng email đang hoạt động để nhận mã xác thực tài khoản." />
                  </InputGroupAddon>
                  <InputGroupInput
                    {...field}
                    aria-invalid={fieldState.invalid}
                    autoComplete="email"
                    className="h-full text-sm font-medium md:text-base"
                    disabled={pending}
                    id={field.name}
                    placeholder="Nhập email của bạn"
                  />
                </InputGroup>
                {fieldState.invalid && (
                  <FieldError className="text-xs md:text-sm" errors={[fieldState.error]} />
                )}
              </Field>
            )}
          />

          {/* Mật khẩu */}
          <Controller
            control={form.control}
            name="password"
            render={({ field, fieldState }) => (
              <Field data-invalid={fieldState.invalid}>
                <FieldLabel className="text-xs md:text-sm" htmlFor={field.name}>
                  Mật khẩu
                </FieldLabel>
                <InputGroup className="h-10">
                  <InputGroupAddon>
                    <KeyRound className="pointer-events-none size-4" />
                  </InputGroupAddon>
                  <InputGroupAddon align="inline-end">
                    <Button
                      aria-label={showPassword ? 'Ẩn mật khẩu' : 'Hiển thị mật khẩu'}
                      size="icon"
                      variant="ghost"
                      onClick={() => {
                        setShowPassword((previous) => !previous);
                      }}
                    >
                      {showPassword ? <EyeOff className="size-4" /> : <Eye className="size-4" />}
                    </Button>
                  </InputGroupAddon>
                  <InputGroupInput
                    {...field}
                    aria-invalid={fieldState.invalid}
                    autoComplete="new-password"
                    className="h-full text-sm font-medium md:text-base"
                    disabled={pending}
                    id={field.name}
                    placeholder="Nhập mật khẩu của bạn"
                    type={showPassword ? 'text' : 'password'}
                  />
                </InputGroup>
                <PasswordRequirements password={field.value} />
                {fieldState.invalid && (
                  <FieldError className="text-xs" errors={[fieldState.error]} />
                )}
              </Field>
            )}
          />

          {/* Xác thực mật khẩu */}
          <Controller
            control={form.control}
            name="confirmPassword"
            render={({ field, fieldState }) => (
              <Field data-invalid={fieldState.invalid}>
                <FieldLabel className="text-xs md:text-sm" htmlFor={field.name}>
                  Xác thực mật khẩu
                </FieldLabel>
                <InputGroup className="h-10">
                  <InputGroupAddon>
                    <KeyRound className="pointer-events-none size-4" />
                  </InputGroupAddon>
                  <InputGroupAddon align="inline-end">
                    <Button
                      aria-label={showConfirmPassword ? 'Ẩn mật khẩu' : 'Hiển thị mật khẩu'}
                      size="icon"
                      variant="ghost"
                      onClick={() => {
                        setShowConfirmPassword((previous) => !previous);
                      }}
                    >
                      {showConfirmPassword ? (
                        <EyeOff className="size-4" />
                      ) : (
                        <Eye className="size-4" />
                      )}
                    </Button>
                  </InputGroupAddon>
                  <InputGroupInput
                    {...field}
                    aria-invalid={fieldState.invalid}
                    autoComplete="new-password"
                    className="h-full text-sm font-medium md:text-base"
                    disabled={pending}
                    id={field.name}
                    placeholder="Nhập lại mật khẩu của bạn"
                    type={showConfirmPassword ? 'text' : 'password'}
                  />
                </InputGroup>
                {fieldState.invalid && (
                  <FieldError className="text-xs" errors={[fieldState.error]} />
                )}
              </Field>
            )}
          />

          <Button
            className="h-9 w-full text-sm font-medium sm:h-11 lg:h-12 lg:text-base"
            disabled={pending}
            type="submit"
          >
            {pending && <Loader2 className="mr-2 size-4 animate-spin" />}
            Đăng ký
          </Button>
        </FieldGroup>
      </form>

      {/* Tính năng đang phát triển */}
      {/* <p className="text-muted-foreground text-center text-sm">
        Đã có tài khoản?
        <Button variant="link">
          <Link to=".">Đăng nhập</Link>
        </Button>
      </p> */}

      {/* Tính năng đang phát triển */}
      {/* <FieldSeparator className="w-full">Hoặc</FieldSeparator>

      <Button
        className="h-9 w-full text-sm font-medium sm:h-11 lg:h-12 lg:text-base"
        variant="outline"
        onClick={onGoogleLogin}
      >
        <GoogleIcon data-icon="inline-start" />
        Tiếp tục với Google
      </Button> */}
    </>
  );
}
