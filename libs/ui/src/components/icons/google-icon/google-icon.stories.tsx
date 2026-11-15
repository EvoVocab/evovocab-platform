import type { Meta, StoryObj } from '@storybook/react-vite';
import { GoogleIcon } from './google-icon';

const meta = {
  component: GoogleIcon,
  title: 'GoogleIcon',
} satisfies Meta<typeof GoogleIcon>;
export default meta;

type Story = StoryObj<typeof GoogleIcon>;

export const Primary = {
  args: {},
} satisfies Story;
