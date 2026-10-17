package com.plumber.wallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

/**
 * Live Wallpaper Service
 * Циклически показывает кадры анимации сантехников.
 */
public class PlumberWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new PlumberEngine();
    }

    private class PlumberEngine extends Engine {
        private final Handler handler = new Handler();
        private boolean visible = true;
        private int currentFrame = 0;
        private Bitmap[] frames;
        private final Paint paint = new Paint();

        // Скорость смены кадров (мс)
        private static final int FRAME_DELAY = 1200;

        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            loadFrames();
        }

        private void loadFrames() {
            // Загружаем кадры из drawable
            // Добавь свои изображения: frame1, frame2, frame3, frame4
            frames = new Bitmap[4];
            try {
                frames[0] = BitmapFactory.decodeResource(getResources(), R.drawable.frame1);
                frames[1] = BitmapFactory.decodeResource(getResources(), R.drawable.frame2);
                frames[2] = BitmapFactory.decodeResource(getResources(), R.drawable.frame3);
                frames[3] = BitmapFactory.decodeResource(getResources(), R.drawable.frame4);
            } catch (Exception e) {
                // Если кадры не найдены — будет чёрный экран с текстом
                frames = null;
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                draw();
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            draw();
        }

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    // Фон
                    canvas.drawColor(Color.parseColor("#1A252F"));

                    if (frames != null && frames[currentFrame] != null) {
                        // Масштабируем кадр под размер экрана
                        Bitmap frame = frames[currentFrame];
                        float scale = Math.max(
                                (float) canvas.getWidth() / frame.getWidth(),
                                (float) canvas.getHeight() / frame.getHeight()
                        );
                        int scaledW = (int) (frame.getWidth() * scale);
                        int scaledH = (int) (frame.getHeight() * scale);
                        int left = (canvas.getWidth() - scaledW) / 2;
                        int top = (canvas.getHeight() - scaledH) / 2;

                        canvas.drawBitmap(
                                Bitmap.createScaledBitmap(frame, scaledW, scaledH, true),
                                left, top, paint
                        );
                    } else {
                        // Заглушка, если картинки не загружены
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(48);
                        paint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText("Plumber Live Wallpaper", canvas.getWidth() / 2f, canvas.getHeight() / 2f, paint);
                        canvas.drawText("Добавьте кадры frame1-4 в drawable", canvas.getWidth() / 2f, canvas.getHeight() / 2f + 60, paint);
                    }

                    // Следующий кадр
                    currentFrame = (currentFrame + 1) % 4;
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, FRAME_DELAY);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawRunner);
            if (frames != null) {
                for (Bitmap b : frames) {
                    if (b != null && !b.isRecycled()) {
                        b.recycle();
                    }
                }
            }
        }
    }
}
