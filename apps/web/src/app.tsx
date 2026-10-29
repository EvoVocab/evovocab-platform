import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { RouterProvider } from '@tanstack/react-router';
import {
  TanStackRouterDevelopmentTools,
  ThemeProvider,
  type FunctionComponent,
} from '@evovocab/ui';
import type { TanstackRouter } from './main';

const queryClient = new QueryClient();

type AppProps = { router: TanstackRouter };

const App = ({ router }: AppProps): FunctionComponent => {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider defaultTheme="dark" storageKey="evovocab-ui-theme">
        <RouterProvider router={router} />
        <TanStackRouterDevelopmentTools
          initialIsOpen={false}
          position="bottom-left"
          router={router}
        />
      </ThemeProvider>
      <ReactQueryDevtools initialIsOpen={false} position="bottom" />
    </QueryClientProvider>
  );
};

export default App;
