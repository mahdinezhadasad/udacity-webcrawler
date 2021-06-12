package com.udacity.webcrawler.main;

import com.google.inject.Guice;
import com.udacity.webcrawler.WebCrawler;
import com.udacity.webcrawler.WebCrawlerModule;
import com.udacity.webcrawler.json.ConfigurationLoader;
import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.json.CrawlResultWriter;
import com.udacity.webcrawler.json.CrawlerConfiguration;
import com.udacity.webcrawler.profiler.Profiler;
import com.udacity.webcrawler.profiler.ProfilerModule;
import org.w3c.dom.ls.LSOutput;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Objects;

public final class WebCrawlerMain {

  private final CrawlerConfiguration config;

  private WebCrawlerMain(CrawlerConfiguration config) {
    this.config = Objects.requireNonNull(config);
  }

  @Inject
  private WebCrawler crawler;

  @Inject
  private Profiler profiler;

  private void run() throws Exception {
    Guice.createInjector(new WebCrawlerModule(config), new ProfilerModule()).injectMembers(this);

    CrawlResult crawlResults = crawler.crawl(config.getStartPages());
    CrawlResultWriter resultWriter = new CrawlResultWriter(crawlResults);
    if(config.getResultPath().isEmpty() || config.getResultPath().isBlank()){
      System.out.println("Crawler Result - ");
      resultWriter.write(new OutputStreamWriter(System.out));
    }
    else{
      System.out.println("Crawler Result written to: " + config.getResultPath());
      resultWriter.write(Path.of(config.getResultPath()));
    }
    if(config.getProfileOutputPath().isEmpty() || config.getProfileOutputPath().isBlank()){
      System.out.println("Profiler Result - ");
      profiler.writeData(new OutputStreamWriter(System.out));
    }
    else{
      System.out.println("Profiler Result written to: " + config.getProfileOutputPath());
      profiler.writeData(Path.of(config.getProfileOutputPath()));
    }

  }
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("Usage: WebCrawlerMain [starting-url]");
      return;
    }

    CrawlerConfiguration config = new ConfigurationLoader(Path.of(args[0])).load();
    new WebCrawlerMain(config).run();
  }
}
