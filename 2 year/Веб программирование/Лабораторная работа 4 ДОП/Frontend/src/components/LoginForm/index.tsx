import React from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "react-oidc-context";


function LoginForm() {
    const auth = useAuth()
    const navigate = useNavigate()
    const login = () => {
        window.history.pushState("", "", "/dots")
    }


    return (
        <form className="space-y-8">
            <div>
                <button onClick={login}
                        className="w-full bg-black text-white p-2 rounded-md hover:bg-gray-800 focus:outline-none flex items-center justify-center
                        focus:bg-black focus:ring-2 focus:ring-offset-2 focus:ring-gray-900 transition-colors duration-300">
                    <p className="ml-auto">Go to main page</p><img className="w-7 h-7 ml-auto"
                        src={"https://upload.wikimedia.org/wikipedia/commons/2/29/Keycloak_Logo.png"}/>
                </button>
            </div>
        </form>
    )
}

export default LoginForm;
