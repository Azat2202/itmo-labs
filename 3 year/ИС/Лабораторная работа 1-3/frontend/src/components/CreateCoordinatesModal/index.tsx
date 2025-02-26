import Modal from "../../containers/Modal";
import React, { useState } from "react";
import {
    CoordinatesRequest,
    StudyGroupRequest, useCreateCoordinatesMutation,
    useGetAllCoordinatesQuery,
    useGetAllPersonsQuery
} from "../../store/types.generated";

interface CreateObjectModalInput {
    isModalOpen: boolean,
    closeModal: () => void,
    isEditable: boolean,
}

export function CreateCoordinatesModal({ isModalOpen, closeModal, isEditable }: CreateObjectModalInput) {
    const [ createCoordinates, { isLoading, isSuccess, isError, data, error } ] = useCreateCoordinatesMutation();
    const [ formData, setFormData ] = useState<CoordinatesRequest>({
        x: 0,
        y: 0,
    });
    const handleOk = async () => {
        const data = await createCoordinates({ coordinatesRequest: formData }).unwrap();
        closeModal();
    };

    const handleCancel = () => {
        closeModal();
    };
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: Number(value), // Ensures values are numbers
        });
    };
    return <Modal isOpen={ isModalOpen } onOk={ handleOk } onClose={ handleCancel }>
        <div className="space-y-2 bg-white m-2">
            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">X</label>
                <input
                    type="number"
                    name="x"
                    value={ formData.x }
                    onChange={ handleChange }
                    className="flex-grow p-2 border border-black"
                />
            </div>

            <div className="flex items-center space-x-4">
                <label className="w-1/3 font-medium text-gray-700">Y</label>
                <input
                    type="number"
                    name="y"
                    value={ formData.y }
                    onChange={ handleChange }
                    className="flex-grow p-2 border border-black"
                />
            </div>
        </div>
    </Modal>
}