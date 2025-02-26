import React, {useState} from "react";
import {useGetFeedHistoryQuery, useUploadFeedMutation} from "../../store/types.generated";
import toast from "react-hot-toast";
import axios from "axios";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store/store";

export function UploadFile() {
    const [file, setFile] = useState<File | null>(null);
    const [uploadFile, {isSuccess, isLoading}] = useUploadFeedMutation();
    const token = useSelector((state: RootState) => state.auth.token);
    const [page, setPage] = useState(0);

    const {data: feedHistory, refetch: refetchFeeds} = useGetFeedHistoryQuery({
        page: page,
        sortDirection: "desc",
    })

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files && event.target.files[0]) {
            setFile(event.target.files[0]);
        }
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!file) {
            toast.error("Файл не выбран");
            return;
        }
        const formData = new FormData();
        formData.append("file", file);
        await axios.post("http://localhost:8080/api/collection/studyGroup/feed",
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                    "Authorization": `Bearer ${token}`
                },
            })
            .then(() => {
                toast.success("Файл успешно загружен!")
                refetchFeeds()
            })
            .catch(() => {
                toast.error("Ошибка при загрузке файла")
                refetchFeeds()
            });
    };

    return (<div className="pl-10 p-4">
        <h1 className="text-3xl font-bold">Загрузка файлов</h1>
        <ul className="list-disc text-2xl min-h-[40px]">

            <li>Добавить в систему возможность массового добавления объектов при помощи импорта файла. Формат для
                импорта необходимо согласовать с преподавателем. Импортируемый файл должен загружаться на сервер через
                интерфейс разработанного веб-приложения.
                <ul className="list-disc text-2xl min-h-[40px]">
                    <li>При реализации логики импорта объектов необходимо реализовать транзакцию таким образом, чтобы в
                        случае возникновения ошибок при импорте, не был создан ни один объект.
                    </li>
                    <li>При импорте должна быть реализована проверка пользовательского ввода в соответствии с
                        ограничениями
                        предметной области из ЛР1.
                    </li>
                    <li>При наличии вложенных объектов в основной объект из ЛР1 необходимо задавать значения полей
                        вложенных
                        объектов в той же записи, что и основной объект.
                    </li>
                    <br/>
                    <form onSubmit={handleSubmit}>
                        <input
                            type="file"
                            onChange={handleFileChange}
                        />
                        <button className="underline text-blue-800">Отправить файл</button>
                    </form>
                </ul>
            </li>

            <li>Необходимо добавить в систему интерфейс для отображения истории импорта (обычный пользователь видит
                только операции импорта, запущенные им, администратор - все операции).
                <ul className="list-disc text-2xl">
                    <li>
                        В истории должны отображаться id операции, статус ее завершения, пользователь, который ее
                        запустил,
                        число
                        добавленных объектов в операции (только для успешно завершенных).
                    </li>
                </ul>
            </li>
            <table className="table-auto text border-collapse border-2 border-black m-2">
                <thead className="text-center">
                <tr>
                    <th className="border border-black p-2">Id</th>
                    <th className="border border-black p-2">Дата</th>
                    <th className="border border-black p-2">Ссылка</th>
                    <th className="border border-black p-2">Количество добавленных объектов</th>
                    <th className="border border-black p-2">Успешная?</th>
                </tr>
                </thead>
                <tbody>
                {feedHistory?.content?.map((feed, index) => (
                    <tr key={feed.id}>
                        <td className="border border-black p-1 text-center">{feed.id}</td>
                        <td className="border border-black p-1 text-center">{feed.creationDate}</td>
                        <td className="border border-black p-1 text-center">
                            {feed.isSuccessful === true ?
                                <a href={feed.feedUrl} className="text-blue underline">Ссылка</a> : <></>}
                        </td>
                        <td className="border border-black p-1 text-center">{feed.batchSize}</td>
                        <td className="border border-black p-1 text-center">{feed.isSuccessful ? "ДА!" : "Нет("}</td>
                    </tr>
                ))}
                </tbody>
                <tfoot>
                {!feedHistory?.first &&
                    <button onClick={() => setPage(page - 1)}
                            className="m-1 p-1 w-6 border-2 font-bold border-black rounded transition hover:bg-gray-200"
                    >{"<"}</button>
                }
                {!feedHistory?.last &&
                    <button onClick={() => setPage(page + 1)}
                            disabled={page >= (feedHistory?.totalPages ? feedHistory?.totalPages : 0) - 1}
                            className="m-1 p-1 w-6 border-2 font-bold border-black rounded transition hover:bg-gray-200"
                    >{">"}</button>
                }
                </tfoot>
            </table>
        </ul>
        =
    </div>)
}