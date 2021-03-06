# What?

A sample Android application for mobile number verification.

# How?

1. Send a SMS with a 4 digit random number to the number entered.
2. Wait for the SMS.

# Features

- Detect user's home country using
  [getSimCountryIso()](https://developer.android.com/reference/android/telephony/TelephonyManager.html#getSimCountryIso()).
- Display list of countries with calling code using
  [libphonenumber](https://github.com/googlei18n/libphonenumber).
- Phone number validation using libphonenumber.
- Save verified phone number into a
  [SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences.html)
  file.

# APK

Get a precompiled APK
[here](https://github.com/arpankapoor/VerifyMobileNumber/releases).

# Screenshots

![](./screenshots/1.png)
![](./screenshots/2.png)
![](./screenshots/3.png)
![](./screenshots/4.png)
![](./screenshots/5.png)

# License

This is free and unencumbered software released into the public domain.
