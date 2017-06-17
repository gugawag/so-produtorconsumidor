import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Alterações feitas por Gustavo Wagner, 15/07/2016
 */
public class ProdutorConsumidorProfessor {
    static Object crunchifyLock = new Object();
    static LinkedList<String> buffer = new LinkedList<String>();

    // Semaphore mantem a gerência das posições do buffer.
    // semaphore.acquire() é o mesmo que down; semaphore.release() é o mesmo que up.

    //semaphore está sendo utilizado para trabalhar com as posições do buffer
    //cada s
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

                    mutex.acquire();
                    buffer.add(threadName);
                    mutex.release();
                    System.out.println("Produtor produziu: " + threadName);


                    // release lock
                    semaphore.release();
                    Thread.sleep(500);
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

    // I'll be consuming Integer every stime
    static class Consumidor extends Thread {
        String consumerName;

        public Consumidor(String name) {
            this.consumerName = name;
        }

        public void run() {
            try {

                while (true) {

                    // acquire lock. Acquires the given number of permits from this semaphore, blocking until all are
                    // available
                    // process stops here until producer releases the lock
                    semaphore.acquire();

                    // Acquires a permit from this semaphore, blocking until one is available
                    mutex.acquire();
                    String result = "";
                    for (String value : buffer) {
                        result = value + ",";
                    }
                    mutex.release();
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