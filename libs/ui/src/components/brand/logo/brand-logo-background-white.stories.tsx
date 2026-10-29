import type { Meta, StoryObj } from '@storybook/react-vite';
import { BrandLogoBackgroundWhite } from './brand-logo-background-white';

const meta = {
  component: BrandLogoBackgroundWhite,
  title: 'BrandLogoBackgroundWhite',
} satisfies Meta<typeof BrandLogoBackgroundWhite>;
export default meta;

type Story = StoryObj<typeof BrandLogoBackgroundWhite>;

export const Primary = {
  args: {
    height: 96,
    width: 'auto',
  },
} satisfies Story;
