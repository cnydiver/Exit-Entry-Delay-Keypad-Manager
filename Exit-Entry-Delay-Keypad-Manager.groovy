/**
 *  Exit/Entry Delay & Keypad Manager
 *
 *  Copyright 2018 Greg Setser
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Exit/Entry Delay & Keypad Manager",
    namespace: "gmsetser",
    author: "Greg Setser",
    description: "SmartApp to manage exit and entry delays for Smart Home Monitor and manage Centralite/IRIS Keypads",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/SafetyAndSecurity/Cat-SafetyAndSecurity.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/SafetyAndSecurity/Cat-SafetyAndSecurity@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/SafetyAndSecurity/Cat-SafetyAndSecurity@3x.png")

//Preferences and menus for the app GUI
preferences {
    page(name: "mainMenu")
    page(name: "introduction")
    page(name: "setupKeypads")
    page(name: "setupArmedAway")
    page(name: "setupArmedStay")
    page(name: "setupSimSensors")
page(name: "setupDelays")
    page(name: "setupNotifications")
    page(name: "setupRoutines")
}
def mainMenu(){  //Main menu page linking to other pages with preferences
	dynamicPage(name: "mainMenu", title: "Main Menu",  install: true, uninstall: true) {
    	section("") {
        href(name: "introduction", title: "Introduction", description: "", required: true, page: "introduction")
        href(name: "setupDelays", title: "Exit & Entry Delays", description: "", required: false, page: "setupDelays")
        href(name: "setupArmedAway", title: "Armed(Away) Sensors", description: "", required: false, page: "setupArmedAway")
	href(name: "setupArmedStay", title: "Armed(Home) Sensors", description: "", required: false, page: "setupArmedStay")
        href(name: "setupSimSensors", title: "Simulated Sensors", description: "", required: false, page: "setupSimSensors")
	href(name: "setupKeypads", title: "Keypads and Pins", description: "", required: false, page: "setupKeypads")
        href(name: "setupNotifications", title: "Notification Preferences", description: "", required: false, page: "setupNotifications")        
	//href(name: "setupRoutines", title: "Routines", required: false, page: "setupRoutines")
	}
    }
}
def introduction(){
	dynamicPage(name: "introduction", title: "") {
    	section (title: "About this App", hideable: true, hidden: false) {
			paragraph "This smartapp is your one stop application to setup Exit & Entry Delays, Keypads & Pins, and Notifications for Smart Home Monitor."
			}    	
        section (title: "Exit & Entry Delays", hideable: true, hidden: true) {
            paragraph "This smartapp adds the ability to setup Exit and Entry Delays for Smart Home Monitor. "+ 
	"Exit Delay gives you an amount of time to exit your home after Smart Home Monitor is armed without triggering an intrusion alarm. "+
			"Entry Delay gives you an amount of time to enter your home and disarm Smart Home Monitor without triggering an intrusion alert. \n\n"+ 
            "Additionally, this smartapp will prevent motion sensors from triggering an intrusion alert during Exit and Entry delays. "+
            "This ensures a false alarm is not triggered if a motion sensor's detection area is near an exit/entry door or keypad. \n\n"+
			"The delays operate off the sensors you setup in this smartapp and not those setup in the Smart Home Monitor application. "+
            "To ensure the delays operate correctly (not trip an intrusion alert during a delay sequence), remove all Open/Closed and Motion sensors from Smart Home Monitor and set them within this smartapp. "+
            "This app allows to setup sensors that should be delayed as well as sensors that should instantly trigger an intrusion alert.\n\n"+
            "You need to add a Simulated Contact Sensor and a Simulated Motion Sensor to your SmartThings Hub through the SmartThings IDE. "+
            "Then, set these as the monitored sensors in Smart Home Monitor and add these to this smartapp under the Simulated Sensors menu. "+
            "The smartapp maps the action detected from the real sensors, monitored in this app, to the simulated sensors to delay or trigger intrusion alerts."
            }
    	section (title: "Keypads & Pins", hideable: true, hidden: true) {
            paragraph "This smartapp allows you to use a Centralite/Iris Keypad and define Pins to arm and disarm Smart Home Monitor. "+
            "Before you can setup a keypad in this smartapp, you need to install the Mitch Pond, Zack Cornelius keypad device handler and sync the keypad with your SmartThings hub. "+
            "You can find the device handler at the link below:" 
            href(name: "", title: "", description: "Link to Device Handler", url: "https://github.com/miriad/Centralite-Keypad/blob/master/devicetypes/mitchpond/centralite-keypad.src/centralite-keypad.groovy")
            }
        section (title: "Notifications", hideable: true, hidden: true) {
            paragraph "Finally, this smartapp can send intrusion notifications instead of using those in Smart Home Monitor. "+
            "The notifications from this app provide the name of the real sensor (Open/Closed or Motion) that triggered an Intrusion Alarm. "+
            "If you setup a keypad in this app, you can turn on additional notifications to notify you when the keypad Panic button is pushed or when a specific pin code is used to disarm Smart Home Monitor."
			}

	}
}

def setupKeypads(){ 
	dynamicPage(name: "setupKeypads", title: "Keypads and Pins") {
	section("Select Keypads") {
        input "keypads", "capability.lockCodes", required: false, multiple: true, submitOnChange: true, title:"Select Keypads"
        }
   	if(keypads){
		section("Enter Pins") {
        	input "pinPrimary", "number", required: true, range: "0000..9999", title: "Primary Pin: Enter a 4 Digit Number"
        	input "pinGuest", "number", required: false, range: "0000..9999", title: "Guest Pin: Enter a 4 Digit Number"
        	input "pinWorker", "number", required: false, range: "0000..9999", title: "Worker Pin: Enter a 4 Digit Number"
			}
    	section("Select Keypad Options"){
        	input "noPinArm", "bool", required: false, title: "Arm without Entering a Pin (Note: Pin is Required to Disarm)"
        	input "panicAlarm", "bool", required: false, title: "Panic Button Triggers Intrusion Alarm"
        	input "keypadBeep", "bool", required: false, title: "Keypad Beeps During Exit & Entry Delay"
        	}
    	}
	}
}
def setupArmedAway() {
    dynamicPage(name: "setupArmedAway", title: "Armed (Away) Intrusion Sensors & Delays") {
    section(){
    	paragraph "All sensors selected below should be removed from Smart Home Monitor.", required: true
    	}
    section("Select Open/Closed Sensors to Monitor when the Home is Unoccupied") {
        input "instantContactSensorAway", "capability.contactSensor", required: false, multiple: true, title: "Open/Closed Sensors (Instant Alarm)"
        input "delayContactSensorAway", "capability.contactSensor", required: false, multiple: true, title: "Open/Closed Sensors (Delayed Alarm)"
		}
    section("Select Motion Sensors to Monitor when the Home is Unoccupied") {
		input "motionSensorAway", "capability.motionSensor", required: false, multiple: true, title: "Motion Sensors"
        }
	}
}
def setupArmedStay() {
    dynamicPage(name: "setupArmedStay", title: "Armed(Home) Intrusion Sensors & Delays") 
    {
    section(){
    	paragraph "All sensors selected below should be removed from Smart Home Monitor.", required: true
    	}
    section("Select Open/Closed Sensors to Monitor when the Home is Occupied") {
        input "instantContactSensorStay", "capability.contactSensor", required: false, multiple: true, title: "Open/Closed Sensors (Instant Alarm)"
        input "delayContactSensorStay", "capability.contactSensor", required: false, multiple: true, title: "Open/Closed Sensors (Delayed Alarm)"
		}
    section("Select Motion Sensors to Monitor when the Home is Occupied") {
		input "motionSensorStay", "capability.motionSensor", required: false, multiple: true, title: "Motion Sensors"
        }
	}
}
def setupSimSensors() {
    dynamicPage(name: "setupSimSensors", title: "Select Simulated Sensors") 
    {
    section(){
    	paragraph "The simulated sensors will activate when a monitored sensor activates.  Set the simulated sensors as the monitored sensors in Smart Home Monitor.", required: true
    }
    section("Select Simulated Open/Closed Sensor") {
        input "simContactSensor", "capability.contactSensor", required: false, title: "Simulated Open/Close Sensor"
    	}
    section("Select Simulated Motion Sensor") {
        input "simMotionSensor", "capability.motionSensor", required: false, title: "Simulated Motion Sensor"
		}
    section("How to Create Simulated Sensors", hideable: true, hidden: true) {
    	paragraph " 1. Go to https://graph.api.smartthings.com\n"+
			" 2. Login\n"+
			" 3. Click 'My Locations'\n"+
			" 4. Under 'Name' click on the appropriate location\n"+
			" 5. Click 'My Devices' on the top menu\n"+ 
			" 6. Click the 'New Device' button\n"+
			" 7. Name: Enter a name for your simulated sensor\n"+  
			" 8. Device Network Id: Enter any value, (ie: sim01)\n"+
			" 9. Type: Select 'Simulated Contact Sensor' from the dropdown\n"+
			"10. Version: Select 'Published'\n"+
			"11. Location: Select your location name from the dropdown\n"+ 
			"12. Hub: Select your hub name from the dropdown\n"+ 
			"13. Click Create\n"+
            "14. Repeat steps 6 to 13; however, in step 9 select 'Simulated Motion Sensor'\n"
        }
	}
}
def setupDelays() {
    dynamicPage(name: "setupDelays", title: "Exit & Entry Delays") 
    {
    section(){
    	paragraph "", required: true
    	}
	section("Enter Exit Delay (Amount of time to exit the home after Smart Home Monitor is armed without triggering an intrusion alarm)") {
        input "exitDelay", "number", required: true, range: "0..120", defaultValue: 30, title: "Exit Delay Time in Seconds from 0 to 120"
		}
	section("Enter Entry Delay (Amount of time to enter the home and disarm Smart Home Monitor before the intrusion alarm occurs)") {
        input "entryDelay", "number", required: true, range: "0..120", defaultValue: 30, title: "Entry Delay Time in Seconds from 0 to 120"
		}
	}
}
def setupNotifications() {
	dynamicPage(name: "setupNotifications", title: "Text & Push Notifications")
    {
    section("Messaging Options") {
		input "sendPush", "bool", title: "Send Push Notification", required: false
        input "sendSMS", "phone", title: "SMS Notification (Enter Phone Number)", required: false
    	}
    section("Events that will Trigger a Notification") {
		input "sendMsgIntrusion", "bool", title: "Intrusion Alerts (Open/Closed or Motion Sensor Trips)", required: false
        if (keypads){
        	input "sendMsgPanic", "bool", title: "Intrusion Alert (Keypad Panic Button is Pushed)", required: false
            input "sendMsgGuestPin", "bool", title: "Guest Pin Disarms System", required: false
			input "sendMsgWorkerPin", "bool", title: "Worker Pin Disarms System", required: false
        	}
        }
    }
}
/** def setupRoutines() {
	dynamicPage(name: "setupRoutines", title: "Routines"){
    section("Select Routines to Run when Smart Home Monitor is Armed and Disarmed") {
		input "ArmAwayRoutines", "enum", title: "Armed (Away)", options: location.helloHome?.getPhrases()*.label.sort(), required: false, multiple: true
    	input "ArmStayRoutines", "enum", title: "Armed (Home)", options: location.helloHome?.getPhrases()*.label.sort(), required: false, multiple: true
    	input "DisarmRoutines", "enum", title: "Disarmed", options: location.helloHome?.getPhrases()*.label.sort(), required: false, multiple: true
    	}
    }
} **/
//End Preferences and menus for the app GUI

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

