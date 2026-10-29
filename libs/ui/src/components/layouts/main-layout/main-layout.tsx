import { BrandIcon, BrandName } from '../../brand';

type MainLayoutProps = {
  children?: React.ReactNode;
};

const MainLayout = ({ children }: MainLayoutProps) => {
  return (
    <div className="bg-background flex min-h-screen flex-col">
      <header className="border-border bg-background/95 sticky top-0 z-10 flex w-full items-center border-b p-4 backdrop-blur sm:px-6 lg:px-8">
        <div className="mx-auto flex w-full max-w-7xl items-center justify-center md:justify-start">
          <div className="flex flex-row items-center gap-2">
            <BrandIcon className="h-6 w-auto sm:h-7 md:h-8" />
            <BrandName className="h-6 w-auto sm:h-7 md:h-8" />
          </div>
        </div>
      </header>

      <main className="mx-auto flex w-full max-w-7xl flex-1 flex-col px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
        {children}
      </main>
    </div>
  );
};

export { MainLayout };
