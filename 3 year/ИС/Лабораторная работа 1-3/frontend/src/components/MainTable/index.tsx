import {useDispatch, useSelector} from "react-redux";
import {
    UpdateStudyGroupRequest,
    useAuthenticateMutation,
    useDeleteStudyGroupMutation, useExpelEverybodyMutation,
    useGetAllStudyGroupsQuery, useMeQuery
} from "../../store/types.generated";
import React, {useEffect, useRef, useState} from "react";
import {CreateStudyGroupModal} from "../CreateStudyGroupModal";
import {RootState} from "../../store/store";
import toast from "react-hot-toast";
import {UpdateStudyGroupModal} from "../UpdateStudyGroupModal";
import {ShowStudyGroupModal} from "../ShowStudyGroupModal";


export function MainTable() {
    const [hoveredCell, setHoveredCell] = useState<{ row: number; col: string } | null>(null);
    const [isStudyGroupModalOpen, setIsStudyGroupModalOpen] = useState(false);
    const [isUpdateStudyGroupModalOpen, setIsUpdateStudyGroupModalOpen] = useState(false);
    const [showStudyGroupModalOpen, setIsShowStudyGroupModalOpen] = useState(false);
    const userLogin = useSelector((state: RootState) => state.auth.login);
    const [page, setPage] = useState(0);
    const filterRef = useRef<HTMLInputElement>(null);
    const adminRef = useRef<HTMLInputElement>(null);
    const [groupName, setGroupName] = useState("");
    const [sortBy, setSortBy] = useState("");
    const [groupAdmin, setGroupAdmin] = useState("");
    const [semester, setSemester] = useState<"FIRST" | "SECOND" | "SEVENTH" | "EIGHTH">();
    const [formOfEducation, setFormOfEducation] = useState<"DISTANCE_EDUCATION"
        | "FULL_TIME_EDUCATION"
        | "EVENING_CLASSES">();
    const [sortDirection, setSortDirection] = useState("asc");

    const dispatch = useDispatch();
    const {data: user} = useMeQuery()
    const {data, error, isLoading, isSuccess, isError, refetch: refetchCollection} = useGetAllStudyGroupsQuery({
        page,
        groupName,
        adminName: groupAdmin,
        formOfEducation,
        semester,
        sortBy,
        sortDirection
    });
    const [updateGroup, setUpdateGroup] = useState<UpdateStudyGroupRequest>();
    const [deleteStudyGroup] = useDeleteStudyGroupMutation();
    const [expellEveryBody] = useExpelEverybodyMutation();
    useEffect(() => {
        const intervalId = setInterval(refetchCollection, 1000)
        return () => clearInterval(intervalId)
    }, [refetchCollection])
    if (isLoading) {
        return <div>Loading...</div>;

    }
    if (isError) {
        return <div>Error</div>;
    }

    if (isSuccess && data) {
        return (
            <div className="pl-10 p-4">
                <div className="text-xl">
                    <h1 className="text-3xl font-bold">Введение</h1>
                    <ul className="list-disc">
                        <li>Основное назначение информационной системы - управление объектами, созданными на основе
                            заданного в варианте класса.
                        </li>
                        <li>Необходимо, чтобы с помощью системы можно было выполнить следующие операции с объектами:
                            <button onClick={() => setIsStudyGroupModalOpen(true)}
                                    className="underline text-blue-800"> создание нового объекта</button>,
                            <button onClick={() => setIsShowStudyGroupModalOpen(true)}
                                    className="underline text-blue-800"> получение информации об объекте по ИД</button>,
                            обновление объекта
                            (модификация его атрибутов), удаление
                            объекта. Операции должны осуществляться в отдельных окнах (интерфейсах) приложения.При
                            получении
                            информации
                            об объекте класса должна также выводиться информация о связанных с ним объектах.
                        </li>
                        <li>На главном экране системы должен выводиться список текущих объетов в виде таблицы (каждый
                            атрибут объекта
                            - отдельная колонка в таблице). При отображении таблицы должна использоваться пагинация
                            (если
                            все объекты не
                            помещаются на одном экране).
                        </li>
                        <li>Нужно обеспечить возможность фильтровать
                            (<input type={"text"}
                                    onChange={e => {
                                        setGroupName(e.target.value)
                                        refetchCollection()
                                    }
                                    }
                                    value={groupName}
                                    name={"groupName"}
                                    placeholder={"Имя группы"}
                                    className="border border-black"
                            />)
                            сортировать строки таблицы
                            (<select
                                name="sortBy"
                                value={sortBy}
                                onChange={(e) => {
                                    setSortBy(e.target.value);
                                }}
                                className="border border-black"
                            >
                                <option value="id">ID</option>
                                <option value="name">Имя</option>
                                <option value="coordinates">Координаы</option>
                                <option value="studentsCount">Количество студентов</option>
                                <option value="expelledStudents">Отличсленные студенты</option>
                                <option value="transferredStudents">Переведенные студенты</option>
                                <option value="formOfEducation">Форма обучения</option>
                                <option value="shouldBeExpelled">На отчисление</option>
                                <option value="semester">Семестр</option>
                                <option value="groupAdmin">Админ</option>
                            </select>)
                            (<select
                                name="direction"
                                value={sortDirection}
                                onChange={(e) => {
                                    setSortDirection(e.target.value);
                                }}
                                className="border border-black"
                            >
                                <option value="asc">По возрастанию</option>
                                <option value="desc">По убыванию</option>
                            </select>)

                            которые показывают объекты
                            (по
                            значениям любой из строковых колонок). Фильтрация элементов должна производиться по
                            неполному
                            совпадению.
                        </li>
                        <li>Переход к обновлению (модификации) объекта должен быть возможен из таблицы с общим списком
                            объектов и из
                            области с визуализацией объекта (при ее реализации).
                        </li>
                    </ul>
                </div>
                <table className="table-auto text border-collapse border-2 border-black m-2">
                    <thead className="text-center">
                    <tr>
                        <th className="border border-black p-2">Id</th>
                        <th className="border border-black p-2">Имя</th>
                        <th className="border border-black p-2">Координаты</th>
                        <th className="border border-black p-2">Дата</th>
                        <th className="border border-black p-2">Количество студентов</th>
                        <th className="border border-black p-2">Отчисленные</th>
                        <th className="border border-black p-2">Переведенные</th>
                        <th className="border border-black p-2">Форма обучения</th>
                        <th className="border border-black p-2">На отчисление</th>
                        <th className="border border-black p-2">Семестр</th>
                        <th className="border border-black p-2">Админ</th>
                        <th className="border border-black p-2">Владелец объекта</th>
                        <th className="border border-black p-2">Обновить</th>
                        <th className="border border-black p-2">Удалить</th>
                        <th className="border border-black p-2">Отчислить всех</th>
                    </tr>
                    </thead>
                    <tbody>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"><input type={"text"}
                                                                               onChange={e => {
                                                                                   setGroupName(e.target.value)
                                                                                   refetchCollection()
                                                                                       .then(() => filterRef.current ? filterRef.current.focus() : {})
                                                                               }
                                                                               }
                                                                               value={groupName}
                                                                               name={"groupName"}
                                                                               placeholder={"Имя группы"}
                                                                               className="border border-black w-20"
                                                                               ref={filterRef}
                    /></td>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"><select
                        name="formOfEducation"
                        value={formOfEducation}
                        onChange={e => setFormOfEducation(e.target.value === undefined ? undefined : e.target.value as "DISTANCE_EDUCATION" | "FULL_TIME_EDUCATION" | "EVENING_CLASSES")}
                        className="flex-grow p-2 border border-black "
                    >
                        <option value={undefined}></option>
                        <option value="DISTANCE_EDUCATION">Заочное обучение</option>
                        <option value="FULL_TIME_EDUCATION">Дневное обучение</option>
                        <option value="EVENING_CLASSES">Вечернее обучение</option>
                    </select></td>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"><select
                        name="semester"
                        value={semester}
                        onChange={e =>
                            setSemester(e.target.value === undefined ? undefined : e.target.value as "FIRST" | "SECOND" | "SEVENTH" | "EIGHTH")}
                        className="flex-grow p-2 border border-black "
                    >
                        <option value={undefined}></option>
                        <option value="FIRST">Первый</option>
                        <option value="SECOND">Второй</option>
                        <option value="SEVENTH">Седьмой</option>
                        <option value="EIGHTH">Восьмой</option>
                    </select></td>
                    <td className="border border-black p-1 text-center"><input type={"text"}
                                                                               onChange={e => {
                                                                                   setGroupAdmin(e.target.value)
                                                                                   refetchCollection()
                                                                                       .then(() => adminRef.current ? adminRef.current.focus() : {})
                                                                               }
                                                                               }
                                                                               value={groupAdmin}
                                                                               name={"groupAdmin"}
                                                                               placeholder={"Имя админа"}
                                                                               className="border border-black w-20"
                                                                               ref={adminRef}
                    /></td>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"></td>
                    <td className="border border-black p-1 text-center"></td>
                    {data.content?.map((group, index) => (
                        <tr key={group.id}>
                            <td className="border border-black p-1 text-center">{group.id}</td>
                            <td className="border border-black p-1 text-center">{group.name}</td>
                            <td className="border border-black p-1 text-center">{
                                group.coordinates ? `(${group.coordinates.x}, ${group.coordinates.y})` : 'N/A'}</td>
                            <td className="border border-black p-1 text-center">{
                                group.creationDate ? new Date(group.creationDate).toDateString() : 'N/A'}</td>
                            <td className="border border-black p-1 text-center">{group.studentsCount}</td>
                            <td className="border border-black p-1 text-center">{group.expelledStudents}</td>
                            <td className="border border-black p-1 text-center">{group.transferredStudents}</td>
                            <td className="border border-black p-1 text-center">{group.formOfEducation}</td>
                            <td className="border border-black p-1 text-center">{group.shouldBeExpelled}</td>
                            <td className="border border-black p-1 text-center">{group.semester}</td>
                            <td className="border border-black p-1 text-center relative"
                                onMouseEnter={() => setHoveredCell({row: index, col: 'id'})}
                                onMouseLeave={() => setHoveredCell(null)}
                            >
                                {group.groupAdmin?.name}
                                {hoveredCell?.row === index && (
                                    <div
                                        className="absolute flex flex-col left-0 top-full w-40 mt-1 p-2 bg-white border border-black rounded shadow-lg z-10">
                                        <div> id: {group?.groupAdmin?.id} </div>
                                        <div> Имя: {group?.groupAdmin?.name} </div>
                                        <div> Цвет волос: {group?.groupAdmin?.hairColor} </div>
                                        <div> Цвет глаз: {group?.groupAdmin?.eyeColor}</div>
                                        <div> Локация: {group?.groupAdmin?.location
                                            ? `(${group.groupAdmin.location.x};${group.groupAdmin.location.y};${group.groupAdmin.location.z})`
                                            : 'N/A'} </div>
                                        <div> Национальность: {group?.groupAdmin?.nationality}</div>
                                        <div> Вес: {group?.groupAdmin?.weight}</div>
                                    </div>
                                )}
                            </td>
                            <td className="border border-black p-1 text-center">{group.user ? group.user.username : 'N/A'}</td>
                            <td className="border border-black p-1 text-center">{(
                                user?.role == "ADMIN" || group.user?.username == userLogin)
                            && group.isEditable ?
                                <button onClick={() => {
                                    setUpdateGroup({
                                        id: group.id!!,
                                        name: group.name ?? "",
                                        coordinatesId: group.coordinates?.id ?? 0,
                                        studentsCount: group.studentsCount ?? 0,
                                        expelledStudents: group.expelledStudents ?? 0,
                                        transferredStudents: group.transferredStudents ?? 0,
                                        formOfEducation: group.formOfEducation ?? "FULL_TIME_EDUCATION",
                                        semester: group.semester ?? "FIRST",
                                        shouldBeExpelled: group.shouldBeExpelled,
                                        groupAdminId: group.groupAdmin?.id,
                                    })
                                    setIsUpdateStudyGroupModalOpen(true)
                                }} className="underline text-blue-800">Обновить</button> :
                                ""} </td>
                            <td className="border border-black p-1 text-center">{(
                                user?.role == "ADMIN" || group.user?.username == userLogin)
                            && group.id ?
                                <button onClick={() => {
                                    deleteStudyGroup({id: group.id ?? 0})
                                        .then(() => {
                                            refetchCollection();
                                        })
                                }} className="underline text-blue-800">Удалить</button> : ""
                            }</td>
                            <td className="border border-black p-1 text-center">{(
                                user?.role == "ADMIN" || group.user?.username == userLogin)
                            && group.id ?
                                <button onClick={() => {
                                    expellEveryBody({groupId: group.id ?? 0})
                                        .then(() => {
                                            refetchCollection();
                                        })
                                }} className="underline text-blue-800">Отчислить всех</button> : ""
                            }</td>
                        </tr>
                    ))}
                    </tbody>
                    <tfoot>
                    {!data.first &&
                        <button onClick={() => setPage(page - 1)}
                                className="m-1 p-1 w-6 border-2 font-bold border-black rounded transition hover:bg-gray-200"
                        >{"<"}</button>
                    }
                    {!data.last &&
                        <button onClick={() => setPage(page + 1)}
                                disabled={page >= (data?.totalPages ? data?.totalPages : 0) - 1}
                                className="m-1 p-1 w-6 border-2 font-bold border-black rounded transition hover:bg-gray-200"
                        >{">"}</button>
                    }
                    </tfoot>
                </table>
                <CreateStudyGroupModal
                    closeModal={() => {
                        setIsStudyGroupModalOpen(false);
                        refetchCollection();
                    }}
                    isModalOpen={isStudyGroupModalOpen}
                    isEditable={false}
                />
                {(isUpdateStudyGroupModalOpen && updateGroup) && <UpdateStudyGroupModal
                    group={updateGroup}
                    closeModal={() => {
                        setIsUpdateStudyGroupModalOpen(false);
                        refetchCollection();
                    }}
                    isModalOpen={isUpdateStudyGroupModalOpen}
                    isEditable={false}
                />}
                {showStudyGroupModalOpen &&
                    <ShowStudyGroupModal isModalOpen={showStudyGroupModalOpen} closeModal={() => {
                        setIsShowStudyGroupModalOpen(false)
                    }}/>}
            </div>
        );
    }

    return null;
}