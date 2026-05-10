FROM python:3.10.19-slim-trixie

WORKDIR /app

RUN pip install --no-cache-dir paho-mqtt

COPY alert.py .

CMD ["python", "-u", "alert.py"]
