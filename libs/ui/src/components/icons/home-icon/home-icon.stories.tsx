import type { Meta, StoryObj } from '@storybook/react-vite';
import { HomeIcon } from 'lucide-react';

const meta = {
  component: HomeIcon,
  title: 'HomeIcon',
} satisfies Meta<typeof HomeIcon>;
export default meta;

type Story = StoryObj<typeof HomeIcon>;

export const Primary = {
  args: {},
} satisfies Story;
