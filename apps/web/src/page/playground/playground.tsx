import * as React from 'react';
import { toast } from 'sonner';
import {
  Button,
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  ModeToggle,
  GoogleIcon,
} from '@evovocab/ui';
import { GoBack } from '@/components/go-back';
import { useOAuthStore } from '@evovocab/store';
import { useGoogleLogin } from '@react-oauth/google';

export function Playground() {
  const oauthState = useOAuthStore((state) => state.oauthState);
  const generateOAuthState = useOAuthStore((state) => state.generateOAuthState);
  const resetOAuthState = useOAuthStore((state) => state.resetOAuthState);

  const simulateAsyncOperation = (shouldSucceed = true) => {
    return new Promise<{ name: string }>((resolve, reject) => {
      setTimeout(() => {
        if (shouldSucceed) {
          resolve({ name: 'Sự kiện EvoVocab' });
        } else {
          reject(new Error('Tạo sự kiện thất bại'));
        }
      }, 1500);
    });
  };

  const handleTestPromise = (shouldSucceed: boolean) => {
    toast.promise(simulateAsyncOperation(shouldSucceed), {
      loading: 'Đang xử lý dữ liệu...',
      success: (data) => `${data.name} đã được tạo thành công!`,
      error: (error) => (error as Error).message || 'Đã có lỗi xảy ra!',
    });
  };

  const oauth = useGoogleLogin({
    flow: 'auth-code',

    // eslint-disable-next-line camelcase
    redirect_uri: `${window.location.origin}/evovocab/playground`,
    // eslint-disable-next-line camelcase
    ux_mode: 'redirect',
    scope: 'openid email profile',
    state: oauthState ?? undefined,
  });

  const handleGoogleLoginClick = () => {
    if (!oauthState) {
      generateOAuthState();
      oauth();
    } else {
      oauth();
    }
  };

  React.useEffect(() => {
    const cleanUrl = () => {
      window.history.replaceState({}, document.title, window.location.pathname);
    };

    const parameters = new URLSearchParams(window.location.search);
    const code = parameters.get('code');
    const returnedState = parameters.get('state');
    const error = parameters.get('error');

    if (error) {
      toast.error('Đăng nhập không thành công. Vui lòng thử lại.');
      cleanUrl();
      return;
    }

    if (code) {
      if (returnedState !== oauthState) {
        toast.error('Đăng nhập không thành công. Vui lòng thử lại.');
      } else {
        toast.success('Đăng nhập thành công!');
      }
      cleanUrl();
    }
    resetOAuthState();
  }, [oauthState, resetOAuthState]);

  return (
    <div className="container mx-auto space-y-8 p-6" data-testid="playground-page">
      <GoBack />
      <div className="flex flex-col gap-2 border-b pb-4 md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">EvoVocab Playground</h1>
          <p className="text-muted-foreground text-sm">
            Khu vực thử nghiệm UI components, state management và thông báo hệ thống.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <span className="text-muted-foreground text-xs">Chuyển theme:</span>
          <ModeToggle />
        </div>
      </div>

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle className="text-xl">Thử nghiệm Toast (Sonner)</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-2">
              <h3 className="text-muted-foreground text-sm font-medium">Thông báo cơ bản</h3>
              <div className="flex flex-wrap gap-2">
                <Button
                  data-testid="toast-default-btn"
                  variant="outline"
                  onClick={() => toast('Thông báo mặc định')}
                >
                  Mặc định
                </Button>
                <Button
                  className="border-green-500 text-green-600 hover:bg-green-50"
                  data-testid="toast-success-btn"
                  variant="outline"
                  onClick={() => toast.success('Sự kiện đã được tạo thành công')}
                >
                  Thành công
                </Button>
                <Button
                  className="border-blue-500 text-blue-600 hover:bg-blue-50"
                  data-testid="toast-info-btn"
                  variant="outline"
                  onClick={() => toast.info('Tạo sự kiện vào lúc 8 giờ sáng ngày mai')}
                >
                  Thông tin
                </Button>
                <Button
                  className="border-yellow-500 text-yellow-600 hover:bg-yellow-50"
                  data-testid="toast-warning-btn"
                  variant="outline"
                  onClick={() => toast.warning('Thời gian bắt đầu không thể sớm hơn 8 giờ')}
                >
                  Cảnh báo
                </Button>
                <Button
                  data-testid="toast-error-btn"
                  variant="destructive"
                  onClick={() => toast.error('Sự kiện chưa được tạo')}
                >
                  Lỗi
                </Button>
              </div>
            </div>

            <div className="space-y-2 border-t pt-2">
              <h3 className="text-muted-foreground text-sm font-medium">Thông báo nâng cao</h3>
              <div className="flex flex-wrap gap-2">
                <Button
                  variant="secondary"
                  onClick={() =>
                    toast.message('Cập nhật mới', {
                      description: 'Phiên bản v2.4.0 đã sẵn sàng để sử dụng.',
                    })
                  }
                >
                  Có mô tả (Description)
                </Button>
                <Button
                  variant="secondary"
                  onClick={() =>
                    toast('Đã xoá mục từ vựng', {
                      action: {
                        label: 'Hoàn tất',
                        onClick: () => toast.success('Đã khôi phục mục!'),
                      },
                    })
                  }
                >
                  Có nút Action
                </Button>
                <Button
                  variant="secondary"
                  onClick={() => {
                    handleTestPromise(true);
                  }}
                >
                  Promise (Success)
                </Button>
                <Button
                  variant="secondary"
                  onClick={() => {
                    handleTestPromise(false);
                  }}
                >
                  Promise (Error)
                </Button>
                <Button variant="outline" onClick={() => toast.dismiss()}>
                  Tắt tất cả Toast
                </Button>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle className="text-xl">Google OAuth</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2 text-sm">
            <Button variant="outline" onClick={handleGoogleLoginClick}>
              <GoogleIcon data-icon="inline-start" />
              Đang nhập với Google
            </Button>
          </CardContent>
        </Card>

        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle className="text-xl">Thông tin môi trường (Debug info)</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2 text-sm">
            <div className="flex justify-between border-b pb-1">
              <span className="text-muted-foreground">NODE_ENV:</span>
              <span className="font-mono">{process.env.NODE_ENV || 'development'}</span>
            </div>
            <div className="flex justify-between border-b pb-1">
              <span className="text-muted-foreground">User Agent:</span>
              <span className="max-w-50 font-mono text-xs">
                {typeof window !== 'undefined' ? window.navigator.userAgent : 'SSR'}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="text-muted-foreground">MatchMedia Support:</span>
              <span className="font-mono">
                {typeof window !== 'undefined' && 'matchMedia' in window ? 'Yes' : 'No'}
              </span>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

export default Playground;
