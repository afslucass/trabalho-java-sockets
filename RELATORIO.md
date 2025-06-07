Relatório Técnico – Aplicação Distribuída com Sockets e Busca em JSON

1. Objetivo do Sistema
   Criar uma aplicação distribuída em Java composta por:

Um cliente socket que envia uma consulta textual (palavra-chave),

Um servidor central que recebe essa consulta e a distribui para dois servidores de busca,

Dois servidores trabalhadores responsáveis por buscar resultados em diferentes metades de uma base de dados em JSON,

E por fim, retornar os resultados processados e formatados de volta ao cliente.

2. Componentes do Sistema
   🔹 2.1 Cliente (Client.java)
   Conecta-se via socket TCP ao servidor central.

Envia a consulta (string) passada por linha de comando.

Lê e exibe a resposta formatada recebida do servidor.

🔹 2.2 Servidor Central (AServer.java)
Fica escutando em uma porta predefinida.

Ao receber uma consulta, ela é repassada simultaneamente para dois workers:

BServer (metade superior do JSON)

CServer (metade inferior do JSON)

Recebe os resultados de ambos, formata em texto organizado e os retorna ao cliente via socket.

🔹 2.3 Servidores Trabalhadores (BServer.java e CServer.java)
Ambos herdam a mesma lógica da classe WorkerServer.

Ao iniciar, cada um carrega apenas metade da base de dados JSON:

O inferior carrega os elementos de índice 0 até o meio.

O superior carrega do meio até o final.

Eles escutam em portas distintas.

Cada worker responde com todos os objetos JSON que contenham o termo da consulta no campo "title" ou "abstract".

3. Base de Dados
   Arquivo JSON chamado base_dados.json, com a estrutura:

[
{
"title": "Obra A",
"abstract": "Descrição da Obra A"
},
{
"title": "Obra B",
"abstract": "Descrição da Obra B"
}
...
]

A leitura é feita em memória usando org.json.JSONArray.

4. Algoritmo de Busca
   🔍 Etapas da busca:
   Divisão da base: cada worker carrega somente a sua metade da base de dados.

Processamento local:

A consulta é convertida para minúsculas.

Verifica-se se o termo aparece no title ou abstract de cada entrada da metade correspondente.

Os objetos compatíveis são convertidos em String (JSON) e enviados via socket linha a linha.

Centralização e Formatação:

O servidor central (AServer) coleta todas as respostas e interpreta cada linha como um JSONObject.

Monta uma resposta formatada humanamente legível, com título e resumo de cada resultado.

Retorno ao cliente:

O texto completo é enviado via socket de volta ao cliente.

O cliente imprime o conteúdo no terminal.

5. Tecnologias Utilizadas
   Tecnologia Uso
   Java Sockets Comunicação TCP entre cliente, servidor e workers
   org.json Parsing e manipulação de JSON
   Threads Workers lidam com múltiplas requisições simultaneamente
   I/O com arquivos Leitura do JSON (base_dados.json)
   Linha de comando Entrada de consulta no SocketClient

6. Vantagens da Arquitetura
   🔄 Escalabilidade: fácil adicionar mais workers (ex: um por letra inicial).

⚡ Divisão de carga: o trabalho de busca é dividido em paralelo entre dois servidores.

🔧 Modularidade: componentes independentes com responsabilidades claras.

📦 Extensível: suporte a novos campos, filtros ou formatos de dados JSON.

7. Conclusão
   A aplicação demonstra de forma clara o uso de sockets em Java para criar uma arquitetura distribuída, modular e extensível. A separação do processamento em dois workers melhora a eficiência e simula um cenário real de paralelismo em sistemas /distribuídos. O uso do JSON como base de dados traz flexibilidade e legibilidade.
