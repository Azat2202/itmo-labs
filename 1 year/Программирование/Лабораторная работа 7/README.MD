# Лабораторная работа 7
### Вариант 3116910

*Ссылки в README.MD ведут на строки кода с реализацией*

**[ОТЧЕТ](Программирование%207%20Сиразетдинов%20Азат.pdf)**

## Доработать программу из лабораторной работы №6 следующим образом:

- [Организовать хранение коллекции в реляционной СУБД (PostgresQL). Убрать хранение коллекции в файле.](server/src/main/java/managers/DatabaseCommands.java)
- [Для генерации поля id использовать средства базы данных (sequence).](server/src/main/java/commands/AddElement.java#L37)
- [Обновлять состояние коллекции в памяти только при успешном добавлении объекта в БД](server/src/main/java/commands/RemoveAllByAverageMark.java#L40)
- [Все команды получения данных должны работать с коллекцией в памяти, а не в БД](server/src/main/java/commands/CountByAverageMark.java#L24)
- [Организовать возможность регистрации и авторизации пользователей. У пользователя есть возможность указать пароль.](client/src/main/java/utility/RuntimeManager.java#L59)
- [Пароли при хранении хэшировать алгоритмом SHA-384](server/src/main/java/managers/DatabaseManager.java#L30)
- [Запретить выполнение команд не авторизованным пользователям.](server/src/main/java/managers/DatabaseManager.java#L84)
- [При хранении объектов сохранять информацию о пользователе, который создал этот объект.](common/src/main/java/models/StudyGroup.java#L40)
- [Пользователи должны иметь возможность просмотра всех объектов коллекции, но модифицировать могут только принадлежащие им.](server/src/main/java/commands/RemoveAllByAverageMark.java#L40)
- [Для идентификации пользователя отправлять логин и пароль с каждым запросом.](client/src/main/java/utility/RuntimeManager.java#L68)

### Необходимо реализовать многопоточную обработку запросов.

- [Для многопоточного чтения запросов использовать Cached thread pool](server/src/main/java/utility/Server.java?plain=1#L61)
- [Для многопотчной обработки полученного запроса использовать Fixed thread pool](server/src/main/java/managers/ConnectionManager.java?plain=1#L38)
- [Для многопоточной отправки ответа использовать ForkJoinPool](server/src/main/java/managers/ConnectionManager.java?plain=1#L41)
- [Для синхронизации доступа к коллекции использовать синхронизацию чтения и записи с помощью synchronized](server/src/main/java/managers/CollectionManager.java)

### Порядок выполнения работы:

- В качестве базы данных использовать PostgreSQL.
- Для подключения к БД на кафедральном сервере использовать хост pg, имя базы данных - studs, имя пользователя/пароль совпадают с таковыми для подключения к серверу.

### Отчёт по работе должен содержать:

1. Текст задания.
2. Диаграмма классов разработанной программы.
3. Исходный код программы.
4. Выводы по работе.

### Вопросы к защите лабораторной работы:

1. Многопоточность. Класс Thread, интерфейс Runnable. Модификатор synchronized.
2. Методы wait(), notify() класса Object, интерфейсы Lock и Condition.
3. Классы-сихронизаторы из пакета java.util.concurrent.
4. Модификатор volatile. Атомарные типы данных и операции.
5. Коллекции из пакета java.util.concurrent.
6. Интерфейсы Executor, ExecutorService, Callable, Future
7. Пулы потоков
8. JDBC. Порядок взаимодействия с базой данных. Класс DriverManager. Интерфейс Connection
9. Интерфейсы Statement, PreparedStatement, ResultSet, RowSet
10. Шаблоны проектирования.
