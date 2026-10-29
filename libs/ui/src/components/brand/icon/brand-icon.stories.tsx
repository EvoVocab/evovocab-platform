import type { Meta, StoryObj } from '@storybook/react-vite';
import { BrandIcon } from './brand-icon';

const meta = {
  component: BrandIcon,
  title: 'BrandIcon',
} satisfies Meta<typeof BrandIcon>;
export default meta;

type Story = StoryObj<typeof BrandIcon>;

export const Primary = {
  args: {
    height: 24,
    width: 'auto',
  },
} satisfies Story;
