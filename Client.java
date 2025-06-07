import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    static private String HOST = "localhost";
    static private int PORT = 12345;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Client <mensagem>");
            return;
        }

        String mensagem = args[0];

        try (Socket socket = new Socket(HOST, PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensagem);
            System.out.println("Mensagem enviada: " + mensagem);
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
