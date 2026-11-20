import * as React from 'react';
import { Button, Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui';
import { InfoIcon } from 'lucide-react';

function FieldHintTooltip({ content }: { content: string }) {
  const [open, setOpen] = React.useState(false);

  return (
    <Tooltip open={open} onOpenChange={setOpen}>
      <TooltipTrigger
        render={
          <Button
            variant="ghost"
            size="icon"
            onClick={() => {
              setOpen(!open);
            }}
          >
            <InfoIcon className="size-4 cursor-help" aria-label="Gợi ý nhập liệu" />
          </Button>
        }
      />
      <TooltipContent className="max-w-xs text-xs" side="top">
        {content}
      </TooltipContent>
    </Tooltip>
  );
}

export { FieldHintTooltip };
