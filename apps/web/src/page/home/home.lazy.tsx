import React from 'react';
import { Link } from '@tanstack/react-router';
import { BrandLogoBackgroundWhite, Button } from '@evovocab/ui';
import { useActuatorHealth } from '@/features/actuator/hooks/use-actuator-health';

function Home() {
  const { data } = useActuatorHealth();

  React.useEffect(() => {
    if (data) {
      console.log('Actuator Health:', data);
    }
  }, [data]);

  return (
    <div className="col-span-4 flex flex-col items-center gap-8 py-8 sm:col-start-2 sm:py-8 lg:col-span-6 lg:col-start-4 lg:pt-24 lg:pb-12">
      <BrandLogoBackgroundWhite className="h-24 w-auto rounded-full shadow-sm sm:h-32 md:h-40" />

      <div className="flex flex-col items-center gap-3 text-center">
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

      <div className="flex w-full flex-col items-center gap-4 sm:flex-row sm:justify-center">
        <Button className="w-full sm:w-auto" size="lg" variant="secondary">
          <Link to="/test">Trang Thử nghiệm</Link>
        </Button>
        <Button
          className="w-full sm:w-auto"
          size="lg"
          onClick={() => {
            alert('Đây là Home Page!');
          }}
        >
          Chạm vào đây
        </Button>
        <Button className="w-full sm:w-auto" size="lg" variant="link">
          <Link to="/about">About Page</Link>
        </Button>
      </div>
    </div>
  );
}

export default Home;
