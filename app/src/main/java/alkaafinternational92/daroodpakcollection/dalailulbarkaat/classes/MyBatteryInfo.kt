package alkaafinternational92.daroodpakcollection.dalailulbarkaat.classes

import android.graphics.Color

data class MyBatteryInfo(
  var batteryLevel: Int = 0,
  var batteryLevelText: String = "0%",
  var isFastCharging: Boolean = false,
  var isCharging: Boolean = false,
  var isChargingText: String = "Not Charging",
  var isGainingCurrent: Boolean = false,
  var isGainingCurrentText: String = "Discharging",
  var chargeSource: String = "Not Plugged",
  var batteryHealth: String = "Unknown",
  var batteryTemperature: Double = 0.0,
  var batteryTemperatureText: String = "°C",
  var batteryTemperatureC: Double = 0.0,
  var batteryTemperatureCText: String = "°C",
  var batteryTemperatureF: Double = 0.0,
  var batteryTemperatureFText: String = "°F",
  var batteryTemperatureK: Double = 0.0,
  var batteryTemperatureKText: String = "K",
  var batteryVoltage: Double = 0.0,
  var batteryVoltageText: String = "mV",
  var batteryTechnology: String = "Unknown",
  var batteryCapacity: Double = 0.0,
  var batteryCapacityText: String = "mAh",
  var chargingSpeedWatts: Double = 0.0,
  var chargingSpeedWattsText: String = "Watts",
  var chargingSpeedWattsTextOrg: String = "Watts",
  var timeToFullCharge: Float = 0f,
  var powerSourceQuality: String = "Unknown",
  var chargerFaulty: Boolean = false,
  var chargerFaultyText: String = "No",
  var chargingCurrent: Int = 0,
  var currentNow: Long = 0,
  var currentNowText: String = "mA",
  var currentNowAbs: Double = 0.0,
  var currentNowAbsText: String = "mA",
  var currentMax: Double = 1.0,
  var currentMaxText: String = "mA",
  var currentAverage: Double = 0.0,
  var currentAverageText: String = "mA",
  var timeToFull: String = "",
  var isStateChange: Boolean = false,
  var batteryLabel: String = "Critical",
  var batteryColor: Int = Color.RED,
  var batteryCapacityCharged: Double = 0.0,
  var batteryCapacityChargedText: String = "mAh",
  var batteryCapacityRemaining: Double = 0.0,
  var batteryCapacityRemainingText: String = "mAh",
)

