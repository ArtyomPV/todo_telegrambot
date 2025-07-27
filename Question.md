1. при выполнении команды **/add** необходимо ввести описание дела, при использовании символа # в описании.
Когда хотим поменять статус на другой, например задача выполнена, вводим команду -> вводим ID задачи и программа выкидывает исключение
```
Exception in thread "pool-2-thread-1" java.lang.RuntimeException: Error executing org.telegram.telegrambots.meta.api.methods.send.SendMessage query: [400] Bad Request: can't parse entities: Character '#' is reserved and must be escaped with the preceding '\'
```