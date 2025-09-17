# Images Converter

A modern Android application for document scanning and text recognition using ML Kit, built with Jetpack Compose and Material Design 3.

## 🚀 Features

- **📄 Document Scanner**: Scan documents using ML Kit Document Scanner API
- **📝 Text Recognition**: Extract text from images using ML Kit Text Recognition
- **📱 Camera Integration**: Take photos with CameraX for text recognition
- **🖼️ Gallery Picker**: Select images from gallery for text recognition
- **📋 PDF Generation**: Convert scanned documents to PDF files
- **🌙 Dark Mode**: Toggle between light and dark themes
- **🎨 Modern UI**: Glassmorphism design with Material Design 3
- **💾 Local Storage**: Save and manage PDF files locally using Room database

## 📋 Requirements

### System Requirements
- **Android Studio**: Arctic Fox (2020.3.1) or later
- **Java**: JDK 8 or higher
- **Android SDK**: API Level 24 (Android 7.0) or higher
- **Target SDK**: API Level 36 (Android 14)

### Development Environment
- **Android Gradle Plugin (AGP)**: 8.10.1
- **Kotlin**: 2.1.0
- **Gradle**: 8.0+
- **Compose Compiler**: 2.0.0
- **Java Compatibility**: Java 21

## 🛠️ Installation

### Prerequisites
1. Install [Android Studio](https://developer.android.com/studio)
2. Install JDK 8 or higher
3. Clone this repository

### Setup
1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Ensure all dependencies are downloaded
4. Build and run the project

### Dependencies
The project uses the following major dependencies:

```kotlin
// Core Android
implementation 'androidx.core:core-ktx:1.17.0'
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.9.3'
implementation 'androidx.activity:activity-compose:1.11.0'

// Jetpack Compose
implementation platform('androidx.compose:compose-bom:2025.09.00')
implementation 'androidx.compose.ui:ui'
implementation 'androidx.compose.material3:material3'
implementation 'androidx.compose.ui:ui-tooling-preview'

// Navigation
implementation 'androidx.navigation:navigation-compose:2.9.4'

// ML Kit
implementation 'com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1'
implementation 'com.google.android.gms:play-services-mlkit-text-recognition:19.0.1'
implementation 'com.google.mlkit:text-recognition:16.0.1'

// Camera
implementation 'androidx.camera:camera-core:1.5.0'
implementation 'androidx.camera:camera-camera2:1.5.0'
implementation 'androidx.camera:camera-view:1.5.0'

// Database
implementation 'androidx.room:room-runtime:2.8.0'
implementation 'androidx.room:room-ktx:2.8.0'

// Permissions
implementation 'com.google.accompanist:accompanist-permissions:0.31.0-alpha'

// Image Loading
implementation 'io.coil-kt:coil-compose:2.6.0'
```

## 🏗️ Project Structure

```
app/
├── src/main/java/com/saefulrdevs/imagesconverter/
│   ├── MainActivity.kt                    # Main activity
│   ├── SplashScreen.kt                   # Splash screen
│   ├── data/
│   │   ├── database/                     # Room database
│   │   ├── model/                        # Data models
│   │   └── repository/                   # Repository pattern
│   ├── ui/
│   │   ├── components/                   # Reusable UI components
│   │   ├── navigation/                   # Navigation setup
│   │   ├── screens/                      # Screen composables
│   │   │   ├── homescreen/              # Home screen
│   │   │   ├── textrecognitionscreen/   # Text recognition
│   │   │   ├── documentscannerscreen/   # Document scanner
│   │   │   └── pdflistscreen/           # PDF list
│   │   └── theme/                        # Theme and colors
│   └── viewmodel/                        # ViewModels
└── src/main/res/                         # Resources
    ├── drawable/                         # Icons and images
    ├── fonts/                           # Font files
    └── values/                          # Strings, colors, themes
```

## 🎨 Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture pattern:

- **Model**: Room database entities and data classes
- **View**: Jetpack Compose UI components
- **ViewModel**: State management and business logic
- **Repository**: Data access layer abstraction

## 🔧 Configuration

### Permissions
The app requires the following permissions:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_MEDIA_VISUAL_USER_SELECTED" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### ML Kit Setup
ML Kit is configured for:
- Document scanning with automatic edge detection
- Text recognition with multiple language support
- On-device processing for privacy

## 🚀 Usage

### Document Scanning
1. Tap the document scan FAB
2. Point camera at document
3. ML Kit automatically detects edges
4. Tap to capture
5. Document is saved as PDF

### Text Recognition
1. Tap the text scan FAB
2. Choose camera or gallery
3. Take photo or select image
4. Text is extracted and displayed
5. Copy or share the text

### Dark Mode
- Toggle the switch in the top toolbar
- Theme changes instantly across all screens

## 🐛 Troubleshooting

### Common Issues

1. **Build Errors**
   - Ensure Java 8+ is installed
   - Check Android Studio version compatibility
   - Clean and rebuild project

2. **ML Kit Issues**
   - Ensure Google Play Services is updated
   - Check device compatibility
   - Verify permissions are granted

3. **Camera Issues**
   - Grant camera permission
   - Check device camera functionality
   - Ensure target SDK compatibility

### Version Compatibility

| Component | Version | Notes |
|-----------|---------|-------|
| Android Gradle Plugin | 8.10.1 | Latest stable |
| Kotlin | 2.1.0 | Latest stable |
| Compose Compiler | 2.0.0 | Must match Kotlin version |
| Java | 8+ | Required for compatibility |
| Min SDK | 24 | Android 7.0+ |
| Target SDK | 36 | Android 14 |

## 📱 Screenshots

*Screenshots will be added here*

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [ML Kit](https://developers.google.com/ml-kit) for document scanning and text recognition
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern UI
- [Material Design 3](https://m3.material.io/) for design system
- [CameraX](https://developer.android.com/training/camerax) for camera functionality

## 📞 Support

For support, email [your-email@example.com] or create an issue in this repository.

---

**Note**: This app requires Android 7.0+ and Google Play Services for ML Kit functionality.
