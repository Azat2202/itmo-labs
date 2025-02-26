import React, {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {RootState} from "../../store/store";
import {
    useApproveAdminProposalMutation,
    useBecomeAdminMutation, useDeclineAdminProposalMutation,
    useGetAdminProposalsQuery,
    useMeQuery
} from "../../store/types.generated";

export function AdminPanel() {
    const userLogin = useSelector((state: RootState) => state.auth.login);

    const {data: user, refetch } = useMeQuery()

    useEffect(() => {
        const intervalId = setInterval(refetch, 1000)
        return () => clearInterval(intervalId)
    }, [refetch])

    return <div className="text-xl pl-10 p-4">
        <h1 className="text-3xl font-bold">Админ панель</h1>
        <ul className="list-disc">
            <li>
                Пользователю системы должен быть предоставлен интерфейс для авторизации/регистрации нового пользователя.
                У каждого пользователя должен быть один пароль. Требования к паролю: пароль должен быть уникален. В
                системе предполагается использование следующих видов пользователей (ролей):обычные пользователи и
                администраторы. Если в системе уже создан хотя бы один администратор, зарегистрировать нового
                администратора можно только при одобрении одним из существующих администраторов (у администратора должен
                быть реализован интерфейс со списком заявок и возможностью их одобрения).
            </li>
            <li>
                Редактировать и удалять объекты могут только пользователи, которые их создали, и администраторы
                (администраторы могут редактировать объекты, которые пользователь разрешил редактировать при создании).
            </li>
            <li>
                Зарегистрированные пользователи должны иметь возможность просмотра всех объектов, но модифицировать
                (обновлять) могут только принадлежащие им (объект принадлежит пользователю, если он его создал). Для
                модификации объекта должно открываться отдельное диалоговое окно. При вводе некорректных значений в поля
                объекта должны появляться информативные сообщения о соответствующих ошибках.
            </li>
            <li>
                Вы : {user?.username}
            </li>
            <li>
                Ваша роль - {user?.role}
            </li>
            {user?.role == "ADMIN" && <AdminProposals/>}
            {user?.role == "DEFAULT" && <UserToAdminButton/>}
        </ul>
    </div>
}

export function AdminProposals(){
    const {data: adminProposals, refetch} = useGetAdminProposalsQuery();
    const [approveAdmin] = useApproveAdminProposalMutation();
    const [declineAdmin] = useDeclineAdminProposalMutation();
    useEffect(() => {
        const intervalId = setInterval(refetch, 1000)
        return () => clearInterval(intervalId)
    }, [refetch])
    if (adminProposals?.length == 0)
        return <>
            Заявок в администраторы пока нет!
        </>
    return <>
        <h2 className="text-3xl">Заявки в администраторы</h2>
        <table className="table-auto border-collapse border-2 border-black m-2">
            <thead className="text-center">
            <tr>
                <th className="border border-black p-2">Имя</th>
                <th className="border border-black p-2">Одобрить</th>
                <th className="border border-black p-2">Отклонить</th>
            </tr>
            </thead>
            <tbody>
            {adminProposals?.map((proposal, index) => (
                <tr key={index}>
                    <td className="border border-black p-1 text-center">{proposal?.user?.username}</td>
                    <td className="border border-black p-1 text-center">
                       <button onClick={() =>
                           approveAdmin({proposalId: proposal.id!!})
                               .then(refetch)
                       }>
                           ✅
                       </button>
                    </td>
                    <td className="border border-black p-1 text-center">
                        <button onClick={() =>
                            declineAdmin({proposalId: proposal.id!!})
                                .then(refetch)
                        }
                        >
                            🚫
                        </button>
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    </>
}

export function UserToAdminButton() {
    const [becomeAdmin, {isError}] = useBecomeAdminMutation();
    return <>
        <button className="underline text-blue-800"
                onClick={() => becomeAdmin()}
        >
            Подать заявку в администраторы!
        </button>
        <br/>
        {isError && "Вы уже подавали заявку в администраторы. Ждите. Подписаться."}
    </>
}

