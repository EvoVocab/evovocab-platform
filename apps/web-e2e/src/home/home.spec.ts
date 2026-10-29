import { test, expect } from '@playwright/test';

test.describe('Home Page E2E', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('should render home page elements successfully', async ({ page }) => {
    const heading = page.getByTestId('home-page');
    await expect(heading).toBeVisible();
    await expect(heading).toHaveText('Home Page');

    const actionButton = page.getByRole('button', { name: 'Chạm vào đây' });
    await expect(actionButton).toBeVisible();

    const aboutLink = page.getByTestId('about-link');
    await expect(aboutLink).toBeVisible();
    await expect(aboutLink).toHaveText('Đi tới trang About');
  });

  test('should trigger alert when button is clicked', async ({ page }) => {
    page.once('dialog', async (dialog) => {
      expect(dialog.message()).toBe('Đây là Home Page!');
      await dialog.dismiss();
    });

    await page.getByRole('button', { name: 'Chạm vào đây' }).click();
  });

  test('should navigate to about page when link is clicked', async ({ page }) => {
    await page.getByTestId('about-link').click();

    await expect(page).toHaveURL(/\/about$/);
  });
});
