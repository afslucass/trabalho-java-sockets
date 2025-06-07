import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java SocketClient <mensagem>");
            return;
        }

        String mensagem = args[0];

        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(mensagem);

            // Lê a resposta formatada do servidor
            String linha;
            while ((linha = in.readLine()) != null) {
                System.out.println(linha);
            }

        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}
