import { useCanGoBack, useRouter } from '@tanstack/react-router';
import { ArrowLeft } from 'lucide-react';
import { Button } from '@evovocab/ui';

function GoBack() {
  const router = useRouter();
  const canGoBack = useCanGoBack();

  if (!canGoBack) return null;

  return (
    <Button
      variant="link"
      onClick={() => {
        router.history.back();
      }}
    >
      <ArrowLeft data-icon="inline-start" />
      Quay lại
    </Button>
  );
}

export { GoBack };
