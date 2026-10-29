import { render } from '@testing-library/react';

import App from './app';
import { router } from './main';

describe('App', () => {
  it('should render successfully', () => {
    const { baseElement } = render(<App router={router} />);
    expect(baseElement).toBeTruthy();
  });
});