//Subscriptions for events to trigger the app to initiate
def initialize() {
	subscribe(location, "alarmSystemStatus", alarmStatusHandler) //Subscribes to Smart Home Monitor status changes
	subscribe(instantContactSensorAway, "contact.open", contactOpenHandler) //Subscribes to selected contact sensors opening
	subscribe(delayContactSensorAway, "contact.open", contactOpenHandler) //Subscribes to selected contact sensors opening	
	subscribe(instantContactSensorStay, "contact.open", contactOpenHandler) //Subscribes to selected contact sensors opening	
	subscribe(delayContactSensorStay, "contact.open", contactOpenHandler) //Subscribes to selected contact sensors opening	
    subscribe(motionSensorAway, "motion.active", motionActiveHandler) //Subscribes to selected motion sensors opening
    subscribe(motionSensorStay, "motion.active", motionActiveHandler) //Subscribes to selected motion sensors opening
    subscribe(keypads, "button.pushed", buttonHandler) //Subscribes to keypad panic button pushed
    subscribe(keypads, "codeEntered", codeEntryHandler) //Subscribes to keypad code entry
    atomicState.entryDelayRunning = false //Global state set to TRUE when the entry delay sequence is running, prevents motion sensor from activating during entry delay sequence 
	atomicState.exitDelayRunning = false //Global state set to TRUE when the ext delay sequence is running
    atomicState.alarmContact //Captures name of open contact sensor for push & SMS notifications
    atomicState.alarmMotion //Captures name of active motion sensor for push & SMS notifications
}
//End Subscriptions

