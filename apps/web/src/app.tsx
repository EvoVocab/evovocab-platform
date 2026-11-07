import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { RouterProvider } from '@tanstack/react-router';
import { GoogleOAuthProvider } from '@react-oauth/google';
import {
  TanStackRouterDevelopmentTools,
  ThemeProvider,
  Toaster,
  type FunctionComponent,
} from '@evovocab/ui';
import type { TanstackRouter } from './main';

const queryClient = new QueryClient();

type AppProps = { router: TanstackRouter };

const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID;

const App = ({ router }: AppProps): FunctionComponent => {
  return (
    <GoogleOAuthProvider clientId={clientId}>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider defaultTheme="dark" storageKey="evovocab-ui-theme">
          <RouterProvider router={router} />
          <TanStackRouterDevelopmentTools
            initialIsOpen={false}
            position="bottom-left"
            router={router}
          />
          <Toaster richColors position="top-center" />
        </ThemeProvider>
        <ReactQueryDevtools initialIsOpen={false} position="bottom" />
      </QueryClientProvider>
    </GoogleOAuthProvider>
  );
};

export default App;
