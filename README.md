# Gym

Подготовка к WorldSkills.

Session-1

![sessions-1](https://user-images.githubusercontent.com/62645670/116912692-fe1c3a00-ac61-11eb-9847-c8e7446b3596.jpg)

При первом запуске приложения запускается LaunchActivity, в которой пользователь через фрагменты указывает различные настройки. По завершению появляется основная Activity (LogInFragment).

Session-2

На экране Sign up имеется возможность зарегистрироваться на предоставленном api. На экране LogIn возможно залогиниться в приложении. Перед отправкой формы осуществляются проверки на корректность введенных данных. 

Сценарий варианта использования "Вход в приложение незарегистрированного пользователя":

1. Пользователь нажал на иконку приложения.
2. Пользователь нажал на кнопку SignUp.
3. Пользователь ввел необходимые данные в поля.
4. Система проверила введенные данные (если данные верны, система открывает экран LogIn, иначе указывает на ошибку).
5. Пользователь вводит верные данные для входа в приложение (вводит неверные данные).
6. При вводе верных данных система меняет экран на Main, иначе указывает на ошибки.

Session-3

На экране Plan отображается статистика пользователя. На экране Lessons отображаются уроки с превью видео. На экране Profile видны рост и вес пользователя, которые здесь же можно изменить, а также пол. Есть возможность открыть политику приватности по нажатию на Privacy policy(web-сайт откроется внутри приложения).

![session-3](https://user-images.githubusercontent.com/62645670/117265219-6ee37200-ae6d-11eb-88ae-e812ecd88b28.jpg)


Session-4

![1 GxtOL](https://user-images.githubusercontent.com/62645670/118368478-a368d380-b5bb-11eb-8f47-47f21f648b9c.jpg)

Есть возможность сохранения тренировок в БД. Существует режим повторения (не сохраняется тренировка). Возможно изменить время перерыва между тренировками (во время перерыва нельзя начать тренировку). Также были добавлены уведомления, напоминающие пользователю о тренировках каждые 5 минут.

![2 d7jbW](https://user-images.githubusercontent.com/62645670/118368986-18d4a400-b5bc-11eb-828a-11a8e9df42c5.jpg)
