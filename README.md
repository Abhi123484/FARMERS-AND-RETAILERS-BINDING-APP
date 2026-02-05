# Farmers and Retailers Binding Platform

An Android application that connects farmers and retailers to facilitate the buying and selling of crops.

## Features

### 1. User System
- Registration (Signup) and Login pages
- Users choose their role: Farmer or Retailer
- Basic details like name, email, region, phone number stored

### 2. Role-Based Home Pages
- **Farmer Home**: Upload crop details (name, image, price, description, region) and view all their uploaded crops
- **Retailer Home**: View crops uploaded by all farmers, search or filter crops by type or region

### 3. Data Storage & Backend
- Authentication: Firebase (email + password)
- Database: Firestore (user info + crop info)
- Image Storage: Cloudinary (stores crop images)

### 4. UI & Design
- Built using Jetpack Compose and Material 3
- Simple, user-friendly screens
- Works on both phones and tablets

### 5. Security
- Secure login system (each user has their own account)
- Role-based access — Farmers and Retailers see only what they need

### 6. Main Screens
- Splash / Welcome Screen
- Signup Screen
- Login Screen
- Farmer Home Screen
- Retailer Home Screen
- Crop Details Screen

### 7. Core Features
- Role-based navigation (different screens for different users)
- Crop upload (for farmers)
- Crop viewing (for both farmers and retailers)
- Region-wise listing of users and crops
- Logout feature

### 8. Extra / Optional Features
- Search and filter crops
- Display list of all farmers and retailers
- View crop details
- Edit or delete uploaded crops (for farmers only)

## Technology Stack
- **Frontend**: Kotlin, Jetpack Compose, Material 3
- **Backend**: Firebase Authentication, Firestore Database
- **Image Storage**: Cloudinary
- **Navigation**: Android Navigation Component

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
```

### 2. Open in Android Studio
1. Launch Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the project folder and select it

### 3. Firebase Configuration
1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use an existing one
3. Add an Android app with package name: `com.farmers.retailers.app`
4. Download the `google-services.json` file
5. Place it in the `app/` directory, replacing the existing placeholder

### 4. Enable Firebase Services
1. In Firebase Console, go to "Authentication" and enable "Email/Password" sign-in method
2. Go to "Firestore Database" and create a database in "Test mode"

### 5. Cloudinary Configuration
The app is already configured with the following Cloudinary credentials:
- **Cloud Name**: dpd0pjq49
- **API Key**: 918639217899689
- **API Secret**: bK5p1VX4msfsXtRhuQZ7H2i4ZJQ

These are already integrated into the app, so no additional configuration is needed.

### 6. Build and Run
1. Sync the project with Gradle files
2. Connect an Android device or start an emulator
3. Click "Run" to build and deploy the app

## Project Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/farmers/retailers/app/
│   │   │       ├── data/           # Data models
│   │   │       ├── service/        # Firebase and Cloudinary services
│   │   │       ├── ui/
│   │   │       │   ├── screens/    # Screen composables
│   │   │       │   └── theme/      # App theme
│   │   │       ├── MainActivity.kt # Main activity
│   │   │       └── MainApp.kt      # Main app composable
│   │   └── res/                    # Resources
│   └── google-services.json        # Firebase configuration
├── build.gradle                    # App-level build configuration
└── ...
```

## Key Components

### Data Models
- **User**: Stores user information (name, email, region, phone, role)
- **Crop**: Stores crop information (name, image URL, price, description, region)

### Services
- **FirebaseService**: Handles Firebase Authentication and Firestore operations
- **CloudinaryService**: Manages image uploads to Cloudinary

### UI Screens
- **SplashScreen**: Initial loading screen
- **LoginScreen**: User authentication
- **SignupScreen**: User registration with role selection
- **FarmerHomeScreen**: Crop management for farmers
- **RetailerHomeScreen**: Crop browsing for retailers
- **CropDetailsScreen**: Detailed view of crop information

## Features Implementation Status

| Feature | Status | Notes |
|---------|--------|-------|
| User Authentication | ✅ Complete | Firebase Auth |
| Role-based Navigation | ✅ Complete | Farmer/Retailer |
| Crop Upload | ✅ Complete | With Cloudinary image support |
| Crop Browsing | ✅ Complete | Search and filter |
| Image Storage | ✅ Complete | Cloudinary integration |
| Data Storage | ✅ Complete | Firestore database |

## Troubleshooting

### Common Issues
1. **Firebase not working**: Ensure `google-services.json` is correctly placed
2. **Images not uploading**: Check Cloudinary credentials and internet connection
3. **Build errors**: Make sure all dependencies are downloaded

### Getting Help
If you encounter any issues, please check:
1. The Firebase Console for error logs
2. Android Studio's Logcat for runtime errors
3. Cloudinary dashboard for upload issues

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.