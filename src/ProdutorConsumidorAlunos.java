import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Alterações feitas por Gustavo Wagner, 15/07/2016
 */
public class ProdutorConsumidorAlunos {
    static Object lock = new Object();
    static LinkedList<String> buffer = new LinkedList<String>();

    // Semaphore mantem a gerência das posições do buffer.
    // semaphore.acquire() é o mesmo que down; semaphore.release() é o mesmo que up.

    //semaphore está sendo utilizado para trabalhar com as posições do buffer
    //cada valor enviado pelo semáforo pode ser consumido
    static Semaphore semaphore = new Semaphore(0);
    //mutex está sendo utilizado para controlar a entrada na região crítica
    static Semaphore mutex = new Semaphore(1);

    // I'll producing new Integer every time
    static class Produtor extends Thread {
        public void run() {

            int counter = 1;
            try {
                while (true) {
                    String threadName = Thread.currentThread().getName() + counter++;


                    buffer.add(threadName);
                    System.out.println("Produtor produziu: " + threadName);


                    Thread.sleep(500);
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

    // Consumirei inteiros a todo momento
    static class Consumidor extends Thread {
        String consumerName;

        public Consumidor(String name) {
            this.consumerName = name;
        }

        public void run() {
            try {

                while (true) {


                    String result = "";
                    for (String value : buffer) {
                        result = value + ",";
                    }
                    System.out.println(consumerName + " consumiu valor: " + result + " Tamanho do buffer: "
                            + buffer.size() + "\n");


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Produtor().start();
        new Consumidor("IFPB").start();
    }
}