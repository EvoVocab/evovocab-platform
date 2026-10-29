import { Button, ModeToggle } from '@evovocab/ui';
import { BrandLogoBackgroundWhite } from '@evovocab/ui/components/brand';
import { HomeIcon } from '@evovocab/ui/components/icons';
import { Link } from '@tanstack/react-router';

function Home() {
  return (
    <div className="flex flex-1 flex-col items-center justify-center gap-8 py-10 sm:py-20">
      <BrandLogoBackgroundWhite className="h-24 w-auto rounded-full shadow-sm sm:h-32 md:h-40" />

      <div className="flex flex-col items-center gap-3 text-center">
        <HomeIcon />
        <h1
          className="text-3xl font-bold tracking-tight sm:text-4xl md:text-5xl"
          data-testid="home-page"
        >
          Home Page
        </h1>
        <title className="text-muted-foreground text-lg font-medium sm:text-xl md:text-2xl">
          {import.meta.env.VITE_APP_TITLE}
        </title>
        <p className="text-muted-foreground max-w-lg text-sm sm:text-base">
          Chào mừng bạn đến với ứng dụng. Giao diện này đã được tự động điều chỉnh để hiển thị hoàn
          hảo trên mọi thiết bị.
        </p>
      </div>

      {/* Cụm chức năng (Actions): 
          - Mobile: Xếp dọc (flex-col), w-full
          - Tablet/Desktop: Xếp ngang (sm:flex-row), w-auto 
      */}
      <div className="flex w-full flex-col items-center justify-center gap-4 px-4 sm:w-auto sm:flex-row sm:gap-6 sm:px-0">
        <Button
          className="w-full sm:w-auto"
          size="lg"
          onClick={() => {
            alert('Đây là Home Page!');
          }}
        >
          Chạm vào đây
        </Button>

        <div className="flex w-full flex-row items-center justify-center gap-4 sm:w-auto">
          <Link
            className="text-primary hover:text-primary/80 text-sm font-medium underline-offset-4 hover:underline sm:text-base"
            data-testid="about-link"
            to="/about"
          >
            Đi tới trang About
          </Link>
          <div className="bg-border hidden h-6 w-px sm:block"></div> <ModeToggle />
        </div>
      </div>
    </div>
  );
}

export default Home;
