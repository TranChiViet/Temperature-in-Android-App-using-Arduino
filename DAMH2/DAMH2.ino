#include "SoftwareSerial.h"
#include "DHT.h"
#define DHpin 12
#define DHTTYPE DHT11
#define buzzer 13
DHT dht(DHpin, DHTTYPE);
String ssid ="T2";
String password="pk0545632";
SoftwareSerial esp(9, 10);
String data;
String server = "viettranchi.000webhostapp.com";
String uri = "/index.php";
void setup() {
pinMode(buzzer, OUTPUT);
esp.begin(9600);
Serial.begin(9600);
dht.begin();
connectWifi();
}
void connectWifi() {
String cmd = "AT+CWJAP=\"" +ssid+"\",\"" + password + "\"";
esp.println(cmd);
delay(4000);
if(esp.find("OK")) {
Serial.println("Connected!");
}
else {
connectWifi();
Serial.println("Cannot connect to wifi"); }
}

String getTemperatureValue(){
   float temp = dht.readTemperature(); 
   delay(50);
   return String(temp);
}
String getHumidityValue(){
   float hum = dht.readHumidity();
   delay(50);
   return String(hum); 
}
void loop () {
  float temp = dht.readTemperature();
  if (temp > 40)
  {digitalWrite(buzzer, LOW);}
  else
  {digitalWrite(buzzer, HIGH);}
data = "temperature=" + getTemperatureValue() + "&humidity=" + getHumidityValue();
Serial.println(data);
httppost();
delay(1000);
}
void httppost () {
esp.println("AT+CIPSTART=\"TCP\",\"" + server + "\",80");//start a TCP connection.
if( esp.find("OK")) {
//Serial.println("TCP connection ready");
} delay(1000);
String postRequest =
"POST " + uri + " HTTP/1.0\r\n" +
"Host: " + server + "\r\n" +
"Accept: *" + "/" + "*\r\n" +
"Content-Length: " + data.length() + "\r\n" +
"Content-Type: application/x-www-form-urlencoded\r\n" +
"\r\n" + data;
String sendCmd = "AT+CIPSEND=";//determine the number of caracters to be sent.
esp.print(sendCmd);
esp.println(postRequest.length());
//Serial.print(sendCmd);
//Serial.println(postRequest.length());
delay(500);
if(esp.find(">")) { 
//Serial.println("Sending.."); 
//Serial.println(postRequest);
esp.println(postRequest);
if( esp.find("SEND OK")) { 
//Serial.println("Packet sent");
while (esp.available()) {
String tmpResp = esp.readString();
//Serial.println(tmpResp);
}
esp.println("AT+CIPCLOSE");
}
}}
