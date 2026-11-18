import type { Meta, StoryObj } from '@storybook/react-vite';
import { ModeToggle } from './mode-toggle';

const meta = {
  component: ModeToggle,
  title: 'ModeToggle',
} satisfies Meta<typeof ModeToggle>;
export default meta;

type Story = StoryObj<typeof ModeToggle>;

export const Primary = {
  args: {},
} satisfies Story;
