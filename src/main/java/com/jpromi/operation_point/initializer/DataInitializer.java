package com.jpromi.operation_point.initializer;

import com.jpromi.operation_point.entity.CrawlService;
import com.jpromi.operation_point.repository.CrawlServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CrawlServiceRepository crawlServiceRepository;

    @Value("${com.jpromi.operation_point.file.storage.path}")
    private String fileStoragePath;

    @Autowired
    public DataInitializer(CrawlServiceRepository crawlServiceRepository) {
        this.crawlServiceRepository = crawlServiceRepository;
    }

    @Override
    public void run(final String... args) {
        initCrawlServices();
        initDataFolder();
    }

    private void initCrawlServices() {
        List<CrawlService> crawlServices = new ArrayList<>();
        crawlServices.add(CrawlService.builder().name("fs-la").friendlyName("Lower Austria").build());
        crawlServices.add(CrawlService.builder().name("fs-ua").friendlyName("Upper Austria").build());
        crawlServices.add(CrawlService.builder().name("fs-bl").friendlyName("Burgenland").build());
        crawlServices.add(CrawlService.builder().name("fs-st").friendlyName("Styria").build());
        crawlServices.add(CrawlService.builder().name("fs-ty").friendlyName("Tyrol").build());

        // not implemented
        crawlServices.add(CrawlService.builder().name("fs-ct").friendlyName("Carinthia").enabled(false).build());
        crawlServices.add(CrawlService.builder().name("fs-sb").friendlyName("Salzburg").enabled(false).build());
        crawlServices.add(CrawlService.builder().name("fs-vi").friendlyName("Vienna").enabled(false).build());
        crawlServices.add(CrawlService.builder().name("fs-vb").friendlyName("Vorarlberg").enabled(false).build());

        for (CrawlService crawlService : crawlServices) {
            if (crawlServiceRepository.findByName(crawlService.getName()) == null) {
                crawlServiceRepository.save(crawlService);
            }
        }
    }

    private void initDataFolder() {
        File file = new File(fileStoragePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
