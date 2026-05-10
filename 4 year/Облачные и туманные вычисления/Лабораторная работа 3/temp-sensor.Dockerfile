FROM python:3.10.19-slim-trixie

WORKDIR /app

RUN pip install --no-cache-dir paho-mqtt

COPY temp_sensor.py .

CMD ["python", "-u", "temp_sensor.py"]
