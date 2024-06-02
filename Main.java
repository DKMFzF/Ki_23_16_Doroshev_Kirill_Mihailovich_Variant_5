import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.util.logging.Logger;

// Main - класс запуска

/**
 * Вариант 5 <br>
 * author: Дорошев Кирилл Михайлович
 * group: КИ23-16/1Б
 */

public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    // Кол-во потоков писателей
    int writerThreadCount;

    // Кол-во потоков читателей
    int readerThreadCount;

    // Длительность операций чтения
    int readOperationDuration;

    // Длительность операций записи
    int writeOperationDuration;

    // Размер пула
    int poolSize;

    // Список потоков читателей
    ArrayList<Reader> readers = new ArrayList<Reader>();

    // Список потоков писателей
    ArrayList<Writer> writers = new ArrayList<Writer>();
    Scanner scanner = new Scanner(System.in);
    XmlParser xmlParser = new XmlParser();

    System.out.println("""
                  -----------------------------------------------------------------
                  | Вам нужно ввести некоторые данные для запуска программы.      |
                  |                                                               |
                  | Выберите опцию:                                               |
                  | (1) Загрузка из XML. Запуск программы.                        |
                  | (2) Ручной ввод настроек с последующим сохранением в XML файл |
                  -----------------------------------------------------------------
                  """);

    System.out.print("Введите: ");

    int choice = 0;
    while (true) {
      try {
        choice = scanner.nextInt();
        if (choice < 1 || choice > 2) {
          throw new InputMismatchException();
        }
        break;
      } catch (InputMismatchException e) {
        System.out.println("Введите: ");
        scanner.nextLine(); // Очистить ввод
      }
    }

    switch (choice) {
      // Работа с существующим файлом
      case 1:
        File file = new File("settings.xml");

        // ПРОВЕРКИ РАБОТЫ С ФАЙЛОМ
        if (file.exists()) {
          // Загрузка настроек из XML файла
          xmlParser.readSettings();

          boolean isValid = true;
          if (xmlParser.getWriterThreadCount() < 1) {
            LOGGER.warning("Некорректное значение кол-во потоков писателей в XML");
            isValid = false;
          }

          if (xmlParser.getReaderThreadCount() < 1) {
            System.out.println("Некорректное значение кол-во потоков читателей в XML");
            isValid = false;
          }

          if (xmlParser.getReadOperationDuration() < 1000) {
            LOGGER.warning("Некорректное значение длительности операций чтения в XML");
            isValid = false;
          }

          if (xmlParser.getWriteOperationDuration() < 1000) {
            LOGGER.warning("Некорректное значение длительности операций записи в XML");
            isValid = false;
          }

          if (xmlParser.getPoolSize() < 1) {
            LOGGER.warning("Некорректное значение размера пула в XML");
            isValid = false;
          }

          // Если не прошел проверки переход на врочной ввод
          if (!isValid) {
            LOGGER.warning("Переход к вводу данных вручную, тк есть некорректное значение в xml-файле");
            choice = 2;
          } else {
            break;
          }

        } else {
          LOGGER.warning("Файл settings.xml не найден. Пожалуйста, выберите операцию 2 для ввода данных вручную.");
          choice = 2; // Сменить выбор на опцию 2
        }
        // Пропустить break для выполнения следующего case
        if (choice == 1) break;

      // Ввести данные вручную и сохранить в XML файл
      case 2:
        while (true) {
          try {
            System.out.println("Ввод кол-во потоков писателей");
            System.out.println("ЛИМИТЫ: больше 0 и меньше 2^31");
            System.out.println("Введите: ");
            writerThreadCount = scanner.nextInt();
            if (writerThreadCount < 1) {
              throw new InputMismatchException();
            }
            break;
          } catch (InputMismatchException e) {
            LOGGER.warning("Некорректное значение. Попробуйте еще раз.");
            scanner.nextLine(); // Очистить ввод
          }
        }

        while (true) {
          try {
            System.out.println("Ввод кол-во потоков читателей");
            System.out.println("ЛИМИТЫ: больше 0 и меньше 2^31");
            System.out.println("Введите: ");
            readerThreadCount = scanner.nextInt();
            if (readerThreadCount < 1) {
              throw new InputMismatchException();
            }
            break;
          } catch (InputMismatchException e) {
            LOGGER.warning("Некорректное значение. Попробуйте еще раз.");
            scanner.nextLine(); // Очистить ввод
          }
        }

        while (true) {
          try {
            System.out.println("Введите длительность операций чтения:");
            System.out.println("ЛИМИТЫ: не менее 1000 и меньше 2^31");
            System.out.println("Введите: ");
            readOperationDuration = scanner.nextInt();
            if (readOperationDuration < 1000) {
              throw new InputMismatchException();
            }
            break;
          } catch (InputMismatchException e) {
            LOGGER.warning("Некорректное значение. Попробуйте еще раз.");
            scanner.nextLine(); // Очистить ввод
          }
        }

        while (true) {
          try {
            System.out.println("Введите длительность операций записи:");
            System.out.println("ЛИМИТЫ: не менее 1000 и меньше 2^31");
            System.out.println("Введите: ");
            writeOperationDuration = scanner.nextInt();
            if (writeOperationDuration < 1000) {
              throw new InputMismatchException();
            }
            break;
          } catch (InputMismatchException e) {
            LOGGER.warning("Некорректное значение. Попробуйте еще раз.");
            scanner.nextLine(); // Очистить ввод
          }
        }

        while (true) {
          try {
            System.out.println("Введите размер пула:");
            System.out.println("ЛИМИТЫ: больше 0 и меньше 2^31");
            System.out.println("Введите: ");
            poolSize = scanner.nextInt();
            if (poolSize < 1) {
              throw new InputMismatchException();
            }
            break;
          } catch (InputMismatchException e) {
            LOGGER.warning("Некорректное значение. Попробуйте еще раз.");
            scanner.nextLine(); // Очистить ввод
          }
        }

        xmlParser.setWriterThreadCount(writerThreadCount);
        xmlParser.setReaderThreadCount(readerThreadCount);
        xmlParser.setReadOperationDuration(readOperationDuration);
        xmlParser.setWriteOperationDuration(writeOperationDuration);
        xmlParser.setPoolSize(poolSize);

        xmlParser.saveSettings();
        xmlParser.readSettings();
        break;
    }

    BufferPool bufferPool = new BufferPool(xmlParser.getPoolSize());
    for (int i = 0; i < xmlParser.getWriterThreadCount(); i++) {
      writers.add(new Writer(bufferPool, xmlParser.getWriteOperationDuration()));
      writers.get(i).setName("Писатель #" + i);
    }

    for (int i = 0; i < xmlParser.getReaderThreadCount(); i++) {
      readers.add(new Reader(bufferPool, xmlParser.getReadOperationDuration()));
      readers.get(i).setName("Читатель #№" + i);
    }

    for (Writer writer : writers) {
      writer.start();
    }
    for (Reader reader : readers) {
      reader.start();
    }
  }
}