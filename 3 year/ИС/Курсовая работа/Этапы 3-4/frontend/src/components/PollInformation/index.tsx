import {
    PollMessage,
    RoomMessage,
    useCreatePoolMutation,
    useGetPollsQuery,
    User,
    useVoteMutation
} from "../../store/types.generated";
import {CSSTransition} from "react-transition-group";
import {useEffect, useState} from "react";
import toast from "react-hot-toast";
import "./PollResults.css";
import {appConfig} from "../../index";

function ShowPoll({poll, roomData, user}: { poll: PollMessage, roomData?: RoomMessage, user: User }) {
    const liveCharacters = roomData?.characters?.filter(character => character?.isActive)
    const [selectedCharacterId, setSelectedCharacterId] = useState<number | null>(null);
    const [vote] = useVoteMutation();
    const handleChoose = (characterId: number) => {
        setSelectedCharacterId(characterId);
    };

    const handleVote = () => {
        if (selectedCharacterId !== null) {
            vote({roomId: roomData?.id!!, characterId: selectedCharacterId}).unwrap()
                .then(() => toast.success("Голос учтен"))
                .catch(() => toast.error("Ошибка голосования"));
        }
    };

    const estimatedTimeSeconds = (
        (new Date(poll.creationTime!!).getTime() + 1000 * appConfig["pollTime"] - Date.now()) / 1000).toFixed(0)

    return (
        <div className="text-burgundy-200 bg-burgundy-800 rounded-lg shadow-[0_10px_10px_rgba(0,0,0,0.5),_0_-3px_10px_rgba(0,0,0,0.5)] shadow-burgundy-500/30 p-4 items-center flex-col flex">
            <p className="text-lg font-semibold text-burgundy-200 mb-4">
                Раунд {poll.roundNumber}
            </p>
            <p>Осталось: {estimatedTimeSeconds} секунд</p>
            {liveCharacters
                ?.filter((character) => character?.user?.id !== user.id)
                .map((character) => (
                    <div key={character?.id} className="mb-2 flex items-center">
                        <input
                            type="radio"
                            id={`character-${character?.id}`}
                            name="character"
                            value={character?.id}
                            onChange={() => handleChoose(character?.id!!)}
                            checked={selectedCharacterId === character?.id}
                            className="mr-2 accent-burgundy-500"
                        />
                        <label
                            htmlFor={`character-${character?.id}`}
                            className="text-burgundy-200 cursor-pointer"
                        >
                            {character?.name ?? character?.user?.username}
                        </label>
                    </div>
                ))}
            <button
                onClick={handleVote}
                disabled={selectedCharacterId === null}
                className={`relative mt-4 px-4 py-2 rounded-md text-burgundy-100 disabled:opacity-50 disabled:bg-burgundy-950 disabled:cursor-not-allowed disabled:rounded-lg ${
                    selectedCharacterId === null
                        ? "bg-gray-400 cursor-not-allowed"
                        : "bg-burgundy-600 hover:bg-burgundy-700 transition"
                }`}
            >
                Проголосовать
                {selectedCharacterId === null && (
                    <span className="absolute inset-0 rounded-lg bg-burgundy-300 opacity-50 pointer-events-none"
                          style={{
                              backgroundImage: 'linear-gradient(135deg, rgba(79, 50, 61, 0.5) 25%, transparent 25%, transparent 75%, rgba(79, 50, 61, 0.5) 75%)',
                              backgroundSize: '10px 10px',
                          }}/>
                )}
            </button>
        </div>
    );
}

function getKeyAndCount<T>(arr: T[]) {
    {
        return arr.reduce((accumulator, value) => {
            accumulator.set(value, ((accumulator.get(value) || 0) + 1));
            return accumulator;
        }, new Map<T, number>());
    }
}

export function PollResults({polls, roomData}: { polls: PollMessage[], roomData?: RoomMessage }) {
    const [showResults, setShowResults] = useState(false);
    const liveCharacters = roomData?.characters?.filter(character => character?.isActive);

    const targetAndVoteCount = (id: number) => Array.from(
        getKeyAndCount(
            polls.at(id)?.votes?.map(vote =>
                vote.targetCharacter?.name) ?? [])
    )

    return (
        <div className="text-burgundy-200 bg-burgundy-950 rounded-lg shadow-[0_10px_10px_rgba(0,0,0,0.5),_0_-3px_10px_rgba(0,0,0,0.5)] shadow-burgundy-500/30 p-4">
            <button
                onClick={() => setShowResults(!showResults)}
                className="mb-4 px-4 py-2 bg-burgundy-500 text-burgundy-200 rounded-md hover:bg-burgundy-600 transition"
            >
                {showResults ? "Скрыть результаты прошлых опросов" : "Показать результаты прошлых опросов"}
            </button>
            <CSSTransition
                in={showResults}
                timeout={1000}
                classNames="results"
                unmountOnExit
            >
                <div className="mt-4 space-y-4">
                    {polls.map((poll, id) => (
                        <div key={id}>
                            <h3 className="text-lg font-semibold text-burgundy-300">
                                Раунд {poll.roundNumber}:
                            </h3>
                            <div className="mt-2 space-y-2">
                                {targetAndVoteCount(id).map((targetCharacter, idx) => (
                                    <p key={idx} className="text-burgundy-200">
                                        {targetCharacter[0]} - {targetCharacter[1]} голосов
                                    </p>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>
            </CSSTransition>
        </div>
    );
}

export function PollInformation({roomData, canOpen, user}: { roomData?: RoomMessage, canOpen: boolean, user: User }) {
    const [createPoll] = useCreatePoolMutation()
    const {data: polls, refetch: refetchPolls} = useGetPollsQuery({roomId: roomData?.id!!})
    const openPoll = polls?.find(poll => poll.isOpen)

    useEffect(() => {
        const intervalId = setInterval(refetchPolls, 200)
        return () => clearInterval(intervalId)
    }, [refetchPolls])

    async function createPollHandler() {
        await createPoll({roomId: roomData?.id!!}).unwrap()
            .then(() => toast.success("Опрос начат"))
            .catch(() => toast.error("Вы не можете начать опрос. Дождитесь других игроков"))
    }

    // const isAdmin = roomData?.characters?.at(-1)?.user?.id === user.id
    return (
        <div className="p-6 text-burgundy-200 items-center flex flex-col">
            {canOpen && (
                <button
                    onClick={createPollHandler}
                    className="px-4 py-2 bg-burgundy-500 text-burgundy-100 rounded-md hover:bg-burgundy-600 transition"
                >
                    НАЧАТЬ ОПРОС
                </button>
            )}
            {openPoll && (
                <div className="mt-6">
                    <ShowPoll poll={openPoll} roomData={roomData} user={user} />
                </div>
            )}
            {polls && (
                <div className="mt-6">
                    <PollResults polls={polls} />
                </div>
            )}
        </div>
    );
}