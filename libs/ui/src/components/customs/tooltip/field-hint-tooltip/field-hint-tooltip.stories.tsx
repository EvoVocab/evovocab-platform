import type { Meta, StoryObj } from '@storybook/react-vite';
import type { ComponentProps } from 'react';
import { FieldHintTooltip } from './field-hint-tooltip';
import { TooltipProvider } from '@/components/ui';

type TooltipProviderProps = Omit<ComponentProps<typeof TooltipProvider>, 'children'>;

type StoryProps = ComponentProps<typeof FieldHintTooltip> & {
  provider?: TooltipProviderProps;
};

const meta = {
  component: FieldHintTooltip,
  title: 'FieldHintTooltip',
  argTypes: {
    provider: {
      control: 'object',
      description: 'Toàn bộ props truyền vào TooltipProvider',
      table: {
        category: 'TooltipProvider',
      },
    },
  },
  args: {
    provider: {
      delay: 0,
    },
  },
  decorators: [
    (Story, context) => (
      <TooltipProvider {...context.args.provider}>
        <Story />
      </TooltipProvider>
    ),
  ],
} satisfies Meta<StoryProps>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    content: 'Tooltip default',
    provider: {
      delay: 0,
      closeDelay: 0,
      timeout: 400,
    },
  },
};

export const InstantTooltip: Story = {
  args: {
    content: 'Default',
    provider: {
      delay: 0,
      closeDelay: 0,
      timeout: 400,
    },
  },
};
