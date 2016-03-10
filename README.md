# TestDiploma
This is prototype application within the context of the my MSc thesis. It has several modules/rules, rules that  are based upon 
the daily use of the device and are intended to help the user. Considering the continuous hustle that a regular user undergoes, 
we seek to release him from these routine movements. Examples:
1)	If user is at home, then change the ringtone volume to mute and enable vibration: Detecting the smartphone’s location 
and the time of the departure we enable this rule in order to decrease the volume of the device and also enable the vibration.
2)	If user is at meeting then set device on Meeting Mode: Detecting the smartphone’s location and the calendar events 
we enable this rule in order to mute the device and also enable the vibration. At the same time we disable the communication via calls.
3)	If user leaves the office, then provide him a tool to organize his/hers commuting. De-tecting the smartphone’s location and 
the exit time we enable this rule in order to create a connection with an 
external Google API to consume the current Public Transport directions based on user’s location.
4)	 If user leaves the office, then notify him about current events in city. 
Detecting the smartphone’s location and the exit time we enable this rule in order to create a con-nection with an external 
API (Eventfull API) to consume the events based on user’s location.
5)	If a caller belongs to top three callers and Rule 4 is applied then notify caller via sms that user is at meeting. 
Detecting the call log file daily, we enable this rule in order to send a sms message inform caller that user is at meeting.
6)	Every week display top three applications on homepage. 
Detecting the application log file weekly, we enable this rule in order to set on homepage the three most used applications  
7)	If the light sensor is near zero and acceleration/speed different from zero then set the ringtone volume to 100% 
and enable vibration. Detecting the motion of the device the proximity sensor, the state of the device and the value of 
the light sensor periodically, we enable this rule in order to set the volume of ringtone to max value and set the vibration on.
...............
