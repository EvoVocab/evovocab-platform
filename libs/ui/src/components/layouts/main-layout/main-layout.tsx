import { BrandIcon, BrandName } from '@/components/brand';
import { ModeToggle } from '@/components/customs';
import { Button } from '@/components/ui';
import { isProduction } from '@evovocab/shared-utils';
import { Link } from '@tanstack/react-router';
import { cn } from 'cn';

type MainLayoutProps = {
  children?: React.ReactNode;
};

const MainLayout = ({ children }: MainLayoutProps) => {
  return (
    <div className="bg-background container mx-auto grid w-full max-w-360 min-w-[320px] grid-rows-[56px_1fr] sm:grid-rows-[96px_1fr]">
      <header className="border-border sticky top-0 z-10 flex h-full w-full items-center justify-between border-b px-4 sm:px-8 lg:px-28">
        <div className="flex items-center gap-2 sm:gap-4">
          <Link className="flex items-center gap-2" to="/">
            <BrandIcon className="h-6 w-auto sm:h-7 lg:h-8" />
            <BrandName className="hidden w-auto sm:block sm:h-7 lg:h-8" />
          </Link>

          <Button
            variant="link"
            size="sm"
            className={cn('sm:h-10 sm:px-3 sm:text-sm lg:h-11 lg:px-8 lg:text-base', {
              hidden: isProduction(import.meta.env),
            })}
          >
            <Link to="/playground">Playground</Link>
          </Button>
        </div>

        <div className="flex items-center">
          <ModeToggle />
        </div>
      </header>
      <main className="grid grid-cols-4 gap-4 px-4 sm:grid-cols-6 sm:gap-8 sm:px-8 lg:grid-cols-12 lg:gap-8 lg:px-28">
        {children}
      </main>
    </div>
  );
};

export { MainLayout };
