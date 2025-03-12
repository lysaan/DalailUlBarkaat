package findmyphone.findmyphonebyclap.phonefindergo.others

class MyEnum {
  companion object {
    const val ONE_DAY = 86400000 // Milliseconds in a day
    const val ANDROID_BATTERY = "findmyphone.findmyphonebyclap.phonefindergo"
    const val MIN_BACKOFF_MILLIS = 60 * 1000L // 60 seconds delays for service run and other background tasks
    const val TEMP_UNIT_CELSIUS = 0
    const val TEMP_UNIT_FAHRENHEIT = 1
    const val TEMP_UNIT_KELVIN = 2
    const val THEME_SYSTEM = 0
    const val THEME_DARK = 1
    const val THEME_LIGHT = 2
    const val CHANNEL_MUTE = "BatteryGoNotificationChannel"
    const val CHANNEL_SOUND = "BatteryGoAlertChannel"
    const val NOTIFICATION_ID_SERVICE = 1
    const val NOTIFICATION_ID_BATTERY_LOW = 2
    const val NOTIFICATION_ID_BATTERY_HIGH = 3
    const val NOTIFICATION_ID_TEMP_HIGH = 4
    const val NOTIFICATION_TRIGGER_DELAY = 5 * 60 * 1000
}
}