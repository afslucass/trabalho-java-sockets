import java.io.*;
import java.net.*;
import org.json.*;

public class AServer {

    public static void main(String[] args) {
        int porta = 12345;

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor central escutando na porta " + porta);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                String mensagem = entrada.readLine();

                System.out.println("Consulta recebida: " + mensagem);

                // Consulta aos workers
                String respostaSuperior = consultarWorker("localhost", 23456, mensagem);
                String respostaInferior = consultarWorker("localhost", 23457, mensagem);

                System.out.println("\n=== Resultados encontrados ===");

                System.out.println("\n--- Resultados da metade INFERIOR ---");
                exibirFormatado(respostaInferior);

                System.out.println("\n--- Resultados da metade SUPERIOR ---");
                exibirFormatado(respostaSuperior);

                System.out.println("=====================================\n");

                clienteSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String consultarWorker(String host, int porta, String consulta) {
        StringBuilder resposta = new StringBuilder();

        try (Socket socket = new Socket(host, porta);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(consulta);

            String linha;
            while ((linha = in.readLine()) != null) {
                resposta.append(linha).append("\n");
            }

        } catch (IOException e) {
            resposta.append("{\"erro\": \"").append(e.getMessage()).append("\"}");
        }

        return resposta.toString();
    }

    private static void exibirFormatado(String jsonText) {
        if (jsonText.trim().isEmpty()) {
            System.out.println("Nenhum resultado.");
            return;
        }

        try {
            // Cada linha é um JSON
            String[] linhas = jsonText.split("\n");
            for (String linha : linhas) {
                if (linha.trim().isEmpty()) continue;

                JSONObject obj = new JSONObject(linha);
                String titulo = obj.optString("title", "(sem título)");
                String resumo = obj.optString("abstract", "(sem resumo)");

                System.out.println("Titulo: " + titulo);
                System.out.println("Resumo: " + resumo);
                System.out.println();
            }
        } catch (JSONException e) {
            System.out.println("Erro ao interpretar JSON: " + e.getMessage());
        }
    }
}
