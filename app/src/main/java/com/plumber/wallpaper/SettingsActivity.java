package com.plumber.wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Простой layout без XML
        Button button = new Button(this);
        button.setText(R.string.set_wallpaper);
        button.setTextSize(18);
        button.setPadding(40, 40, 40, 40);
        setContentView(button);

        button.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(this, PlumberWallpaperService.class)
                );
                startActivity(intent);
            } catch (Exception e) {
                // На некоторых прошивках другой способ
                try {
                    Intent intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                    startActivity(intent);
                    Toast.makeText(this, "Выберите «Сантехники» в списке", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(this, "Не удалось открыть выбор обоев", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
