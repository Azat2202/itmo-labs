import React from "react";

interface SelectInputProps{
    name: string,
    value: number | undefined,
    value_setter: (x: number) => void
    labels: number[],
    label: string
}

function SelectInput(props: SelectInputProps) {
    return (
            <label className="font-semibold my-4"> {props.label}
                <select name={props.name}
                        key={props.value}
                        value={props.value}
                        className="bg-gray-700 border border-gray-600 text-sm rounded-md mt-2
                        dark:border-s-gray-700 border-s-2 focus:ring-blue-500 block w-full p-2.5
                        placeholder-gray-400 text-white"
                        onChange={event => {
                            props.value_setter(parseFloat(event.target.value))
                        }}>
                    {props.labels.map((value, index) => (
                        <option value={value} key={value}>
                            {props.labels.map(String)[index]}
                        </option>
                    ))}
                </select>
            </label>
    )
}

export default SelectInput;