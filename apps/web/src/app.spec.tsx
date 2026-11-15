import { render } from '@testing-library/react';
import App from './app';
import { createRouter } from '@tanstack/react-router';
import { routeTree } from './routeTree.gen';

const router = createRouter({ routeTree, basepath: import.meta.env.VITE_APP_BASE_URL });

describe('App', () => {
  it('should render successfully', () => {
    const { baseElement } = render(<App router={router} />);
    expect(baseElement).toBeTruthy();
  });
});
