# 🛠️ Plumber Live Wallpaper

Милые мультяшные сантехники лазают по трубам, чинят протечки, а когда всё спокойно — протирают иконки и отдыхают.

Готовый Android Live Wallpaper проект. Можно сразу клонировать, открыть в Android Studio и собрать APK.

![Preview](app/src/main/res/drawable/wallpaper_preview.jpg)

## ✨ Возможности

- Классический **Android Live Wallpaper**
- Циклическая анимация из 4 кадров
- Кнопка «Установить обои» внутри приложения
- Минимальный Android 7.0 (API 24)
- GitHub Actions автоматически собирает APK при каждом push

## 🚀 Быстрый старт

### Вариант 1 — Android Studio (рекомендуется)

1. Клонируй репозиторий:

```bash
git clone https://github.com/glushko2010-dot/trubasik-live-wallpape.git
cd trubasik-live-wallpape
```

2. Открой папку в **Android Studio**
3. Дождись синхронизации Gradle
4. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. APK появится здесь:

```
app/build/outputs/apk/debug/app-debug.apk
```

### Вариант 2 — Командная строка

```bash
./gradlew assembleDebug
```

### Вариант 3 — Скачать готовый APK из Actions

1. Зайди во вкладку **Actions** на GitHub
2. Выбери последний успешный workflow
3. Скачай артефакт `plumber-live-wallpaper-debug`

## 🎨 Как заменить кадры анимации

Положи свои изображения в папку:

```
app/src/main/res/drawable/
```

| Файл | Что должно быть на картинке |
|-------------------------|----------------------------------|
| `frame1.jpg` | Сантехники чинят трубы |
| `frame2.jpg` | Сантехники бегут / лазают |
| `frame3.jpg` | Сантехники сидят и отдыхают |
| `frame4.jpg` | Сантехники протирают поверхности |
| `wallpaper_preview.jpg` | Превью для списка обоев |

После замены просто пересобери проект.

## ⚙️ Настройка скорости анимации

Открой файл:

```
app/src/main/java/com/plumber/wallpaper/PlumberWallpaperService.java
```

Найди строку:

```java
private static final int FRAME_DELAY = 1200; // миллисекунды
```

Измени значение:

- `800` — быстрее
- `1500` — медленнее

## 📂 Структура проекта

```
trubasik-live-wallpape/
├── .github/workflows/build.yml ← автоматическая сборка APK
├── app/
│   ├── src/main/
│   │   ├── java/.../PlumberWallpaperService.java
│   │   ├── java/.../SettingsActivity.java
│   │   ├── res/drawable/ ← кадры анимации
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── gradle/wrapper/
├── gradlew
├── LICENSE
└── README.md
```

## 📱 Установка на телефон

1. Включи **Режим разработчика** → **Отладка по USB** (или разреши установку из неизвестных источников)
2. Перекинь `app-debug.apk` на телефон
3. Установи
4. Открой приложение → нажми **«Установить обои»**
5. Или: Настройки → Обои → Живые обои → выбери «Сантехники»

## 🤝 Как помочь проекту

- Форкни репозиторий
- Добавь новые кадры / улучши анимацию
- Сделай Pull Request

## 📜 Лицензия

MIT License — свободно используй, изменяй и распространяй.
