import Modal from "../../containers/Modal";
import {StudyGroupResponse, useGetStudyGroupsQuery} from "../../store/types.generated";
import {useState} from "react";

interface ShowStudyGroupModalProps {
    isModalOpen: boolean,
    closeModal: () => void
}

export function ShowStudyGroupModal({isModalOpen, closeModal}: ShowStudyGroupModalProps) {
    const [id, setId] = useState(1);
    const {data, refetch, isError, isSuccess, isFetching, isLoading} = useGetStudyGroupsQuery({id});
    const noData = isError || isFetching || isLoading;
    return (
        <Modal isOpen={isModalOpen} onOk={closeModal} onClose={closeModal}>
            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Id</label>
                <input className="flex-grow p-2 border border-black"
                       type="number"
                       value={id} onChange={(e) => {
                    setId(parseInt(e.target.value))
                    refetch()
                }}/>
            </div>
            {data && (
                <div className="space-y-2 bg-white min-h-[200px]">

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Имя группы</label>
                        <span className="flex-grow p-2 border border-black min-h-[42px]">{noData ? "" : data.name}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Координаты</label>
                        <span className="flex-grow p-2 border border-black min-h-[42px]">
                            {noData ? "" : `(${data.coordinates?.x};${data.coordinates?.y})`}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Количество студентов</label>
                        <span className="flex-grow p-2 border border-black min-h-[42px]">{noData ? "" : data.studentsCount}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Отчисленные студенты</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{noData ? "" : data.expelledStudents}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Переведенные студенты</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{noData ? "" : data.transferredStudents}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Форма обучения</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{noData ? "" : data.formOfEducation}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">На отчисление</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{noData ? "" : data.shouldBeExpelled}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Семестр</label>
                        <span className="flex-grow p-2 border border-black min-h-[42px]">{noData ? "" : data.semester}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Админ</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{noData ? "" : data.groupAdmin?.name}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Владелец объекта</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{noData ? "" : data.user?.username}</span>
                    </div>
                </div>
            )}
        </Modal>
    );
}

export function ShowStudyGroupModalWithData({isModalOpen, closeModal, data}: ShowStudyGroupModalProps & {data: StudyGroupResponse}) {
    return (
        <Modal isOpen={isModalOpen} onOk={closeModal} onClose={closeModal}>
            {data && (
                <div className="space-y-2 bg-white min-h-[200px]">

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Имя группы</label>
                        <span className="flex-grow p-2 border border-black min-h-[42px]">{data.name}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Координаты</label>
                        <span className="flex-grow p-2 border border-black min-h-[42px]">
                            {`(${data.coordinates?.x};${data.coordinates?.y})`}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Количество студентов</label>
                        <span className="flex-grow p-2 border border-black min-h-[42px]">{data.studentsCount}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Отчисленные студенты</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{data.expelledStudents}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Переведенные студенты</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{data.transferredStudents}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Форма обучения</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{data.formOfEducation}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">На отчисление</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{data.shouldBeExpelled}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Семестр</label>
                        <span className="flex-grow p-2 border border-black min-h-[42px]">{data.semester}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Админ</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{data.groupAdmin?.name}</span>
                    </div>

                    <div className="flex items-center space-x-4">
                        <label className="w-1/3 font-medium text-gray-700">Владелец объекта</label>
                        <span
                            className="flex-grow p-2 border border-black min-h-[42px]">{data.user?.username}</span>
                    </div>


                </div>
            )}
        </Modal>
    );
}