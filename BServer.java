public class BServer {
    public static void main(String[] args) {
        WorkerServer worker = new WorkerServer(23456, true); // true = metade superior
        worker.iniciar();
    }
}