//Start evaluating Smart Home Monitor status, executes from alarmSystemStatus subscription
def alarmStatusHandler(evt) //triggers when alarm status changes
{
    def alarmstatus = location.currentState("alarmSystemStatus").value
    if(alarmstatus == "away") //checks SHM set to "Away" and runs exit delay sequence
    {    
        atomicState.exitDelayRunning = true
        log.debug "Caught exit delay ${atomicState.exitDelayRunning}"
        if(exitDelay > 0 && keypadBeep == true) runIn(2, keypadExitDelay) //2 second delay to start keypad exit delay beeps to fix an issue with the LED not turning off
        runIn(exitDelay, endExitDelay)
    }
    if(alarmstatus == "stay") //checks SHM set to "Stay" and sets  status to away (red text)
    {
		keypads.setArmedStay() //sets appropriate light color on keypad
        location.helloHome?.execute(settings.ArmStayRoutines)
    }
    if(alarmstatus == "off") //checks SHM set to "Disarm" and sets keypad status to disarmed (green text)
    {
    	keypads.setDisarmed() //sets appropriate light color on keypad
        unschedule(openSimContactSensor) //important if system is rearmed within entry delay time
        unschedule(endExitDelay) //important if system is disarmed during exit delay
        location.helloHome?.execute(settings.DisarmRoutines)
        atomicState.entryDelayRunning = false
    	atomicState.exitDelayRunning = false
        log.debug "Caught exit delay ${atomicState.exitDelayRunning}"
        log.debug "Caught entry delay ${atomicState.entryDelayRunning}"
    }
}
def endExitDelay() {
	atomicState.exitDelayRunning = false
    log.debug "Caught exit delay ${atomicState.exitDelayRunning}"
    location.helloHome?.execute(settings.ArmAwayRoutines)
	keypads.setArmedAway() //sets appropriate light color on keypad
}
//End evaluating Smart Home Monitor status

