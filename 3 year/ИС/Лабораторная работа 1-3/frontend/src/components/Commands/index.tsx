import React, {useEffect, useState} from "react";
import {
    useCountGroupAdminQuery,
    useDeleteByExpelledMutation, useExpelEverybodyMutation, useGetAllPersonsQuery,
    useGetExpelledCountQuery,
    useMinGroupAdminQuery
} from "../../store/types.generated";
import toast from "react-hot-toast";
import {ShowStudyGroupModalWithData} from "../ShowStudyGroupModal";

export function CommandsPage() {
    const [shouldBeExpelled, setShouldBeExpelled] = useState(0);
    const [showStudyGroupModalOpen, setShowStudyGroupModalOpen] = useState(false);
    const [countGroupAdminId, setCountGroupAdminId] = useState<number | undefined>(undefined);
    const [expellGroupid, setExpellGroupId] = useState(1);

    const [deleteByShouldBeExpelled] = useDeleteByExpelledMutation();
    const {data: groupAdmins} = useGetAllPersonsQuery();
    const {data: minGroupAdmin, refetch: refetchMinGroupAdmin} = useMinGroupAdminQuery();
    const {
        data: countByGroupAdmin,
        refetch: refetchCountByGroupAdmin
    } = useCountGroupAdminQuery({groupAdminId: countGroupAdminId});
    const [expellEveryBody] = useExpelEverybodyMutation();
    const {data: expelledCount, refetch: updateExpelledCount} = useGetExpelledCountQuery();

    return <div className="pl-10 p-4">
        <h1 className="text-3xl font-bold">Основные команды</h1>
        <ul className="list-disc text-2xl">
            В системе должен быть реализован отдельный пользовательский интерфейс для выполнения специальных операций
            над объектами:
            <li>
                <button
                    className="underline text-blue-700"
                    onClick={() => {
                        deleteByShouldBeExpelled({expelled: shouldBeExpelled})
                            .unwrap()
                            .then(() => toast("Объект удален!", {icon: "🗑️"}))
                            .catch(() => toast("Таких объектов нет!", {icon: "❌"}))
                    }}
                >Удалить
                </button>
                {" "}один (любой) объект, значение поля shouldBeExpelled которого эквивалентно заданному.
                <input
                    type="number"
                    className="border-black border"
                    placeholder="shouldBeExpelled"
                    value={shouldBeExpelled}
                    onChange={e => setShouldBeExpelled(+e.target.value)}
                />
            </li>
            <li>
                <button
                    className="underline text-blue-700"
                    onClick={() => {
                        refetchMinGroupAdmin()
                            .then(() => setShowStudyGroupModalOpen(true))
                    }}
                >
                    Вернуть
                </button>
                {" "}один (любой) объект, значение поля groupAdmin которого является минимальным.
            </li>
            <li>
                <button
                    className="underline text-blue-700"
                    onClick={() => {
                        refetchCountByGroupAdmin();
                    }}
                >
                    Вернуть
                </button>
                {" "}количество объектов ({countByGroupAdmin}) , значение поля groupAdmin которых равно заданному.
                <select
                    name="Админ"
                    value={countGroupAdminId}
                    onChange={e =>
                    {
                        setCountGroupAdminId(e.target.value !== "Undefined" ? parseInt(e.target.value) : undefined)
                        refetchCountByGroupAdmin();
                    }}
                    className="border border-black"
                >
                    {
                        groupAdmins?.length ? (
                            groupAdmins.map((person, index) => (
                                <option key={index} value={person.id || "DEFAULT_VALUE"}>
                                    {person.name || "Unnamed"}
                                </option>
                            ))
                        ) : (
                            <option disabled>No persons available</option>
                        )

                    }
                    <option value={undefined}>
                        Undefined
                    </option>
                </select>
            </li>
            <li>
                <button
                    className="underline text-blue-700"
                    onClick={() => {
                        expellEveryBody({groupId: expellGroupid})
                            .unwrap()
                            .then(() => toast("Студенты отчислены!", {icon: "😢"}))
                            .catch(() => toast("Таких студентов нет!", {icon: "❌"}))
                    }}
                >
                    Отчислить
                </button>
                {" "}всех студентов указанной группы.
                <input
                    type="number"
                    className="border-black border"
                    placeholder="groupId"
                    value={expellGroupid}
                    onChange={e => setExpellGroupId(+e.target.value)}
                />
            </li>
            <li>
                <button
                    className="underline text-blue-700"
                    onClick={() => {
                        updateExpelledCount();
                    }}
                >
                    Посчитать
                </button>
                {" "}общее число отчисленных студентов во всех группах. ({expelledCount})
            </li>
        </ul>
        {minGroupAdmin && <ShowStudyGroupModalWithData
            isModalOpen={showStudyGroupModalOpen}
            closeModal={() => setShowStudyGroupModalOpen(false)}
            data={minGroupAdmin}
        />}
    </div>
}