import paho.mqtt.client as mqtt
import time
import random

broker = "mqtt-broker"
client = mqtt.Client(client_id="temp-sensor")
client.connect(broker, 1883, 60)
print("Connected to broker")

while True:
    temp = random.randint(50, 100)
    client.publish("temp", temp)
    print(f"Sent temperature: {temp}")
    time.sleep(1)
