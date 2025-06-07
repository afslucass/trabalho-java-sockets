public class CServer {
    public static void main(String[] args) {
        WorkerServer worker = new WorkerServer(23457, false); // true = metade superior
        worker.iniciar();
    }
}
