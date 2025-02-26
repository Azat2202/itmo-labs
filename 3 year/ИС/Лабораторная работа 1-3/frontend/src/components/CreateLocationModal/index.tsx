import { LocationRequest, LocationResponse, useCreateLocationMutation } from "../../store/types.generated";
import { useState } from "react";
import Modal from "../../containers/Modal";

export function CreateLocationModal({ isModalOpen, closeModal }: { isModalOpen: boolean; closeModal: () => void; }) {
    const [createLocation, { isLoading, isSuccess, isError, data, error }] = useCreateLocationMutation();
    const [formData, setFormData] = useState<LocationRequest>({
        x: 1,
        y: 1,
        z: 1,
        name: "",
    });

    const handleOk = async () => {
        await createLocation({ locationRequest: formData }).unwrap();
        closeModal();
    };

    const handleCancel = () => {
        closeModal();
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: name === "x" || name === "y" || name === "z" ? Number(value) || undefined : value,
        });
    };

    const formValid :boolean =
        formData.name.trim().length > 0

    return (
        <Modal isOpen={isModalOpen} onOk={handleOk} onClose={handleCancel} okDisabled={!formValid}>
            <div className="space-y-2 bg-white m-2 p-4">

                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">X</label>
                    <input
                        type="number"
                        name="x"
                        value={formData.x ?? ""}
                        onChange={handleChange}
                        className="flex-grow p-2 border border-black"
                    />
                </div>

                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">Y</label>
                    <input
                        type="number"
                        name="y"
                        value={formData.y ?? ""}
                        onChange={handleChange}
                        className="flex-grow p-2 border border-black"
                    />
                </div>

                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">Z</label>
                    <input
                        type="number"
                        name="z"
                        value={formData.z ?? ""}
                        onChange={handleChange}
                        className="flex-grow p-2 border border-black"
                    />
                </div>

                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">Имя</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name ?? ""}
                        onChange={handleChange}
                        className="flex-grow p-2 border border-black"
                    />
                </div>
            </div>
        </Modal>
    );
}