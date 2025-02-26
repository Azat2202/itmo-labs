import {
    PersonRequest,
    useCreatePersonMutation,
    useGetAllCoordinatesQuery,
    useGetAllLocationsQuery
} from "../../store/types.generated";
import React, {useEffect, useState} from "react";
import Modal from "../../containers/Modal";
import { CreateLocationModal } from "../CreateLocationModal";
import toast from "react-hot-toast";

export function CreatePersonModal({ isModalOpen, closeModal }: { isModalOpen: boolean; closeModal: () => void; }) {
    const [ isLocationModalOpen, setIsLocationModalOpen ] = useState(false);
    const [createPerson, { isLoading, isSuccess, isError, data, error }] = useCreatePersonMutation();
    const { data: locations, refetch: refetchLocations } = useGetAllLocationsQuery();
    const [formData, setFormData] = useState<PersonRequest>({
        name: "darya",
        eyeColor: "BLACK",
        locationId: 1,
        weight: 1,
        hairColor: undefined,
        nationality: "FRANCE"
    });

    const handleOk = async () => {
        await createPerson({ personRequest: formData })
            .unwrap()
            .then(() => toast("Объект создан!"))
            .catch(() => toast("Ошибка создания объекта!"));
        closeModal();
    };

    const handleCancel = () => {
        closeModal();
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: name === "locationId" || name === "weight" ? Number(value) : value,
        });
    };

    useEffect(() => setFormData({...formData, locationId: locations?.[0]?.id || 1}), [locations]);

    const formValid: boolean =
        formData.name.trim().length > 0 &&
        formData.weight > 0

    return (
        <Modal isOpen={isModalOpen} onOk={handleOk} onClose={handleCancel} okDisabled={!formValid}>
            <div className="space-y-2 bg-white m-2 p-4">
                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">Имя</label>
                    <input
                        type="text"
                        name="name"
                        value={ formData.name }
                        onChange={ handleChange }
                        className="flex-grow p-2 border border-black"
                    />
                </div>

                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">Цвет глаз</label>
                    <select
                        name="eyeColor"
                        value={ formData.eyeColor }
                        onChange={ handleChange }
                        className="flex-grow p-2 border border-black"
                    >
                        <option value="RED">Красный</option>
                        <option value="BLACK">Черный</option>
                        <option value="BLUE">Синий</option>
                        <option value="BROWN">Коричневый</option>
                    </select>
                </div>

                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">Цвет волос</label>
                    <select
                        name="hairColor"
                        value={ formData.hairColor || "" }
                        onChange={ handleChange }
                        className="flex-grow p-2 border border-black"
                    >
                        <option value="RED">Красный</option>
                        <option value="BLACK">Черный</option>
                        <option value="BLUE">Синий</option>
                        <option value="BROWN">Коричневый</option>
                        <option value={undefined}>Undefined</option>
                    </select>
                </div>

                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">Локация</label>
                    <select
                        name="Локация"
                        value={ formData.locationId }
                        onChange={ (e) =>
                            setFormData({
                                ...formData,
                                locationId: e.target.value ? Number(e.target.value) : 1,
                            })
                        }
                        className="flex-grow p-2 border border-black max-w-20"
                    >
                        {
                            locations?.length ? (
                                locations.map((location, index) => (
                                    <option key={ index } value={ location.id || "DEFAULT_VALUE" }>
                                        { location?.name || `(${location.x};${location.y};${location.z})` }
                                    </option>
                                ))
                            ) : (
                                <option disabled>No locations available</option>
                            )
                        }
                    </select>
                    <button className="flex-grow p-1 border border-black bg-blue-300"
                            onClick={ () => setIsLocationModalOpen(true) }>
                        Создать
                    </button>
                </div>

                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">Вес</label>
                    <input
                        type="number"
                        name="weight"
                        value={ formData.weight }
                        onChange={ handleChange }
                        className="flex-grow p-2 border border-black"
                    />
                </div>

                <div className="flex items-center space-x-4">
                    <label className="w-1/3 font-medium text-gray-700">Национальность</label>
                    <select
                        name="nationality"
                        value={ formData.nationality || "" }
                        onChange={ handleChange }
                        className="flex-grow p-2 border border-black"
                    >
                        <option value="FRANCE">Франция</option>
                        <option value="SPAIN">Испания</option>
                        <option value="SOUTH_KOREA">Южная Корея</option>
                        <option value="JAPAN">Япония</option>
                    </select>
                </div>
            </div>
            <CreateLocationModal isModalOpen={isLocationModalOpen} closeModal={() => {
                setIsLocationModalOpen(false);
                refetchLocations();
            }}/>
        </Modal>
    );
}