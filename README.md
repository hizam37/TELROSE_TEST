# TELROSE_TEST
Тестовое задание на позицию Java Backend разработчика.
Описание задачи:
Необходимо реализовать REST-API средствами языка программирования Java и
фреймворка Spring.
Выбор СУБД для хранения данных остаётся за Вами.
Пункты, помеченные звёздочкой &quot;*&quot;, являются дополнительными.
Обязательные условия:
1*) Реализовать возможность авторизации по логину и паролю (admin:admin). Тип
авторизации разрешается выбрать с обоснованием: Base Auth, OAuth, JWT;
2 ) Реализовать CRUD модель для работы с пользователями (контактная
информация);
3 ) Реализовать CRUD модель для работы с детальной информацией о
пользователе;
4*) Реализовать CRUD модель для возможности работы с фотографией
пользователя;
Хранимая информации о &quot;пользователе&quot;:
1 ) Фамилия
2 ) Имя
3 ) Отчество
4 ) Дата рождения
5 ) Электронная почта
6 ) Номер телефона
7*) Фотография
Будет плюсом, если:
1 ) Выбранная Вами СУБД - PostgreSQL;
2 ) Проект будет покрыт автотестами;
3 ) В проекте будут присутствовать комментарии;
4 ) Для работы с базой данных будет использован JDBC / Hibernate;
5*) Хотя бы одно из дополнительных заданий в разделе обязательных будет
выполнено;
Ожидаемые данные:
1 ) Исходный код готового проекта;
2 ) Экспортированный JSON из Postman для тестирования REST-API;
Сроки выполнения задания:
На выполнение задания предоставляется 3 дня.





# Решение
Экспортированный JSON из Postman для тестирования REST-API
Для регистрации Пользователя мы используем post-запрос с указанным URL-адресом
http://localhost:8086/api/v1/auth/register 
```
{
    "firstName": "dan",
    "dateOfBirth":"2022-10-15",
    "phoneNumber": "9560296487",
    "middleName": "ali",
    "lastName": "hizam",
    "password":"123",
    "email": "dhizam@gmail.com"
}
```

Пользователь может войти в систему с помощью post-запроса
http://localhost:8086/api/v1/auth/login
```
{
 "email": "dhizam@gmail.com",
 "password":"123"
}

```


Пользователь может обновить свою контактную информацию, не мешая другим зарегистрированным клиентам, с помощью ссылочного токена (Reference Token), который содержит идентификатор пользователя и генерируется при входе(login) пользователя в систему вместе с токеном доступа(AccessToken) для аутентификации..

Пользователь может обновить контактную информацию, используя запрос put 
http://localhost:8086/api/v1/auth/login/costumer/update_contact_info 
примечание: здесь мы предполагаем, что пользователь с адресом электронной почты "dhizam@gmail.com" уже вошел в систему и хочет обновить свои контактные данные
```
{
    "phoneNumber": "9560296887",
    "email": "ddhizam@gmail.com"
}
```

Пользователь может обновить полную информацию, используя запрос put 
http://localhost:8086/api/v1/auth/login/costumer/update_costumer_info 
примечание: здесь мы предполагаем, что пользователь с адресом электронной почты "dhizam@gmail.com" уже вошел в систему и хочет обновить свои контактные данные
```
{
    "firstName": "dan",
    "dateOfBirth":"1993-10-15",
    "phoneNumber": "9560296487",
    "middleName": "ali",
    "lastName": "hizam",
    "password":"123",
    "email": "dhizam@gmail.com"
}
```


Пользователь с ролью Администратора может получить доступ к клиенту по идентификатору
Пользователь с ролью Admin может получить клиента по идентификатору, используя запрос get
http://localhost:8086/api/v1/auth/get_costumer/4
```
{
    "id": 4,
    "firstName": "daniel",
    "middleName": "lio",
    "lastName": "hizam",
    "dateOfBirth": "2022-10-15",
    "password": "$2a$10$E8YVDibnz8mDGg10zuLFQOWVbTMYv1jkzzaFyqBnN438MwZunf6ge",
    "email": "daniilhizam@gmail.com",
    "phoneNumber": "9560296487"
}
```

Пользователь с ролью Администратора может принимать всех клиентов используя запрос get
http://localhost:8086/api/v1/auth/get_all_costumers

```
[
{
    "id": 4,
    "firstName": "daniel",
    "middleName": "lio",
    "lastName": "hizam",
    "dateOfBirth": "2022-10-15",
    "password": "$2a$10$E8YVDibnz8mDGg10zuLFQOWVbTMYv1jkzzaFyqBnN438MwZunf6ge",
    "email": "daniilhizam@gmail.com",
    "phoneNumber": "9560296487"
},
    "id": 4,
    "firstName": "daniel",
    "middleName": "lio",
    "lastName": "hizam",
    "dateOfBirth": "2022-10-15",
    "password": "$2a$10$E8YVDibnz8mDGg10zuLFQOWVbTMYv1jkzzaFyqBnN438MwZunf6ge",
    "email": "daniilhizam@gmail.com",
    "phoneNumber": "9560296487"
}
]
```
