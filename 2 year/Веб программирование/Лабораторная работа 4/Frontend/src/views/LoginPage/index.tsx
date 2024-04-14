import React from "react";
import Header from "../../components/AboutMe";
import Clock from "../../components/Clock";
import LoginForm from "../../components/LoginForm";

function LoginPage(){
    return (
        <div className="flex h-screen">
            <div className="hidden lg:flex items-center justify-center flex-1 bg-white text-black">
                <Clock updateInterval={1000}/>
            </div>
            <div className="w-full bg-gray-100 lg:w-1/2 flex items-center justify-center">
                <div className="max-w-md w-full p-6">
                    <Header/>
                    <LoginForm/>
                </div>
            </div>
        </div>
    );
}

export default LoginPage;