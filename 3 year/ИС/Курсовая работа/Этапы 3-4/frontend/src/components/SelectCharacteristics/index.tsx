import {useCreateCharacterMutation, useGenerateFactQuery} from "../../store/types.generated";
import {useState} from "react";
import toast from "react-hot-toast";
import {useParams} from "react-router-dom";

interface characteristicChooseProps {
    options: string[] | undefined,
    selectedId: number,
    onSelect: (id: number) => void,
    name: string
}

function CharacteristicsChoose({options, selectedId, onSelect, name}: characteristicChooseProps) {
    return (
        <div className="flex flex-col space-y-2 bg-burgundy-700 p-4 rounded-lg shadow-md w-full">
            <p className={"text-center"}>{name}</p>
            {options?.map((option, index) => (
                <div
                    key={index}
                    className={`p-3 rounded-lg border-spacing-1 border-burgundy-900 cursor-pointer text-center transition duration-300 ${
                        index === selectedId
                            ? "bg-burgundy-900 text-burgundy-200"
                            : "bg-burgundy-800 hover:bg-burgundy-850 text-burgundy-100"
                    }`}
                    onClick={() => onSelect(index)}
                >
                    {option}
                </div>
            ))}
        </div>
    );
}

export function SelectCharacteristics() {
    const {roomId: roomIdStr} = useParams<{ roomId: string }>()
    const roomId = Number(roomIdStr)
    const {data: characteristics} = useGenerateFactQuery({roomId: roomId})
    const [saveCharacteristics] = useCreateCharacterMutation()
    const [name, setName] = useState("")
    const [bodyTypeIdx, setbodyTypeIdx] = useState(1)
    const [healthIdx, setHealthIdx] = useState(1)
    const [traitIdx, setTraitIdx] = useState(1)
    const [hobbyIdx, setHobbyIdx] = useState(1)
    const [professionIdx, setProfessionIdx] = useState(1)
    const [phobiaIdx, setPhobiaIdx] = useState(1)
    const [equipmentIdx, setEquipmentIdx] = useState(1)
    const [bagIdx, setBagIdx] = useState(1)

    const characteristicsList = [
        {
            value: characteristics?.bodyTypes,
            selectedId: bodyTypeIdx,
            setter: setbodyTypeIdx,
            characteristicName: "Телосложение"
        },
        {value: characteristics?.healths, selectedId: healthIdx, setter: setHealthIdx, characteristicName: "Здоровье"},
        {
            value: characteristics?.traits,
            selectedId: traitIdx,
            setter: setTraitIdx,
            characteristicName: "Черта характера"
        },
        {value: characteristics?.hobbies, selectedId: hobbyIdx, setter: setHobbyIdx, characteristicName: "Хобби"},
        {
            value: characteristics?.professions,
            selectedId: professionIdx,
            setter: setProfessionIdx,
            characteristicName: "Профессия"
        },
        {value: characteristics?.phobiases, selectedId: phobiaIdx, setter: setPhobiaIdx, characteristicName: "Фобия"},
        {
            value: characteristics?.equipments,
            selectedId: equipmentIdx,
            setter: setEquipmentIdx,
            characteristicName: "Предмет"
        },
        {value: characteristics?.bags, selectedId: bagIdx, setter: setBagIdx, characteristicName: "Инвентарь"}
    ].map(({value, selectedId, setter, characteristicName}) => {
        return {
            value: value ?? [],
            selectedId: selectedId,
            setter: setter,
            characteristicName: characteristicName
        }
    })

    const levelSum = characteristicsList
        .map(({value, selectedId}) => value.at(selectedId)?.level ?? 0)
        .reduce((a, b) => a + b, 0)

    async function selectCharacteristics() {
        await saveCharacteristics({
            createCharacterRequest: {
                name: name,
                notes: "",
                bodyTypeId: characteristics?.bodyTypes?.at(bodyTypeIdx)?.id ?? 0,
                healthId: characteristics?.healths?.at(healthIdx)?.id ?? 0,
                traitId: characteristics?.traits?.at(traitIdx)?.id ?? 0,
                hobbyId: characteristics?.hobbies?.at(hobbyIdx)?.id ?? 0,
                professionId: characteristics?.professions?.at(professionIdx)?.id ?? 0,
                phobiaId: characteristics?.phobiases?.at(phobiaIdx)?.id ?? 0,
                equipmentId: characteristics?.equipments?.at(equipmentIdx)?.id ?? 0,
                bagId: characteristics?.bags?.at(bagIdx)?.id ?? 0,
                roomId: roomId
            }
        }).unwrap()
            .then(() => toast.success("Персонаж сохранен!"))
            .catch(e => toast.error("Создать персонажа не удалось"))
    }

    return (
        <div className="flex flex-col items-center space-y-6">
            <input
                type="text"
                value={name}
                onChange={e => setName(e.target.value)}
                className="w-full max-w-md p-3 placeholder:text-burgundy-400 text-burgundy-950 bg-burgundy-200 rounded-lg border-2 border-burgundy-700 focus:outline-none focus:ring-2 focus:ring-burgundy-400"
                placeholder="Введите имя персонажа"
            />

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 w-full max-w-5xl">
                {characteristicsList.map((characteristic, index) => (
                    <CharacteristicsChoose
                        key={index}
                        options={characteristic.value.map(v => v.name ?? "СЕКРЕТ")}
                        selectedId={characteristic.selectedId}
                        onSelect={characteristic.setter}
                        name={characteristic.characteristicName}
                    />
                ))}
            </div>
            <p>Текущая сумма характеристик: {levelSum} (должно быть 0)</p>
            <p>{name.trim().length === 0 ? "Имя пустое!" : ""}</p>

            <button
                onClick={selectCharacteristics}
                className="py-3 px-6 bg-burgundy-600  text-burgundy-100 font-semibold text-md rounded-lg transition duration-300 transform hover:scale-105 disabled:opacity-50 disabled:bg-burgundy-950 disabled:cursor-not-allowed disabled:rounded-lg"
                disabled={levelSum !== 0 || name.trim().length === 0}
            >
                СОЗДАТЬ
                {(levelSum !== 0 || name.trim().length === 0) && (
                    <span className="absolute inset-0 rounded-lg bg-burgundy-300 opacity-50 pointer-events-none cursor-not-allowed hover:scale-100"
                          style={{
                              backgroundImage: 'linear-gradient(135deg, rgba(79, 50, 61, 0.5) 25%, transparent 25%, transparent 75%, rgba(79, 50, 61, 0.5) 75%)',
                              backgroundSize: '10px 10px',
                          }}/>
                )}
            </button>
        </div>
    );
}