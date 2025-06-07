# OBS

Abrindo o projeto no VS Code, o arquivo WorkerServer.java vai apresentar mensagens de erro no codigo. Ignorar essas mensagens.

# Setup (Windows usando PowerShell)

## Compilar

1. javac -cp .\json-20230618.jar AServer.java
2. javac ./Client.java
3. javac javac -cp .\json-20230618.jar BServer.java .\WorkerServer.java
4. javac javac -cp .\json-20230618.jar CServer.java .\WorkerServer.java

## Rodar (Em terminais separados)

1. java -cp ".;json-20230618.jar" AServer
2. java -cp ".;json-20230618.jar" BServer
3. java -cp ".;json-20230618.jar" CServer
4. java Client "mensagem para buscar na base de dados entre aspas duplas"
