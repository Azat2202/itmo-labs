import React, {useState} from "react";
import toast from "react-hot-toast";
import {useAppDispatch} from "../../redux/hooks";
import {setLogin} from "../../redux/login";
import {setPassword} from "../../redux/password";
import {useNavigate} from "react-router-dom";


function LoginForm() {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [newLogin, setNewLogin] = useState("");
    const [newPassword, setNewPassword] = useState("");

    function loginRequest(event: React.MouseEvent<HTMLButtonElement>, login: String, password: String){
        event.preventDefault()
        login = encodeURI(login.trim())
        password = encodeURI(password.trim())
        if(login === "" || password === "") {
            toast.error("Fields must be non empty");
            return;
        }
        // lol lets work with promises in strange way
        fetch("/api/login", {
            method: 'POST',
            headers: {"Authorization": "Basic " + btoa(login + ":" + password)}
        })
            .then(response => {
                if(response.ok){
                    dispatch(setLogin(newLogin))
                    dispatch(setPassword(newPassword))
                    navigate("/dots")
                } else {
                    toast.error(response.statusText)
                }
            })
    }

    function registerRequest(event: React.MouseEvent<HTMLButtonElement>, login: String, password: String){
        event.preventDefault()
        login = encodeURI(login.trim())
        password = encodeURI(password.trim())
        if(login === "" || password === "") {
            toast.error("Fields must be non empty")
            return;
        }
        let formData = new FormData();
        formData.append('login', login.toString());
        formData.append('password', password.toString());
        fetch("/api/register",{
            method: 'POST',
            body: formData
        })
            .then(response => {
                if(response.ok){
                    dispatch(setLogin(newLogin))
                    dispatch(setPassword(newPassword))
                    navigate("/dots")
                }
                console.log(response)
            })
    }

    return (
        <form className="space-y-8">
            <div>
                <label htmlFor="username" className="block text-sm font-medium text-gray-700">Username</label>
                <input type={"text"}
                       autoFocus={true}
                       id={"username"}
                       className="mt-1 p-2 w-full border rounded-md focus:border-gray-200 focus:outline-none
                       focus:ring-2 focus:ring-offset-2 focus:ring-gray-300 transition-colors duration-300"
                       onChange={event => setNewLogin(event.target.value)}/>
                <label htmlFor="password" className="block text-sm font-medium text-gray-700">Password</label>
                <input type={"password"}
                       name={"password"}
                       id={"password"}
                       className="mt-1 p-2 w-full border rounded-md focus:border-gray-200 focus:outline-none
                       focus:ring-2 focus:ring-offset-2 focus:ring-gray-300 transition-colors duration-300"
                       onChange={event => setNewPassword(event.target.value)}/>
            </div>
            <div>
                <button onClick={event => loginRequest(event, newLogin, newPassword)}
                        value={newLogin}
                        className="w-full bg-black text-white p-2 rounded-md hover:bg-gray-800 focus:outline-none
                        focus:bg-black focus:ring-2 focus:ring-offset-2 focus:ring-gray-900 transition-colors duration-300">
                    Login
                </button>
            </div>
            <div>
                <button onClick={event => registerRequest(event, newLogin, newPassword)}
                        value={newPassword}
                        className="w-full bg-black text-white p-2 rounded-md hover:bg-gray-800 focus:outline-none
                        focus:bg-black focus:ring-2 focus:ring-offset-2 focus:ring-gray-900 transition-colors duration-300">
                    Register
                </button>
            </div>
        </form>
    )
}

export default LoginForm;