//Start of Entry Delay Sequence, Method executes from contact.open subscriptions above
def contactOpenHandler(evt) //method auto runs when a door contact opens
{
	log.debug "Caught contact sensor open name! ${evt.displayName}"
    log.debug "Caught contact sensor open event! ${evt.value}"
    atomicState.alarmContact = evt.displayName //Captures name of open contact sensor for push & SMS notifications
    def alarmstatus = location.currentState("alarmSystemStatus").value
    if(alarmstatus == "away" && delayContactSensorAway && atomicState.exitDelayRunning == false || alarmstatus == "stay" && delayContactSensorStay)
    {
        atomicState.entryDelayRunning = true //Prevents motion sensor from activating during entry delay
        log.debug "Caught entry delay ${atomicState.entryDelayRunning}"
        if(entryDelay > 0 && keypadBeep == true) keypads.setEntryDelay(entryDelay)
        runIn(entryDelay, openSimContactSensor) //Runs openSimContactSensor method below after entry delay time
    }  
    if(alarmstatus == "away" && instantContactSensorAway && atomicState.exitDelayRunning == false || alarmstatus == "stay" && instantContactSensorStay)
    {
        atomicState.entryDelayRunning = true
        log.debug "Caught entry delay ${atomicState.entryDelayRunning}"
        runIn(1, openSimContactSensor) //Runs openSimContactSensor method below immediately
    }
	if(alarmstatus == "off")
    {
        //if(keypadBeep == true) keypads.beep()
    }
}

