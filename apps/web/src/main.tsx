import '@evovocab/ui/globals.css';
import { createRouter } from '@tanstack/react-router';
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './app';
import { routeTree } from './routeTree.gen';

export const router = createRouter({ routeTree, basepath: import.meta.env.VITE_APP_BASE_URL });

export type TanstackRouter = typeof router;

declare module '@tanstack/react-router' {
  interface Register {
    router: TanstackRouter;
  }
}

const rootElement = document.querySelector('#root');
if (rootElement !== null && !rootElement.innerHTML) {
  const root = ReactDOM.createRoot(rootElement);
  root.render(
    <React.StrictMode>
      <React.Suspense fallback="loading">
        <App router={router} />
      </React.Suspense>
    </React.StrictMode>,
  );
}
