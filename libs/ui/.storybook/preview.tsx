import type { Decorator, Preview } from '@storybook/react';

// @ts-expect-error Storybook's CSS loader handles this side-effect import.
import '../src/styles/globals.css';
import { ThemeProvider } from '../src';

const preview: Preview = {
  parameters: {
    layout: 'centered',
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
  },
};

export const decorators: Array<Decorator> = [
  (Story) => (
    <ThemeProvider>
      <Story />
    </ThemeProvider>
  ),
];

export default preview;
