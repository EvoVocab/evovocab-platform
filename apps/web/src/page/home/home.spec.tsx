import { describe, it } from 'vitest';
import { type AnyRoute, createRoute } from '@tanstack/react-router';
import { screen } from '@testing-library/dom';

import { renderWithRouter, rootRoute } from '../../test/router-utilities';
import Home from './home';
import App from '../../app';
import { render } from '@testing-library/react';

describe('Home Page', () => {
  it('should render successfully', async () => {
    const testRoute = createRoute({
      getParentRoute: () => rootRoute as AnyRoute,
      path: '/',
      component: Home,
    });

    const { router } = renderWithRouter(<div />, {
      routes: [testRoute],
      initialLocation: '/',
    });

    render(<App router={router} />);

    expect(await screen.findByTestId('home-page')).toBeInTheDocument();
  });
});
