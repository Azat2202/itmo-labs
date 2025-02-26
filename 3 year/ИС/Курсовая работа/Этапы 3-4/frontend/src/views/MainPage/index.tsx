import {FormEvent, useState} from "react";
import toast from "react-hot-toast";
import {useCreateRoomMutation, useJoinRoomMutation} from "../../store/types.generated";
import {Link, useNavigate} from "react-router-dom";

export function MainPage() {
    const [code, setCode] = useState("")
    const [createRoom] = useCreateRoomMutation()
    const [joinRoom] = useJoinRoomMutation()
    const navigate = useNavigate()

    async function createGame() {
        createRoom().unwrap()
            .then(response => navigate(`/room/${response.id}`))
            .catch(() => toast.error("Создать комнату не удалось..."))
    }

    async function enterGame(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        if (code.trim().length === 0) {
            toast.error("Заполните код комнаты!")
            return
        }
        await joinRoom({joinCode: code}).unwrap()
            .then(response => navigate(`/room/${response.id}`))
            .catch(() => toast.error("Такой комнаты не существует либо игра уже началась!"))
    }

    return (
        <div className="min-h-screen bg-burgundy-900 flex flex-col items-center justify-center text-white">

            <main className="flex flex-col lg:flex-row items-center justify-center space-y-6 lg:space-y-0 lg:space-x-6 mt-12 p-6 w-full max-w-5xl min-h-80 bg-burgundy-900 rounded-xl shadow-lg">

                <div className="w-full lg:w-1/2 min-h-80 bg-burgundy-950 p-6 rounded-lg shadow-[0_10px_10px_rgba(0,0,0,0.5),_0_-3px_10px_rgba(0,0,0,0.5)] shadow-burgundy-500/30 flex flex-col items-center justify-between space-y-4">
                    <p className="text-2xl text-burgundy-200">Создать новую игру</p>
                    <div className="flex-grow flex items-center justify-center w-1/2 h-20 py-3">
                        <button
                            onClick={createGame}
                            className="w-full h-20 py-3 bg-burgundy-700  text-burgundy-200 font-semibold text-xl rounded-lg transition duration-300 transform hover:scale-105"
                        >
                            СОЗДАТЬ
                        </button>
                    </div>
                </div>

                <div
                    className="w-full lg:w-1/2 min-h-80 bg-burgundy-950 p-6 rounded-lg shadow-[0_10px_10px_rgba(0,0,0,0.5),_0_-3px_10px_rgba(0,0,0,0.5)] shadow-burgundy-500/30 flex flex-col items-center space-y-4">
                    <p className="text-2xl text-burgundy-200">Присоединиться к игре</p>
                    <div className="flex-grow flex items-center justify-center w-1/2 h-20 py-3">
                        <form onSubmit={enterGame} className="w-full space-y-4 items-center">
                            <div className="flex flex-col w-full">
                                <input
                                    type="text"
                                    value={code}
                                    onChange={e => setCode(e.target.value)}
                                    placeholder="Введите код комнаты"
                                    className="p-3 w-full placeholder:text-burgundy-400 text-burgundy-950 bg-burgundy-200 rounded-lg border border-burgundy-700 focus:outline-none focus:ring-2 focus:ring-burgundy-400"
                                />
                            </div>
                            <button
                                type="submit"
                                className="w-full h-20 py-3 bg-burgundy-700 text-burgundy-200 font-semibold text-xl rounded-lg transition duration-300 transform hover:scale-105"
                            >
                                ВОЙТИ В ИГРУ
                            </button>
                        </form>
                    </div>
                </div>
            </main>
            <Link to={"/"}>
                <button
                    className="bg-burgundy-950 hover:bg-burgundy-700 text-burgundy-200 font-bold py-2 px-6 rounded-lg transition duration-300 border-2 border-burgundy-200">
                    Вернуться на главную
                </button>
            </Link>
        </div>
);
}