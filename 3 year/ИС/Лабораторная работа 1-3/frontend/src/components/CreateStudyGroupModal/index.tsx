import Modal from "../../containers/Modal";
import React, {useEffect, useState} from "react";
import {
    StudyGroupRequest, useCreateStudyGroupMutation,
    useGetAllCoordinatesQuery,
    useGetAllPersonsQuery
} from "../../store/types.generated";
import {CreateCoordinatesModal} from "../CreateCoordinatesModal";
import {CreatePersonModal} from "../CreatePersonModal";
import toast from "react-hot-toast";

interface CreateStudyGroupModalInput {
    isModalOpen: boolean,
    closeModal: () => void,
    isEditable: boolean,
}

export function CreateStudyGroupModal({isModalOpen, closeModal, isEditable}: CreateStudyGroupModalInput) {
    const [isCoordinatesModalOpen, setIsCoordinatesModalOpen] = useState(false);
    const [isPersonModalOpen, setIsPersonModalOpen] = useState(false);
    const [formName, setFormName] = useState<string>("P3316");
    const [formCoordinatesId, setFormCoordinatesId] = useState<string>("1");
    const [formStudentsCount, setFormStudentsCount] = useState<string>("16");
    const [formExpelledStudents, setFormExpelledStudents] = useState<string>("5");
    const [formTransferredStudents, setFormTransferredStudents] = useState<string>("2");
    const [formFormOfEducation, setFormFormOfEducation] = useState<"DISTANCE_EDUCATION" | "FULL_TIME_EDUCATION" | "EVENING_CLASSES">("FULL_TIME_EDUCATION");
    const [formSemester, setFormSemester] = useState<"FIRST" | "SECOND" | "SEVENTH" | "EIGHTH">("FIRST");
    const [formShouldBeExpelled, setFormShouldBeExpelled] = useState<string | undefined>(undefined);
    const [formGroupAdminId, setFormGroupAdminId] = useState<string | undefined>(undefined);
    const [formIsEditable, setFormIsEditable] = useState<boolean>(true);
    const [formData, setFormData] = useState<StudyGroupRequest>();
    const [studyGroupRequest, setStudyGroupRequest] = useState<StudyGroupRequest>({
        name: formName,
        coordinatesId: parseFloat(formCoordinatesId),
        studentsCount: parseFloat(formStudentsCount),
        expelledStudents: parseFloat(formExpelledStudents),
        transferredStudents: parseFloat(formTransferredStudents),
        formOfEducation: formFormOfEducation,
        semester: formSemester,
        shouldBeExpelled: (formShouldBeExpelled?.length == 0) ? undefined : parseFloat(formShouldBeExpelled ?? "1"),
        groupAdminId: formGroupAdminId ? parseFloat(formGroupAdminId) : undefined,
        isEditable: formIsEditable,
    })
    const [createStudyGroup, {isLoading, isSuccess, isError, data, error}] = useCreateStudyGroupMutation();
    const {data: persons, refetch: refetchPersons} = useGetAllPersonsQuery();
    const {data: coordinates, refetch: refetchCoordinates} = useGetAllCoordinatesQuery();

    useEffect(
        () => setStudyGroupRequest({
            name: formName,
            coordinatesId: parseFloat(formCoordinatesId),
            studentsCount: parseFloat(formStudentsCount),
            expelledStudents: parseFloat(formExpelledStudents),
            transferredStudents: parseFloat(formTransferredStudents),
            formOfEducation: formFormOfEducation,
            semester: formSemester,
            shouldBeExpelled: (formShouldBeExpelled?.length == 0) ? undefined : parseFloat(formShouldBeExpelled ?? "1"),
            groupAdminId: formGroupAdminId ? parseFloat(formGroupAdminId) : undefined,
            isEditable: formIsEditable,
        }), [formName, formCoordinatesId, formStudentsCount, formExpelledStudents, formTransferredStudents, formFormOfEducation, formSemester, formShouldBeExpelled, formGroupAdminId, formIsEditable]
    )

    useEffect(() => setFormCoordinatesId(coordinates?.[0]?.id?.toString() ?? "0"), [coordinates])
    useEffect(() => setFormGroupAdminId(persons?.[0]?.id?.toString() ?? undefined), [persons])

    const handleOk = async () => {
        await createStudyGroup({studyGroupRequest}).unwrap()
            .then(() => toast("Объект успешно создан", {icon: "🎉"}))
            .catch((e) => toast("Этот объект нельзя создать", {icon: "❌"}));
        closeModal();
    };

    const handleCancel = () => {
        console.log("Cancel Clicked");
        closeModal();
    };

    const formValid: boolean =
        studyGroupRequest.name.trim().length > 0 &&
        studyGroupRequest.studentsCount > 0 &&
        studyGroupRequest.expelledStudents > 0 &&
        studyGroupRequest.transferredStudents > 0 &&
        ((formShouldBeExpelled?.length == 0) || (studyGroupRequest?.shouldBeExpelled ?? -1) > 0);


    return <Modal isOpen={isModalOpen} onOk={handleOk} onClose={handleCancel} okDisabled={!formValid}>
        <div className="space-y-2 bg-white">
            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Имя группы</label>
                <input
                    type="text"
                    name="name"
                    value={formName}
                    onChange={e => setFormName(e.target.value)}
                    contentEditable={isEditable}
                    className="flex-grow p-2 border border-black"
                />
            </div>

            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Координаты</label>
                <select
                    name="Координаты"
                    value={formCoordinatesId}
                    onChange={(e) =>
                        setFormCoordinatesId(e.target.value)
                    }
                    contentEditable={isEditable}
                    className="flex-grow p-2 border border-black max-w-20"
                >
                    {
                        coordinates?.length ? (
                            coordinates.map((coordinate, index) => (
                                <option key={index} value={coordinate.id || "DEFAULT_VALUE"}>
                                    {`(${coordinate.x};${coordinate.y})` || "Unnamed"}
                                </option>
                            ))
                        ) : (
                            <option disabled>No persons available</option>
                        )
                    }
                </select>
                <button className="flex-grow p-1 border border-black bg-blue-300"
                        onClick={() => setIsCoordinatesModalOpen(true)}>
                    Создать
                </button>
            </div>

            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Количество студентов</label>
                <input
                    type="number"
                    name="studentsCount"
                    value={formStudentsCount}
                    onChange={e => setFormStudentsCount(e.target.value)}
                    contentEditable={isEditable}
                    className="flex-grow p-2 border border-black "
                />
            </div>

            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Отчисленные студенты</label>
                <input
                    type="number"
                    name="expelledStudents"
                    value={formExpelledStudents}
                    onChange={e => setFormExpelledStudents(e.target.value)}
                    contentEditable={isEditable}
                    className="flex-grow p-2 border border-black "
                />
            </div>

            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Переведенные студенты</label>
                <input
                    type="number"
                    name="transferredStudents"
                    value={formTransferredStudents}
                    onChange={e => setFormTransferredStudents(e.target.value)}
                    contentEditable={isEditable}
                    className="flex-grow p-2 border border-black "
                />
            </div>

            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Форма обучения</label>
                <select
                    name="formOfEducation"
                    value={formFormOfEducation}
                    onChange={e => setFormFormOfEducation(e.target.value as "DISTANCE_EDUCATION" | "FULL_TIME_EDUCATION" | "EVENING_CLASSES")}
                    contentEditable={isEditable}
                    className="flex-grow p-2 border border-black "
                >
                    <option value="DISTANCE_EDUCATION">Заочное обучение</option>
                    <option value="FULL_TIME_EDUCATION">Дневное обучение</option>
                    <option value="EVENING_CLASSES">Вечернее обучение</option>
                </select>
            </div>

            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">На отчисление</label>
                <input
                    type="text"
                    name="shouldBeExpelled"
                    value={formShouldBeExpelled || ""}
                    onChange={e => setFormShouldBeExpelled(e.target.value)}
                    contentEditable={isEditable}
                    className="flex-grow p-2 border border-black "
                />
            </div>

            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Семестр</label>
                <select
                    name="semester"
                    value={formSemester}
                    onChange={e => setFormSemester(e.target.value as "FIRST" | "SECOND" | "SEVENTH" | "EIGHTH")}
                    contentEditable={isEditable}
                    className="flex-grow p-2 border border-black "
                >
                    <option value="FIRST">Первый</option>
                    <option value="SECOND">Второй</option>
                    <option value="SEVENTH">Седьмой</option>
                    <option value="EIGHTH">Восьмой</option>
                </select>
            </div>

            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Админ</label>
                <select
                    name="Админ"
                    value={formGroupAdminId}
                    contentEditable={isEditable}
                    onChange={e => setFormGroupAdminId(e.target.value)
                    }
                    className="flex-grow p-2 border border-black max-w-20"
                >
                    {
                        persons?.length ? (
                            persons.map((person, index) => (
                                <option key={index} value={person.id || "DEFAULT_VALUE"}>
                                    {person.name || "Unnamed"}
                                </option>
                            ))
                        ) : (
                            <option disabled>No persons available</option>
                        )
                    }
                    <option key={0} value={undefined}>
                        {"Undefined"}
                    </option>
                </select>
                <button className="flex-grow p-1 border border-black bg-blue-300"
                        onClick={() => setIsPersonModalOpen(true)}>
                Создать
                </button>
            </div>

            <div className="flex items-center space-x-4">
                <input
                    type="checkbox"
                    name="isEditable"
                    checked={formIsEditable}
                    contentEditable={isEditable}
                    onChange={() => setFormIsEditable(!formIsEditable)}
                    className="mr-2"
                />
                <label className="font-medium text-gray-700">Можно изменять?</label>
            </div>
        </div>
        <CreateCoordinatesModal isModalOpen={isCoordinatesModalOpen} closeModal={
            () => {
                setIsCoordinatesModalOpen(false);
                refetchCoordinates();
            }
        } isEditable={false}/>
        <CreatePersonModal isModalOpen={isPersonModalOpen}
                           closeModal={() => {
                               setIsPersonModalOpen(false);
                               refetchPersons()
                           }}/>
    </Modal>

}