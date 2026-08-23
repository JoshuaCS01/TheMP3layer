# MP3Layer

MP3Layer is a native Android music player written in Java. It discovers audio stored on the device through Android's MediaStore and provides centralized playback controls across the application.

## Implemented features

- Query local audio files through MediaStore
- Display songs using fragments and RecyclerView adapters
- Play and pause the selected track
- Move to the previous or next track
- Automatically advance when a track finishes
- Maintain shared playback state through a centralized playback manager
- Request the Android media permission required for local audio access

## Technology stack

- Java
- Android SDK and Android Studio
- MediaStore
- Fragments and RecyclerView
- Android media playback APIs
- Gradle

## Architecture

The application separates media discovery, interface presentation, and playback state. Fragments and adapters present the local library, while a shared playback manager coordinates the selected song and play, pause, previous, next, and automatic-advance behavior.

## Run the project

1. Clone the repository.
2. Open it in Android Studio.
3. Allow Gradle to synchronize dependencies.
4. Run it on an Android device or emulator containing accessible audio files.
5. Grant media access when Android requests permission.

## Roadmap

The following capabilities are planned and are not part of the current public implementation:

- SQLite-backed metadata storage
- Additional metadata retrieval
- Expanded sorting, filtering, and organization
- Playlist management
