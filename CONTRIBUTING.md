# Contributing to Images Converter

Thank you for your interest in contributing to Images Converter! This document provides guidelines and information for contributors.

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or later
- JDK 21 or higher
- Git
- Basic knowledge of Android development and Jetpack Compose

### Development Setup
1. Fork the repository
2. Clone your fork locally
3. Open the project in Android Studio
4. Sync the project with Gradle files
5. Build and run the project

## 📋 Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions small and focused
- Use Material Design 3 components

### Architecture
- Follow MVVM pattern
- Use Jetpack Compose for UI
- Implement proper state management
- Use Repository pattern for data access
- Follow Android architecture components

### Commit Messages
Use clear and descriptive commit messages:
```
feat: add dark mode toggle
fix: resolve camera permission issue
docs: update README with new features
refactor: simplify theme management
```

## 🐛 Bug Reports

When reporting bugs, please include:
- Android version
- Device model
- Steps to reproduce
- Expected vs actual behavior
- Screenshots if applicable

## ✨ Feature Requests

For feature requests, please:
- Describe the feature clearly
- Explain the use case
- Consider implementation complexity
- Check for existing similar requests

## 🔧 Pull Request Process

1. Create a feature branch from `main`
2. Make your changes
3. Test thoroughly on different devices
4. Update documentation if needed
5. Submit a pull request with a clear description

### PR Requirements
- Code compiles without errors
- All tests pass
- Follows project coding standards
- Includes necessary documentation updates
- Screenshots for UI changes

## 🧪 Testing

### Manual Testing
- Test on different Android versions (API 24+)
- Test on different screen sizes
- Test both light and dark themes
- Test camera and gallery functionality
- Test ML Kit features

### Automated Testing
- Unit tests for ViewModels
- UI tests for critical user flows
- Integration tests for database operations

## 📱 Device Compatibility

The app supports:
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 36 (Android 14)
- **Architecture**: ARM64, ARMv7, x86, x86_64

## 🔒 Security

- Never commit sensitive information
- Use proper permission handling
- Validate user inputs
- Follow Android security best practices

## 📄 License

By contributing, you agree that your contributions will be licensed under the MIT License.

## 🤝 Community

- Be respectful and inclusive
- Help others learn and grow
- Provide constructive feedback
- Follow the code of conduct

## 📞 Getting Help

- Create an issue for bugs or questions
- Join discussions in issues
- Check existing documentation
- Review closed issues for solutions

Thank you for contributing to Images Converter! 🎉
