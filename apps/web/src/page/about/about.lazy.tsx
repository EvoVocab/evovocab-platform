import { Button } from '@evovocab/ui';
import { Link } from '@tanstack/react-router';

function About() {
  return (
    <div className="col-span-4 flex flex-col items-center gap-8 py-8 sm:col-start-2 sm:py-8 md:pt-24 md:pb-12 lg:col-span-6 lg:col-start-4">
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

      <div className="flex w-full flex-col items-center gap-4 sm:flex-row sm:justify-center">
        <Button
          className="w-full sm:w-auto"
          size="lg"
          onClick={() => {
            alert('Đây là About Page!');
          }}
        >
          Chạm vào đây
        </Button>
        <Button className="w-full sm:w-auto" size="lg" variant="link">
          <Link to="/">Home Page</Link>
        </Button>
      </div>
    </div>
  );
}

export default About;
