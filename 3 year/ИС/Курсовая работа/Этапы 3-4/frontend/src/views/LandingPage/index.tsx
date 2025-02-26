import { Link } from "react-router-dom";
import { useAuth } from "react-oidc-context";
import {useGetApiMeQuery} from "../../store/types.generated";

export function LandingPage() {
    const auth = useAuth();
    const {data: userData} = useGetApiMeQuery()

    return (
        <div className="min-h-screen bg-burgundy-900 flex flex-col text-white">
            {/* Header */}
            <header className="bg-gradient-to-b bg-burgundy-950 text-burgundy-200 p-4 fixed w-full z-10 flex items-center justify-between shadow-lg shadow-burgundy-500/30">
                <div className="flex-grow flex justify-center">
                    <h1 className="text-5xl font-extrabold tracking-wider">BUNKER</h1>
                </div>
                <div>
                    {auth.user?.access_token && <button onClick={() => auth.removeUser()}
                        className="bg-burgundy-900 mr-2 hover:bg-burgundy-700 text-burgundy-100 font-bold py-2 px-6 rounded-lg transition duration-300 border-2 border-burgundy-100">
                        ВЫЙТИ ИЗ АККАУНТА ({userData?.username})
                    </button>}
                    <Link to="/main">
                        <button
                            className="bg-burgundy-900 hover:bg-burgundy-700 text-burgundy-100 font-bold py-2 px-6 rounded-lg transition duration-300 border-2 border-burgundy-100">
                            НАЧАТЬ
                        </button>
                    </Link>
                </div>
            </header>

            <main className="flex-grow flex flex-col items-center justify-center mt-24 space-y-6 px-4">
                <div
                    className="bg-burgundy-950 text-burgundy-300 rounded-xl p-8 max-w-4xl shadow-lg mb-8 shadow-burgundy-500/30">
                    <h2 className="text-2xl font-semibold text-center text-burgundy-200 mb-4">
                        Добро пожаловать в онлайн-игру <span className="text-burgundy-500">Бункер!</span>
                    </h2>

                    <div className="text-center">
                        <h3 className="text-2xl font-semibold text-burgundy-200 mb-4">Что вас ждет?</h3>
                        <p className="text-lg text-burgundy-200 mb-6">
                            Исследуйте катастрофу, найдите бункер и решите, кто будет выживать в этом опасном мире. Вам
                            предстоит интересное приключение с множеством неожиданностей!
                        </p>
                    </div>
                    <p className="text-center mb-6">
                        Чтобы играть, вам нужно <Link to="/main" className="text-burgundy-500 hover:underline">зарегистрироваться</Link>.
                    </p>
                </div>

                <div
                    className="bg-burgundy-950 text-gray-800 rounded-xl p-8 max-w-4xl shadow-lg shadow-burgundy-500/30">
                    <h3 className="text-2xl font-semibold text-burgundy-200 mb-4">Правила Игры</h3>
                    <div className="space-y-4 text-burgundy-300">
                        <p>
                            Можете ли вы себе представить, как это — пережить глобальную катастрофу? Думаю, что нет... Именно для этого был создан «Бункер Онлайн», чтобы вы смогли ощутить, как это. Наша игра очень проста, и на изучение правил вам не понадобится много времени!
                        </p>
                        <p>
                            История: На земле вот-вот произойдет катастрофа, а может уже и началась! Я, как и большинство людей в панике пытаюсь выжить, и найти укрытие, чтобы спасти свою жизнь. В поисках убежища я увидел группу людей, но не знал что делать... Стоит подойти к ним или нет, ведь опасность сейчас на каждом шагу! Но так как терять было нечего я решил пойти к ним... Как только я начал подходить к людям, я понял, что мне невероятно повезло — все эти люди находились возле бункера на который была последняя надежда! Как оказалось они ждут людей, которым все еще удалось выжить, и разбили временный лагерь... Приняли меня хорошо, но что меня ждет дальше мне было не известно. Все что я понимал, так это то, что предстоит решить, кто действительно заслуживает попасть в бункер, чтобы остаться в живых. Тех, кто не попадет - ждет верная смерть. И тут началась моя история выживания...
                        </p>
                        <p>
                            Катаклизм: Описание текущего для игры катаклизма. Как это произошло, что случилось и четкое понимание того, с чем связаны проблемы, что даст вам понять в процессе игры кто среди людей вам подходит, а кого нужно выгнать.
                        </p>
                        <p>
                            Бункер: Описание найденного бункера. Единственный шанс, чтобы выжить в случае катаклизма - это попасть в бункер. У вас есть информация о необходимом времени пребывания в бункере, запасе еды, размерах и вместимости бункера, а также о полезных вещах в нем.
                        </p>
                        <p>
                            Персонаж: Описание вашего персонажа. Ваш герой состоит из следующих характеристик:
                            <ul className="list-disc list-inside">
                                <li>Пол</li>
                                <li>Телосложение</li>
                                <li>Профессия</li>
                                <li>Здоровье</li>
                                <li>Хобби</li>
                                <li>Фобия</li>
                                <li>Особенность характера</li>
                                <li>Инструмент</li>
                                <li>Крупный инвентарь</li>
                            </ul>
                        </p>
                        <p>
                            Процесс игры: В каждом раунде игрок открывает о себе все новые факты и представляет своего персонажа и убеждаете остальных в необходимости попасть в бункер именно вам. Далее наступает время голосования, где игроки должны выбрать того, кто покинет временный лагерь и не попадёт в бункер.
                        </p>
                        <p>
                            Победа в игре: По завершению финального голосования, когда определилось нужное количество людей для прохождения в бункер, игра завершается. Игроки, которые попали в бункер и переживут катаклизм - считаются победителями!
                        </p>
                    </div>
                </div>

                <div className="mt-12 text-center mb-3">
                    <h3 className="text-2xl font-semibold text-burgundy-400 mb-4">Готовы начать?</h3>
                    <p className="text-xl text-burgundy-400 mb-6">Пройдите регистрацию и присоединяйтесь к игре! Мы ждем вас!</p>
                    <Link to="/main">
                        <button className="bg-burgundy-950 hover:bg-burgundy-700 text-burgundy-200 border-2 border-burgundy-200 font-bold py-2 px-6 rounded-lg transition duration-300">
                            РЕГИСТРАЦИЯ
                        </button>
                    </Link>
                </div>
            </main>
        </div>
    );
}
