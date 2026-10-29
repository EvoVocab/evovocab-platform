import type { Meta, StoryObj } from '@storybook/react-vite';
import { MainLayout } from './main-layout';

const meta = {
  component: MainLayout,
  title: 'MainLayout',
  parameters: {
    layout: 'fullscreen',
  },
} satisfies Meta<typeof MainLayout>;
export default meta;

type Story = StoryObj<typeof MainLayout>;

export const Primary = {
  args: {},
} satisfies Story;
