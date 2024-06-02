/**
 * Класс для чтения файлов из потока буф пула
 */

public class Reader extends Thread {
  // Время выполнения операции
  public int operationDuration;

  // Объект буферного пула
  private final BufferPool bufferPool;

  public Reader(BufferPool bufferPool, int operationDuration) {
    this.bufferPool = bufferPool;
    this.operationDuration = operationDuration;
  }

  /**
   * Запускает поток для выполнения операций чтения.
   * Читает данные из буфера, если они доступны, и выводит их.
   * Обновляет счетчики доступных ячеек для чтения и записи.
   * Если буфер пуст, выводит сообщение "Ждите, так как буфер пока пуст".
   */
  public void run() {
    try {
      while (!isInterrupted()) {
        while (bufferPool.getReadCount() > 0) {
          synchronized (this.bufferPool) {
            Object data = bufferPool.readFromBuffer();
            if (data != null) {
              System.out.println(Thread.currentThread().getName() + ": Прочитано из буферного пула: " + data);
              bufferPool.setReadCount(bufferPool.getReadCount() - 1);
              bufferPool.setWriteCount(bufferPool.getWriteCount() + 1);
            }
          }
          Thread.sleep(operationDuration);
        }
        System.out.println("Ждите так как буфер пока пуст.");
        Thread.sleep(operationDuration);
      }
    } catch (InterruptedException e) {
      System.out.println(Thread.currentThread().getName() + ": Прервано.");
    }
  }
}