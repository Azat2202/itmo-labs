import asyncio
import json
import os
import random
import string

import aiohttp
from yandex_music import Client


async def create_ynison_ws(ya_token: str, ws_proto: dict) -> dict:
    async with aiohttp.ClientSession() as session:
        async with session.ws_connect(
            "wss://ynison.music.yandex.ru/redirector.YnisonRedirectService/GetRedirectToYnison",
            headers={
                "Sec-WebSocket-Protocol": f"Bearer, v2, {json.dumps(ws_proto)}",
                "Origin": "http://music.yandex.ru",
                "Authorization": f"OAuth {ya_token}",
            },
        ) as ws:
            response = await ws.receive()
            return json.loads(response.data)

def generate_device_id(length: int = 16) -> str:
    return ''.join(random.choices(string.ascii_lowercase, k=length))

async def get_current_track_beta(info, ya_token):
    device_id = generate_device_id()
    ws_proto = {
        "Ynison-Device-Id": device_id,
        "Ynison-Device-Info": json.dumps({"app_name": "Chrome", "type": 1}),
    }
    data = await create_ynison_ws(ya_token, ws_proto)

    ws_proto["Ynison-Redirect-Ticket"] = data["redirect_ticket"]

    payload = {
        "update_full_state": {
            "player_state": {
                "player_queue": {
                    "current_playable_index": -1,
                    "entity_id": "",
                    "entity_type": "VARIOUS",
                    "playable_list": [],
                    "options": {"repeat_mode": "NONE"},
                    "entity_context": "BASED_ON_ENTITY_BY_DEFAULT",
                    "version": {"device_id": device_id, "version": 9021243204784341000, "timestamp_ms": 0},
                    "from_optional": "",
                },
                "status": {
                    "duration_ms": 0,
                    "paused": True,
                    "playback_speed": 1,
                    "progress_ms": 0,
                    "version": {"device_id": device_id, "version": 8321822175199937000, "timestamp_ms": 0},
                },
            },
            "device": {
                "capabilities": {"can_be_player": True, "can_be_remote_controller": False, "volume_granularity": 16},
                "info": {
                    "device_id": device_id,
                    "type": "WEB",
                    "title": "Chrome Browser",
                    "app_name": "Chrome",
                },
                "volume_info": {"volume": 0},
                "is_shadow": True,
            },
            "is_currently_active": False,
        },
        "rid": "ac281c26-a047-4419-ad00-e4fbfda1cba3",
        "player_action_timestamp_ms": 0,
        "activity_interception_type": "DO_NOT_INTERCEPT_BY_DEFAULT",
    }

    async with aiohttp.ClientSession() as session:
        async with session.ws_connect(
                f"wss://{data['host']}/ynison_state.YnisonStateService/PutYnisonState",
                headers={
                    "Sec-WebSocket-Protocol": f"Bearer, v2, {json.dumps(ws_proto)}",
                    "Origin": "http://music.yandex.ru",
                    "Authorization": f"OAuth {ya_token}",
                }
        ) as ws:
            await ws.send_str(json.dumps(payload))
            response = await ws.receive()
            ynison = json.loads(response.data)

    track = ynison["player_state"]["player_queue"]["playable_list"][
        ynison["player_state"]["player_queue"]["current_playable_index"]
    ]

    return {
        "paused": ynison["player_state"]["status"]["paused"],
        "duration_ms": ynison["player_state"]["status"]["duration_ms"],
        "progress_ms": ynison["player_state"]["status"]["progress_ms"],
        "entity_id": ynison["player_state"]["player_queue"]["entity_id"],
        "entity_type": ynison["player_state"]["player_queue"]["entity_type"],
        "track": track["playable_id"],
    }

async def main():
    data = await get_current_track_beta(1, 'AgAAAAAhQE6EAAG8XjFziI-IakfflPp7Rlb6l_Q')
    track_id = data['track']
    client = Client('AgAAAAAhQE6EAAG8XjFziI-IakfflPp7Rlb6l_Q').init()
    track = client.tracks([track_id])
    print(track)

if __name__ == '__main__':
    asyncio.run(main())


