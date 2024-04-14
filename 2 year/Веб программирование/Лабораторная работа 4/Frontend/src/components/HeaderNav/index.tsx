import React from "react";
import {useAppDispatch} from "../../redux/hooks";
import {setLogin} from "../../redux/login";
import {setPassword} from "../../redux/password";
import {useNavigate} from "react-router-dom";

function HeaderNav(){
    const dispatch = useAppDispatch()
    const navigate = useNavigate()
    function logOut(){
        dispatch(setLogin(""))
        dispatch(setPassword(""))
        navigate("/")
    }
    return (
        <div className="bg-white dark:bg-gray-900">
            <div className="flex flex-wrap justify-between mx-auto p-4">
                <img className="h-9" src="https://itmo.ru/file/pages/213/logo_osnovnoy_russkiy_belyy.png" alt="ITMO Logo"/>
                <span className="ml-5 self-center text-2xl font-semibold whitespace-nowrap dark:text-white justify-self-center">
                    Лабораторная работа 4</span>
                <div className="flex flex-wrap">
                    <button onClick={logOut} className="text-white bg-gray-600 hover:bg-gray-800 focus:ring-blue-300 font-medium
                    rounded-lg text-sm px-3 py-1.5 me-2 mb-2 focus:outline-none">LOG OUT</button>
                    <img className=" w-9 h-9" src="https://img.icons8.com/ios/50/FFFFFF/user-male-circle--v1.png"
                         alt="user-male-circle--v1"/>
                </div>
            </div>
        </div>
    )
}

export default HeaderNav;