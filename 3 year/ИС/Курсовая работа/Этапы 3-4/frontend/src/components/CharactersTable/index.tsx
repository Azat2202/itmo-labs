import {
    RoomMessage,
    useGetAllFactsQuery,
    useGetApiMeQuery,
    useOpenFactMutation,
    User
} from "../../store/types.generated";

export function CharactersTable({roomData, canOpen, user}: { roomData?: RoomMessage, canOpen: boolean, user: User }) {
    const characterId = roomData?.characters
        ?.find(character => character?.user?.id === user?.id)
        ?.id ?? 0
    const {data: userFacts} = useGetAllFactsQuery({characterId: characterId})
    const userOpenFacts = roomData?.characters
        ?.find(character => character?.id === characterId)
        ?.openedFacts
    const [openFact] = useOpenFactMutation()

    return (
        <div className="text-burgundy-200 bg-burgundy-950 rounded-lg shadow-[0_10px_10px_rgba(0,0,0,0.5),_0_-3px_10px_rgba(0,0,0,0.5)] shadow-burgundy-500/30 mt-6 p-4">
            <div className="overflow-hidden rounded-lg border border-burgundy-850">
            <table className="table-auto w-full border-collapse border-4 border-burgundy-850 rounded-lg">
                <thead>
                <tr className="bg-burgundy-850">
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Имя игрока</th>
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Имя персонажа</th>
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Телосложение</th>
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Профессия</th>
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Здоровье</th>
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Хобби</th>
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Фобия</th>
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Особенность характера</th>
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Инструмент</th>
                    <th className="border border-burgundy-900 px-4 py-2 text-left font-semibold text-sm">Инвентарь</th>
                </tr>
                </thead>
                <tbody>
                {roomData?.characters?.map((character) => (
                    <tr className={`${character.isActive ? "bg-burgundy-800" : "line-through bg-black"}`}>
                        <td className="border border-burgundy-900 px-4 py-2">{character.user?.username}</td>
                        <td className="border border-burgundy-900 px-4 py-2">{character.name}</td>
                        <td className="border border-burgundy-900 px-4 py-2">{character.openedFacts?.bodyType}</td>
                        <td className="border border-burgundy-900 px-4 py-2">{character.openedFacts?.profession}</td>
                        <td className="border border-burgundy-900 px-4 py-2">{character.openedFacts?.health}</td>
                        <td className="border border-burgundy-900 px-4 py-2">{character.openedFacts?.hobby}</td>
                        <td className="border border-burgundy-900 px-4 py-2">{character.openedFacts?.phobia}</td>
                        <td className="border border-burgundy-900 px-4 py-2">{character.openedFacts?.trait}</td>
                        <td className="border border-burgundy-900 px-4 py-2">{character.openedFacts?.equipment}</td>
                        <td className="border border-burgundy-900 px-4 py-2">{character.openedFacts?.bag}</td>
                    </tr>
                ))}
                {canOpen && (
                    <tr className="bg-burgundy-850">
                        <td className="border border-burgundy-900 px-4 py-2 font-bold">Ваш персонаж:</td>
                        <td className="border border-burgundy-900 px-4 py-2">{userFacts?.name}</td>
                        <td className="border border-burgundy-900 px-4 py-2">
                            <button
                                onClick={() =>
                                    openFact({
                                        characterId: characterId,
                                        factType: "BODY_TYPE",
                                    })
                                }
                                className="my-b bg-burgundy-600  text-burgundy-100 font-semibold transition duration-300 transform hover:scale-105 px-3 py-1 rounded-md text-sm"
                            >
                                {userFacts?.bodyType}
                            </button>
                        </td>
                        <td className="border border-burgundy-900 px-4 py-2">
                            <button
                                onClick={() =>
                                    openFact({
                                        characterId: characterId,
                                        factType: "PROFESSION",
                                    })
                                }
                                className="my-b bg-burgundy-600  text-burgundy-100 font-semibold transition duration-300 transform hover:scale-105 px-3 py-1 rounded-md text-sm"
                            >
                                {userFacts?.profession}
                            </button>
                        </td>
                        <td className="border border-burgundy-900 px-4 py-2">
                            <button
                                onClick={() =>
                                    openFact({
                                        characterId: characterId,
                                        factType: "HEALTH",
                                    })
                                }
                                className="my-b bg-burgundy-600  text-burgundy-100 font-semibold transition duration-300 transform hover:scale-105 px-3 py-1 rounded-md text-sm"
                            >
                                {userFacts?.health}
                            </button>
                        </td>
                        <td className="border border-burgundy-900 px-4 py-2">
                            <button
                                onClick={() =>
                                    openFact({
                                        characterId: characterId,
                                        factType: "HOBBY",
                                    })
                                }
                                className="my-b bg-burgundy-600  text-burgundy-100 font-semibold transition duration-300 transform hover:scale-105 px-3 py-1 rounded-md text-sm"
                            >
                                {userFacts?.hobby}
                            </button>
                        </td>
                        <td className="border border-burgundy-900 px-4 py-2">
                            <button
                                onClick={() =>
                                    openFact({
                                        characterId: characterId,
                                        factType: "PHOBIA",
                                    })
                                }
                                className="my-b bg-burgundy-600  text-burgundy-100 font-semibold transition duration-300 transform hover:scale-105 px-3 py-1 rounded-md text-sm"
                            >
                                {userFacts?.phobia}
                            </button>
                        </td>
                        <td className="border border-burgundy-900 px-4 py-2">
                            <button
                                onClick={() =>
                                    openFact({
                                        characterId: characterId,
                                        factType: "TRAIT",
                                    })
                                }
                                className="my-b bg-burgundy-600  text-burgundy-100 font-semibold transition duration-300 transform hover:scale-105 px-3 py-1 rounded-md text-sm"
                            >
                                {userFacts?.trait}
                            </button>
                        </td>
                        <td className="border border-burgundy-900 px-4 py-2">
                            <button
                                onClick={() =>
                                    openFact({
                                        characterId: characterId,
                                        factType: "EQUIPMENT",
                                    })
                                }
                                className="my-b bg-burgundy-600  text-burgundy-100 font-semibold transition duration-300 transform hover:scale-105 px-3 py-1 rounded-md text-sm"
                            >
                                {userFacts?.equipment}
                            </button>
                        </td>
                        <td className="border border-burgundy-900 px-4 py-2">
                            <button
                                onClick={() =>
                                    openFact({
                                        characterId: characterId,
                                        factType: "BAG",
                                    })
                                }
                                className="my-b bg-burgundy-600  text-burgundy-100 font-semibold transition duration-300 transform hover:scale-105 px-3 py-1 rounded-md text-sm"
                            >
                                {userFacts?.bag}
                            </button>
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
            </div>
        </div>
    );
}