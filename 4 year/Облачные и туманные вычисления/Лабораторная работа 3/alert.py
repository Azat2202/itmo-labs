import paho.mqtt.client as mqtt

def on_message(client, userdata, msg):
    print(f"ALERT: {msg.payload.decode()}")

broker = "mqtt-broker"
client = mqtt.Client(client_id="cloud")
client.on_message = on_message
client.connect(broker, 1883, 60)
client.subscribe("alerts")
client.loop_forever()
