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
                PrintWriter saidaCliente = new PrintWriter(clienteSocket.getOutputStream(), true);

                String mensagem = entrada.readLine();
                System.out.println("Consulta recebida: " + mensagem);

                String respostaSuperior = consultarWorker("localhost", 23456, mensagem);
                String respostaInferior = consultarWorker("localhost", 23457, mensagem);

                StringBuilder respostaFinal = new StringBuilder();
                respostaFinal.append("=== Resultados encontrados ===\n\n");

                respostaFinal.append("--- Metade SUPERIOR ---\n");
                respostaFinal.append(formatarResultado(respostaSuperior)).append("\n");

                respostaFinal.append("--- Metade INFERIOR ---\n");
                respostaFinal.append(formatarResultado(respostaInferior)).append("\n");

                respostaFinal.append("================================\n");

                // Envia para o cliente
                saidaCliente.println(respostaFinal.toString());

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

    private static String formatarResultado(String jsonText) {
        StringBuilder sb = new StringBuilder();

        if (jsonText.trim().isEmpty()) {
            sb.append("Nenhum resultado.\n");
            return sb.toString();
        }

        try {
            String[] linhas = jsonText.split("\n");
            for (String linha : linhas) {
                if (linha.trim().isEmpty()) continue;

                JSONObject obj = new JSONObject(linha);
                String titulo = obj.optString("title", "(sem titulo)");
                String resumo = obj.optString("abstract", "(sem resumo)");

                sb.append("Titulo: ").append(titulo).append("\n");
                sb.append("Resumo: ").append(resumo).append("\n\n");
            }
        } catch (JSONException e) {
            sb.append("Erro ao interpretar JSON: ").append(e.getMessage()).append("\n");
        }

        return sb.toString();
    }
}
