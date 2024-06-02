/**
 * Класс пула буферов <br>
 * Методы пула: <br>
 * writeToBuffer() - записывает данные в буферный пул <br>
 * readFromBuffer() - читает данные из буферного пула <br>
 */

public class BufferPool {
  // пул буферов
  private Object[] bufferList;

  // Количество свободных ячеек для чтения
  private int readCount;

  // Количество свободных ячеек для записи
  private int writeCount;

  // Размер пула
  private int bufferSize;

  public BufferPool(int size) {
    setBufferSize(size);
    this.bufferList = new Object[getBufferSize()];
    setReadCount(0);
    setWriteCount(getBufferSize());
  }

  /**
   * @return прочитанный объект из буфера
   */
  public Object readFromBuffer() {
    for (int i = 0; i < getBufferSize(); i++) {
      if (this.bufferList[i] != null) {
        Object data = this.bufferList[i];
        this.bufferList[i] = null;
        return data;
      }
    }
    return null;
  }

  /**
   * @param size размер буфера
   */
  public void setBufferSize(int size) {
    this.bufferSize = size;
  }

  /**
   * @return размер буфера
   */
  public int getBufferSize() {
    return this.bufferSize;
  }

  /**
   * @param count кол-во ячеек для чтения
   */
  public void setReadCount(int count) {
    this.readCount = count;
  }

  /**
   * @return кол-во ячеек для чтения
   */
  public int getReadCount() {
    return this.readCount;
  }

  /**
   * @param count кол-во ячеек для записи
   */
  public void setWriteCount(int count) {
    this.writeCount = count;
  }

  /**
   * @return кол-во ячеек для записи
   */
  public int getWriteCount() {
    return this.writeCount;
  }

  /**
   * @param data данные для записи в буфер
   */
  public void writeToBuffer(Object data) {
    // getWriteCount = -1
    this.bufferList[getWriteCount() % getBufferSize()] = data;
  }
}