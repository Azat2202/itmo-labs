import {useGetApiMeQuery, useGetRoomStateQuery, useStartGameMutation} from "../../../store/types.generated";
import {useEffect} from "react";
import {SelectCharacteristics} from "../../../components/SelectCharacteristics";
import toast from "react-hot-toast";
import {Link, useParams} from "react-router-dom";

export function NewRoom() {
    const { roomId: roomIdStr } = useParams<{ roomId: string }>()
    const roomId = Number(roomIdStr)
    const {data: room, refetch: refetchRoom} = useGetRoomStateQuery({roomId: Number(roomId)})
    const [startGame,] = useStartGameMutation()

    useEffect(() => {
        const intervalId = setInterval(refetchRoom, 1000)
        return () => clearInterval(intervalId)
    }, [refetchRoom])

    async function startGameButtonHandler() {
        await startGame({roomId: roomId}).unwrap()
            .then(() => toast.success("Игра начата!"))
            .catch(() => toast.error("Игру начать не удалось! Слишком мало участников или персонажи еще не созданы!"))
    }

    return (
        <div className="min-h-screen bg-burgundy-900 text-burgundy-200 flex flex-col items-center justify-center p-8">
            <header className="w-full text-center mb-6">
                <h1 className="text-4xl font-bold text-burgundy-200">Добро пожаловать в комнату</h1>
                <Link to={"/main"}>
                    <button
                        className="bg-burgundy-950 hover:bg-burgundy-700 text-burgundy-200 font-bold py-2 px-6 rounded-lg transition duration-300 border-2 border-burgundy-200">
                        Вернуться на главную
                    </button>
                </Link>
            </header>

            <div className="bg-burgundy-950 p-6 rounded-lg shadow-[0_10px_10px_rgba(0,0,0,0.5),_0_-3px_10px_rgba(0,0,0,0.5)] shadow-burgundy-500/30 w-full max-w-xl mb-8">
                <p className="text-xl text-burgundy-200 mb-2">
                    Код приглашения:
                    <span className="text-burgundy-500 font-semibold"> {room?.joinCode}</span>
                </p>

                <div className="mb-4">
                    <p className="text-lg text-burgundy-200">Список текущих игроков:</p>
                    <ul className="space-y-2 list-disc">
                        {room?.characters?.map((character, index) => (
                            <li key={index} className="text-burgundy-200">{character.user?.username}</li>
                        ))}
                    </ul>
                </div>
            </div>

            <div className="bg-burgundy-950 p-6 rounded-lg shadow-[0_10px_10px_rgba(0,0,0,0.5),_0_-3px_10px_rgba(0,0,0,0.5)] shadow-burgundy-500/30 w-full max-w-7xl mb-8 items-center">
                <p className="text-xl text-burgundy-200 mb-4 text-center">Создайте своего персонажа</p>
                <SelectCharacteristics />
            </div>

            <button
                onClick={startGameButtonHandler}
                type="button"
                className="w-full max-w-7xl py-3 mt-6 bg-burgundy-600 text-burgundy-100 font-semibold text-xl rounded-lg transition duration-300 transform hover:scale-105"
            >
                НАЧАТЬ ИГРУ
            </button>
        </div>
    );
}