import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class WorkerServer {

    private int porta;
    private boolean metadeSuperior;
    private List<JSONObject> baseDados;

    public WorkerServer(int porta, boolean metadeSuperior) {
        this.porta = porta;
        this.metadeSuperior = metadeSuperior;
        carregarBaseDados();
    }

    private void carregarBaseDados() {
        try {
            String conteudo = new String(java.nio.file.Files.readAllBytes(
                    java.nio.file.Paths.get("dados_servidor_c.json")));
            JSONArray jsonArray = new JSONArray(conteudo);

            int meio = jsonArray.length() / 2;
            baseDados = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                if ((metadeSuperior && i >= meio) || (!metadeSuperior && i < meio)) {
                    baseDados.add(jsonArray.getJSONObject(i));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            baseDados = new ArrayList<>();
        }
    }

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Worker " + (metadeSuperior ? "Superior" : "Inferior") + " escutando na porta " + porta);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> tratarCliente(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tratarCliente(Socket socket) {
        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String termo = entrada.readLine();
            for (JSONObject obj : baseDados) {
                if (obj.getString("title").toLowerCase().contains(termo.toLowerCase()) ||
                    obj.getString("abstract").toLowerCase().contains(termo.toLowerCase())) {
                    saida.println(obj.toString());
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
