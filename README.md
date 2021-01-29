﻿###Счётчик уникальных слов на сайте  (RU)
**Принцип работы**:  
1. Пользователь конфигурирует и запускает `.bat` файл;
2. Вводит url сайта в консоль;
3. Приложение скачивает страницу и помещает в папку `pages` в формате `domain_dd.MM_HHmm.html`;
4. Приложение парсит .html файл, сортирует по возврастанию и выводит список слов в консоль:
```
ЧИТАТЬ=16  
КОМПАНИИ=17  
ДЛЯ=19  
СЕГОДНЯ=22  
```
**Первый запуск:**
1. Сконфигурируйте ``.bat`` файл по образцу:
```bat
chcp 1251
MD pages
java -jar ParserSite-1.0.jar
pause
```
***Важно!***
`chcp 1251` - задаёт кодировку консоли *windows-1251*.  
Если вывод русских буков похож на кракозябры или вовсе отсутствует, то попробуйте заменить кодировку на:  
`866` – DOC-кодировка;  
`65001` – UTF-8.

**Технологии:**  
```
JDK 1.8  
JUnit 4.13  
JSoup 1.13.1  
Gradle 6.1
```