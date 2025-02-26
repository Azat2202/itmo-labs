import {useGetApiMeQuery, useGetRoomStateQuery} from "../../store/types.generated";
import {ClosedRoom} from "./ClosedRoom";
import {NewRoom} from "./NewRoom";
import {RunningRoom} from "./RunningRoom";
import {useEffect} from "react";
import {useParams} from "react-router-dom";

export function RoomPage() {
    const { roomId: roomIdStr } = useParams<{ roomId: string }>()
    const roomId = Number(roomIdStr)
    const {data: user} = useGetApiMeQuery()
    const {data: room, refetch: refetchRoom} = useGetRoomStateQuery({roomId: roomId})
    useEffect(() => {
        const intervalId = setInterval(refetchRoom, 300)
        return () => clearInterval(intervalId)
    }, [refetchRoom])
    if (room?.isClosed || !room?.characters
        ?.find(character => character.user?.id === user?.id)
        ?.isActive) return <ClosedRoom/>
    if (!room?.isStarted) return <NewRoom/>
    return <RunningRoom/>
}