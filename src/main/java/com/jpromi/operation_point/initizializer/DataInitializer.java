package com.jpromi.operation_point.initizializer;

import com.jpromi.operation_point.enitiy.CrawlService;
import com.jpromi.operation_point.repository.CrawlServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CrawlServiceRepository crawlServiceRepository;

    @Override
    public void run(String... args) throws Exception {
        initCrawlServices();
    }

    private void initCrawlServices() {
        List<CrawlService> crawlServices = new ArrayList<>();
        crawlServices.add(CrawlService.builder().name("fs-la").friendlyName("Lower Austria").build());
        crawlServices.add(CrawlService.builder().name("fs-ua").friendlyName("Upper Austria").build());
        crawlServices.add(CrawlService.builder().name("fs-bl").friendlyName("Burgenland").build());
        crawlServices.add(CrawlService.builder().name("fs-st").friendlyName("Styria").build());
        crawlServices.add(CrawlService.builder().name("fs-ty").friendlyName("Tyrol").build());

        // not implemented
        crawlServices.add(CrawlService.builder().name("fs-ct").friendlyName("Carinthia").isEnabled(false).build());
        crawlServices.add(CrawlService.builder().name("fs-sb").friendlyName("Salzburg").isEnabled(false).build());
        crawlServices.add(CrawlService.builder().name("fs-vi").friendlyName("Vienna").isEnabled(false).build());
        crawlServices.add(CrawlService.builder().name("fs-vb").friendlyName("Vorarlberg").isEnabled(false).build());

        for (CrawlService crawlService : crawlServices) {
            if (crawlServiceRepository.findByName(crawlService.getName()) == null) {
                crawlServiceRepository.save(crawlService);
            }
        }
    }
}