def openSimContactSensor() //note no evt in parenthesis, the method will only run when called
{
//	def alarmstatus = location.currentState("alarmSystemStatus").value
//	if(alarmstatus != "off" && atomicState.entryDelayRunning == true && atomicState.exitDelayRunning == false) //Open simulated contact sensor if alarm is not disarmed & entry delay is running
//		{
		simContactSensor.open()
    	simContactSensor.close([delay:5000])
        if (sendMsgIntrusion == true) runIn(0, msgIntrusionContact)
        atomicState.entryDelayRunning = false //Returns to FALSE to let motion sensors run after delay
        log.debug "Caught entry delay ${atomicState.entryDelayRunning}"
//		}
}
//End of Entry Delay Sequence

//Start Keypad Pin, handler executes from codeEntered subscription above
def codeEntryHandler(evt)
{
	log.debug "Caught code entry data! ${evt.data}"
    log.debug "Caught code entry event! ${evt.value}"
	def codeEntered = evt.value as String
    def primaryCode = pinPrimary as String
    def guestCode = pinGuest as String
    def workerCode = pinWorker as String
    def codeMatch
    def data = evt.data as String
    
    //if statement determines if the entered code matches one of the stored codes
    if(codeEntered == primaryCode) codeMatch = true
    else if(codeEntered == guestCode) codeMatch = true
    else if(codeEntered == workerCode) codeMatch = true
    else codeMatch = false
    
    log.debug "Code entered matched! ${codeMatch}"
    
	if(codeMatch == true && data == "0")
    	{
        keypads.acknowledgeArmRequest(0)
        runIn(0, disarmAlarm) //Runs disarmAlarm method below immediately
        if (codeEntered == guestCode && sendMsgGuestPin) runIn(0, msgGuestPin) //Runs msgGuestPin method to send a notification
        if (codeEntered == workerCode && sendMsgWorkerPin) runIn(0, msgWorkerPin) //Runs msgWorkerPin method to send a notification
		}
	if(codeMatch == true && data == "1" || noPinArm == true && data == "1")
		{
        keypads.acknowledgeArmRequest(1)
        runIn(0, armStayAlarm) //Runs armStayAlarm method below after exit delay time
		}       
    if(codeMatch == true && data == "2" || noPinArm == true && data == "2")
    	{
        keypads.acknowledgeArmRequest(2)
        runIn(0, armStayAlarm) //Runs armStayAlarm method below after exit delay time
		}
    if(codeMatch == true && data == "3" || noPinArm == true && data == "3")
    	{
        keypads.acknowledgeArmRequest(3)
        runIn(0, armAwayAlarm) //Runs armAwayAlarm method below immediately
		}
	if(codeMatch == false && noPinArm == false)
    	{
    	keypads.sendInvalidKeycodeResponse()
     	}
}
//End Keypad Pin handler

