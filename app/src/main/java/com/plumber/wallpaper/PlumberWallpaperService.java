package com.plumber.wallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class PlumberWallpaperService extends WallpaperService {

    private static final int FRAME_DELAY = 1200; // миллисекунды

    private static final int[] FRAMES = {
            R.drawable.frame1,
            R.drawable.frame2,
            R.drawable.frame3,
            R.drawable.frame4
    };

    @Override
    public Engine onCreateEngine() {
        return new PlumberEngine();
    }

    private class PlumberEngine extends Engine {

        private final Handler handler = new Handler(Looper.getMainLooper());
        private final Runnable drawRunnable = this::draw;
        private final Bitmap[] bitmaps = new Bitmap[FRAMES.length];
        private int frameIndex = 0;
        private boolean visible = false;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 2; // экономия памяти, для обоев хватает
            for (int i = 0; i < FRAMES.length; i++) {
                bitmaps[i] = BitmapFactory.decodeResource(getResources(), FRAMES[i], opts);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                draw();
            } else {
                handler.removeCallbacks(drawRunnable);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            visible = false;
            handler.removeCallbacks(drawRunnable);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawRunnable);
            for (Bitmap b : bitmaps) {
                if (b != null) b.recycle();
            }
        }

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(0xFF000000);
                    Bitmap bmp = bitmaps[frameIndex];
                    if (bmp != null) {
                        canvas.drawBitmap(bmp, null,
                                fitRect(bmp, canvas.getWidth(), canvas.getHeight()), null);
                    }
                }
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas);
            }

            frameIndex = (frameIndex + 1) % FRAMES.length;

            handler.removeCallbacks(drawRunnable);
            if (visible) {
                handler.postDelayed(drawRunnable, FRAME_DELAY);
            }
        }

        // Заполняет весь экран с сохранением пропорций, лишнее обрезается по центру
        private Rect fitRect(Bitmap bmp, int w, int h) {
            float scale = Math.max((float) w / bmp.getWidth(), (float) h / bmp.getHeight());
            int dw = Math.round(bmp.getWidth() * scale);
            int dh = Math.round(bmp.getHeight() * scale);
            int left = (w - dw) / 2;
            int top = (h - dh) / 2;
            return new Rect(left, top, left + dw, top + dh);
        }
    }
}
