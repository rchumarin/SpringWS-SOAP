package ru.chumarin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;
import ru.chumarin.configuration.ApplicationConfig;
import ru.chumarin.ws.findnumber.GetFilenameRequest;
import ru.chumarin.ws.findnumber.GetFilenameResponse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTests {

    private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

    @LocalServerPort
    private int port = 0;

    @Autowired
    ApplicationConfig config;

    private static final int FILE_COUNT = 20;
    private static final String FILE_NAME_WITHOUT_EXSTENSION = "AvgNumbers";
    private static Logger Log = LogManager.getLogger(ApplicationIntegrationTests.class);


    private volatile int oneRandomNumber;

    @Before
    public void before() throws Exception {
        marshaller.setPackagesToScan(ClassUtils.getPackageName(GetFilenameRequest.class));
        marshaller.afterPropertiesSet();
        createNFiles(FILE_COUNT, FILE_NAME_WITHOUT_EXSTENSION);
    }

    @After
    public void after() throws IOException {
        for (int i = 1; i <= FILE_COUNT; i++) {
            Path filePath = Paths.get(config.getFileDir()).resolve(FILE_NAME_WITHOUT_EXSTENSION + "_" + i + ".txt");
            deleteFile(filePath);
        }
    }

    @Test
    public void testSendAndReceive() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        GetFilenameRequest request = new GetFilenameRequest();
        request.setNumber(oneRandomNumber);

        GetFilenameResponse response = (GetFilenameResponse) ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);

        assertThat(response).isNotNull();
    }

    private void createNFiles(int threadPoolSize, String filename) {
        Paths.get(config.getFileDir()).toFile().mkdir();
        ExecutorService service = Executors.newFixedThreadPool(threadPoolSize);
        for (int i = 1; i <= threadPoolSize; i++) {
            int finalI = i;
            service.execute(() -> createFile(Paths.get(config.getFileDir()).resolve(filename + "_" + finalI + ".txt").toString()));
        }
        service.shutdown();
        try {
            service.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Log.error("tasks interrupted");
        }
        finally {
            if (!service.isTerminated()) {
                Log.error("cancel non-finished tasks");
            }
            service.shutdownNow();
            Log.info("shutdown finished");
        }
    }

    private void createFile(String pathName) {
        Random random = new Random();
        File file = new File(pathName);
        long start = System.currentTimeMillis();

        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")), false)) {
            int counter = 0;
            while (true) {
                String sep = "";
                for (int i = 0; i < 100; i++) {
                    int number = random.nextInt(Integer.MAX_VALUE);
                    if (oneRandomNumber == 0) {
                        oneRandomNumber = number;
                    }
                    writer.print(sep);
                    writer.print(number);
                    sep = ",";
                }
                counter++;
                // Проверяем, является ли текущий размер тем, что мы хотим
                if (counter == 20000) {
                    Log.info(String.format(Thread.currentThread().getName() + ". Size: %.3f GB%n\n", file.length() / 1e9));
                    if (file.length() >= config.getFileSize() * 1e9) {
                        writer.close();
                        break;
                    } else {
                        counter = 0;
                    }
                }
            }
            long time = System.currentTimeMillis() - start;
            Log.info(String.format(Thread.currentThread().getName() + ". Took %.1f seconds to create a file of %.3f GB\n", time / 1e3, file.length() / 1e9));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            Log.error(e.getMessage());
        }
    }

    private void deleteFile(Path path) throws IOException {
        if (path.toFile().exists()) {
            Files.delete(path);
        }
    }
}