//Start Keypad Exit Delay
def keypadExitDelay()
{
	keypads.setExitDelay(exitDelay - 2) //subtract 2 from the keypad exit delay beeps to account for the above 2 second delay
}
//End Keypad Exit Delay

//Start Panic Button Sequence, Method executes from button.pushed subscription above
def buttonHandler(evt) //note evt in parenthesis, method auto runs when keypad panic button is pushed
{
	def alarmstatus = location.currentState("alarmSystemStatus").value
    if(panicAlarm == true)
    {
        runIn(0, armStayAlarm) //Runs armAwayAlarm method below immediately
        simContactSensor.open([delay:2000])
        if (sendMsgPanic == true) runIn(0, msgPanic)
    	simContactSensor.close([delay:5000])
	}
}
//End Panic Button Sequence

//Start Arm and Disarm Sequences
def armAwayAlarm() 
{
	sendLocationEvent(name: "alarmSystemStatus", value: "away") //arms SHM in away
}

def armStayAlarm() 
{
	sendLocationEvent(name: "alarmSystemStatus", value: "stay") //arms SHM in stay
}

def disarmAlarm() 
{
	sendLocationEvent(name: "alarmSystemStatus", value: "off") //disarms SHM
}
//End Arm and Disarm Sequences

//Start of Motion Sequence
def motionActiveHandler(evt) //pause motion sensors during entry delay
{
    log.debug "Caught Motion from Device! ${evt.displayName}"
    atomicState.alarmMotion = evt.displayName //Captures name of active motion sensor for push & SMS notifications
    def alarmstatus = location.currentState("alarmSystemStatus").value
    if(alarmstatus != "off")
    {
    	runIn(2, openSimMotionSensor) //Runs openSimMotionSensor method below after a short delay to ensure no timing issues
    }
}

def openSimMotionSensor() //note no evt in parenthesis, the method will only run when called
{
	if(atomicState.entryDelayRunning == false && atomicState.exitDelayRunning == false) //Only open sensor contacts if entry  or exit delay sequences are not running
    {
		simMotionSensor.active()
   		simMotionSensor.inactive([delay:5000]) //closes simulated motion sensor after 5 seconds
        if (sendMsgIntrusion == true) runIn(0, msgIntrusionMotion) //run intrusion notification method
    }
}
//End of Motion Sequence

//Start Notifications
def msgIntrusionContact() {
	def message = "${atomicState.alarmContact} intrusion detected!" //Creates intrusion message
	if (sendPush == true) sendPush(message) //Sends push message
	if (sendSMS) sendSms(sendSMS, message) //Send SMS message
}
def msgIntrusionMotion() {
    def message = "${atomicState.alarmMotion} motion detected!" //Creates intrusion message
    if (sendPush == true) sendPush(message) //Sends push message
    if (sendSMS) sendSms(sendSMS, message) //Send SMS message
}
def msgPanic() {
	def message = "Panic Button Pressed!" //Creates intrusion message
	if (sendPush == true) sendPush(message) //Sends push message
	if (sendSMS) sendSms(sendSMS, message) //Send SMS message
}
def msgGuestPin() {
	def message = "System Disarmed with Guest Pin" //Creates intrusion message
	if (sendPush == true) sendPush(message) //Sends push message
	if (sendSMS) sendSms(sendSMS, message) //Send SMS message
}
def msgWorkerPin() {
	def message = "System Disarmed with Worker Pin" //Creates intrusion message
	if (sendPush == true) sendPush(message) //Sends push message
	if (sendSMS) sendSms(sendSMS, message) //Send SMS message
}
// TODO: implement event handlers as needed
