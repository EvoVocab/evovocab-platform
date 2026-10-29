import { describe, it } from 'vitest';
import { type AnyRoute, createRoute } from '@tanstack/react-router';
import { screen } from '@testing-library/dom';
import { render } from '@testing-library/react';

import { renderWithRouter, rootRoute } from '../../test/router-utilities';
import About from './about';
import App from '../../app';

describe('About Page', () => {
  it('should render successfully', async () => {
    const testRoute = createRoute({
      getParentRoute: () => rootRoute as AnyRoute,
      path: '/about',
      component: About,
    });

    const { router } = renderWithRouter(<div />, {
      routes: [testRoute],
      initialLocation: '/about',
    });

    render(<App router={router} />);

    expect(await screen.findByTestId('about-page')).toBeInTheDocument();
    expect(await screen.findByTestId('app-title')).toBeInTheDocument();
    expect(await screen.findByTestId('app-title')).toHaveTextContent('EvoVocab');
  });
});
