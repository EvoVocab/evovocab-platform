import { Button, ModeToggle } from '@evovocab/ui';
import { Link } from '@tanstack/react-router';

function About() {
  return (
    <div className="flex flex-1 flex-col items-center justify-center gap-8 py-10 sm:py-20">
      <div className="flex flex-col items-center gap-3 text-center">
        <h1
          className="text-3xl font-bold tracking-tight sm:text-4xl md:text-5xl"
          data-testid="about-page"
        >
          About Page
        </h1>
        <p
          className="text-muted-foreground text-lg font-medium sm:text-xl md:text-2xl"
          data-testid="app-title"
        >
          {import.meta.env.VITE_APP_TITLE}
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
            alert('Đây là About Page!');
          }}
        >
          Chạm vào đây
        </Button>

        <div className="flex w-full flex-row items-center justify-center gap-4 sm:w-auto">
          <Link
            className="text-primary hover:text-primary/80 text-sm font-medium underline-offset-4 hover:underline sm:text-base"
            data-testid="home-link"
            to="/"
          >
            Đi tới trang Home
          </Link>
          <div className="bg-border hidden h-6 w-px sm:block"></div> <ModeToggle />
        </div>
      </div>
    </div>
  );
}

export default About;
