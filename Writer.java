import java.security.SecureRandom;

/**
 * Класс для потоков, выполняющих операции записи в буферный пул.
 * Хранит длительность операции и ссылку на буферный пул.
 */

public class Writer extends Thread {
  // Время выполнения операции
  public int operationDuration;

  // Объект буферного пула
  private final BufferPool bufferPool;

  public Writer(BufferPool bufferPool, int operationDuration) {
    this.bufferPool = bufferPool;
    this.operationDuration = operationDuration;
  }

  /**
   * МЕТОД ГЕНЕРАЦИИ
   * @param length длина генерируемой строки
   * @return сгенерированная случайная строка
   */
  public static String createRandomString(int length) {
    final String chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789";
    StringBuilder sb = new StringBuilder(length);
    SecureRandom random = new SecureRandom();
    for (int i = 0; i < length; i++) {
      sb.append(chars.charAt(random.nextInt(chars.length())));
    }
    return sb.toString();
  }

  /**
   * Запускает поток для выполнения операций записи.
   * Записывает данные в буфер, пока есть свободные ячейки, и обновляет счетчики.
   * Если буфер заполнен, выводит сообщение "Пожалуйста, подождите. Буфер заполнен".
   */
  public void run() {
    try {
      while (!isInterrupted()) {
        while (bufferPool.getWriteCount() > 0) {
          synchronized (this.bufferPool) {
            String randomString = createRandomString(15);
            bufferPool.writeToBuffer(randomString);
            System.out.println(Thread.currentThread().getName() + ": Записано в буферный пул: " + randomString);
            bufferPool.setReadCount(bufferPool.getReadCount() + 1);
            bufferPool.setWriteCount(bufferPool.getWriteCount() - 1);
          }
          Thread.sleep(operationDuration);
        }
        System.out.println("Ждите так как буфер пока заполнен.");
        Thread.sleep(operationDuration);
      }
    } catch (InterruptedException e) {
      System.out.println(Thread.currentThread().getName() + ": Прервано");
    }
  }
}