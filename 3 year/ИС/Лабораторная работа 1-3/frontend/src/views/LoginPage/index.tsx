import {useAuthenticateMutation, useRegisterMutation} from "../../store/types.generated";
import React, {useState} from "react";
import {Routes, useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {setToken, setLogin} from "../../store/authSlice";
import {RootState} from "../../store/store";

export function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setLocalPassword] = useState('');
    const [authenticate, {isLoading: isLoginLoading, isError: isLoginError, data, error}] = useAuthenticateMutation();
    const [register, {isLoading: isRegisterLoading, isError: isRegisterError}] = useRegisterMutation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const handleLogin = async () => {
        try {
            const jwtDto = {username, password};
            await authenticate({jwtDto})
                .unwrap()
                .then((response) => {
                        dispatch(setToken(response.token!!));
                        dispatch(setLogin(username));
                        navigate("/main");
                    }
                );
        } catch (err) {
            console.error('Login failed:', err);
        }
    };
    const handleRegister = async () => {
        try {
            const registerUserDto = {username, password};
            register({registerUserDto})
                .unwrap()
                .then(() => {
                    const jwtDto = {username, password};
                    authenticate({jwtDto})
                        .unwrap()
                        .then((response) => {
                            dispatch(setToken(response.token!!));
                            dispatch(setLogin(username));
                            navigate("/main");
                        });
                });

        } catch (err) {
            console.error('Login failed:', err);
        }
    };

    return <div className="flex flex-col h-screen p-8 ">
        <header className="text-center space-y-2 mb-10">
            <p>Министерство науки и высшего образования Российской Федерации</p>
            <p>Федеральное государственное автономное образовательное учреждение</p>
            <p>Высшего образования</p>
            <p className="italic">Факультет Программной Инженерии и Компьютерной Техники</p>
        </header>
        <main className="flex flex-col items-center space-y-2 mb-32">
            <h1 className="text-lg font-bold">Лабораторная работа 1 по Информационным Системам</h1>
            <p>Управление коллекцией StudyGroup</p>
            <p>Вариант №368796</p>
        </main>
        <footer className="space-y-8 text-center">
            <div className="flex flex-col space-y-2 mr-64 mb-32">
                <p className="text-right">
                    Группа: P3316<br/>
                    Выполнил: <br/>
                    <input type={"text"} placeholder={"Сиразетдинов"} value={username}
                           onChange={e => setUsername(e.target.value)}
                           className="w-32 placeholder:text-black"
                    />
                    <input type={"password"} placeholder={"А. Н."} value={password}
                           onChange={e => setLocalPassword(e.target.value)}
                           className="w-10 placeholder:text-black"
                    />
                    <br/>
                    <button onClick={handleLogin} disabled={isLoginLoading}>
                        {isLoginError ? 'Не проверено!' : (isLoginLoading ? 'Проверяю...' : 'Проверить')}
                    </button>
                    <br/>
                    <button onClick={handleRegister} disabled={isRegisterLoading}>
                        {isRegisterError ? 'Отправить на проверку!' : (isRegisterLoading ? 'Проверяю...' : 'Отправить на проверку')}
                    </button>
                    <br/>
                    Бострикова Д. А.
                </p>
            </div>
            <div>
                <p>г. Санкт-Петербург</p>
                <p>2024</p>
            </div>
        </footer>
    </div>
}