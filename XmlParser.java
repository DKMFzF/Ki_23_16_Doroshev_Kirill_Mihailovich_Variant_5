import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Парсинг и сохранения XML файла
 */

public class XmlParser {
  // Количество потоков читателей
  private int readerThreadCount;

  // Количество потоков писателей
  private int writerThreadCount;

  // Длительность операции чтения
  private int readOperationDuration;

  // Длительность операции записи
  private int writeOperationDuration;

  // Размер буфера
  private int poolSize;

  // Имя файла конфигурации
  private static final String FILENAME = "settings.xml";

  /**
   * @return poolSize (Размер пула)
   */
  public int getPoolSize() {
    return poolSize;
  }

  /**
   * @return readerThreadCount (кол-во потоков читателей.)
   */
  public int getReaderThreadCount() {
    return readerThreadCount;
  }

  /**
   * @return writerThreadCount (кол-во потоков писателей)
   */
  public int getWriterThreadCount() {
    return writerThreadCount;
  }

  /**
   * @return readOperationDuration (длительность операции чтения)
   */
  public int getReadOperationDuration() {
    return readOperationDuration;
  }

  /**
   * @return writeOperationDuration (длительность операции записи)
   */
  public int getWriteOperationDuration() {
    return writeOperationDuration;
  }

  /**
   * @param poolSize новый размер буфера (размер буфера, значение которого должно быть больше 0)
   */
  public void setPoolSize(int poolSize) {
    if (poolSize > 0) {
      this.poolSize = poolSize;
    } else {
      this.poolSize = 1;
      System.out.println("Установите значение размера пула в файле 'settings.xml' больше 0");
    }
  }

  /**
   * @param readerThreadCount новое количество потоков чтения (кол-во потоков читателей, значение должно быть больше 0.)
   */
  public void setReaderThreadCount(int readerThreadCount) {
    if (readerThreadCount > 0) {
      this.readerThreadCount = readerThreadCount;
    } else {
      this.readerThreadCount = 1;
      System.out.println("Установите значение кол-во потоков чтения в файле 'settings.xml' больше 0");
    }
  }

  /**
   * @param writerThreadCount новое количество потоков записи
   */
  public void setWriterThreadCount(int writerThreadCount) {
    this.writerThreadCount = writerThreadCount;
  }

  /**
   * @param readOperationDuration новая длительность операции чтения
   */
  public void setReadOperationDuration(int readOperationDuration) {
    if (readOperationDuration >= 1000) {
      this.readOperationDuration = readOperationDuration;
    } else {
      this.readOperationDuration = 1000;
      System.out.println("Установите значение длительности операции чтения в файле 'settings.xml' не меньше 1000");
    }
  }

  /**
   * @param writeOperationDuration новая длительность операции записи
   */
  public void setWriteOperationDuration(int writeOperationDuration) {
    if (writeOperationDuration >= 1000) {
      this.writeOperationDuration = writeOperationDuration;
    } else {
      this.writeOperationDuration = 1000;
      System.out.println("Установите длительность операции записи в файле 'settings.xml' не меньше 1000");
    }
  }

  /**
   * Проверка на число
   * @param str строка
   * @return boolean
   */

  private boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Загружает настройки из XML файла
   */
  public void readSettings() {
    try {
      File file = new File(FILENAME);
      if (file.exists()) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        Element root = document.getDocumentElement();
        NodeList node;

        node = root.getElementsByTagName("writerThreadCount");
        if (isNumeric(node.item(0).getTextContent())) {
          this.setWriterThreadCount(Integer.parseInt(node.item(0).getTextContent()));
        }

        node = root.getElementsByTagName("readerThreadCount");
        if (isNumeric(node.item(0).getTextContent())) {
          this.setReaderThreadCount(Integer.parseInt(node.item(0).getTextContent()));
        }

        node = root.getElementsByTagName("readOperationDuration");
        if (isNumeric(node.item(0).getTextContent())) {
          this.setReadOperationDuration(Integer.parseInt(node.item(0).getTextContent()));
        }

        node = root.getElementsByTagName("writeOperationDuration");
        if (isNumeric(node.item(0).getTextContent())) {
          this.setWriteOperationDuration(Integer.parseInt(node.item(0).getTextContent()));
        }

        node = root.getElementsByTagName("poolSize");
        if (isNumeric(node.item(0).getTextContent())) {
          this.setPoolSize(Integer.parseInt(node.item(0).getTextContent()));
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(Arrays.toString(e.getStackTrace()));
    }
  }

  /**
   * Сохраняет настройки в XML файл.
   */
  public void saveSettings() {
    String content =
      "<settings>\n" +
        "  <writerThreadCount>" + this.getWriterThreadCount() + "</writerThreadCount>\n" +
        "  <readerThreadCount>" + this.getReaderThreadCount() + "</readerThreadCount>\n" +
        "  <readOperationDuration>" + this.getReadOperationDuration() + "</readOperationDuration>\n" +
        "  <writeOperationDuration>" + this.getWriteOperationDuration() + "</writeOperationDuration>\n" +
        "  <poolSize>" + this.getPoolSize() + "</poolSize>\n" +
        "</settings>";

    try (FileWriter writer = new FileWriter(FILENAME)) {
      writer.write(content);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.out.println(Arrays.toString(e.getStackTrace()));
    }
  }
}