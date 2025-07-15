# 📱 MobileAppProject

A movie-app application for mobile built using Android Studio
---

## 🛠️ Technologies Used

### Frontend
- **Android Studio**
- **Kotlin** (Jetpack Compose UI)
- **Material Design Components**

### Backend (Mock)
- **json-server**
- **db.json** (for local data storage)


## Setting up Backend

### Prerequisites

> Install Docker if you want to use devcontainer (In this case, you do not need to install Node.JS on your computer)

Make sure you installed the Node.JS
For details: visit this website and follow the instructions
https://nodejs.org/en/download

### Installation

1. Clone the repo

```sh
git clone https://github.com/Solarctic/MobileAppProject
```

2. Go to the mobile-app folder

```sh
cd MobileAppProject
```

4. Install json-server globally (if not already installed)
```sh
npm install -g json-server
```

## Run Android App

- **Open the MobileAppProject in Android Studio.**
- **Connect your Android device or start an emulator.**
- **Run the app using the Run button.**

## Run Backend Database 

Make sure the project has:

in "AndroidManifest.xml"
```sh
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
...
    <uses-permission android:name="android.permission.INTERNET" /> 
    <application
    ...
        android:usesCleartextTraffic="true"
```
in main/java/com/example/api/ 
```.baseUrl("http://10.0.2.2:4000/")```

is configured along with

```sh
json-server --watch db.json --port 4000
```
