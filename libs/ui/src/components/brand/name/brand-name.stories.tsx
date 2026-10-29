import type { Meta, StoryObj } from '@storybook/react-vite';
import { BrandName } from './brand-name';

const meta = {
  component: BrandName,
  title: 'BrandName',
} satisfies Meta<typeof BrandName>;
export default meta;

type Story = StoryObj<typeof BrandName>;

export const Primary = {
  args: {
    height: 24,
    width: 'auto',
  },
} satisfies Story;
