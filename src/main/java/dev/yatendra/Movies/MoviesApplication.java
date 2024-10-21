package dev.yatendra.Movies;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan({ "dev.yatendra.Movies.*" })
@EnableMongoRepositories(basePackages = "dev.yatendra.Movies.movies")
public class MoviesApplication {
    
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        // Log the MongoDB URI before starting the application
        String mongoUri = "mongodb+srv://" +
                dotenv.get("MONGO_USER") + ":" +
                dotenv.get("MONGO_PASSWORD") + "@" +
                dotenv.get("MONGO_CLUSTER") + "/?retryWrites=true&w=majority";
        System.out.println("Resolved MongoDB URI: " + mongoUri);

        SpringApplication.run(MoviesApplication.class, args);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        Dotenv dotenv = Dotenv.load();
        String mongoUri = "mongodb+srv://" +
                dotenv.get("MONGO_USER") + ":" +
                dotenv.get("MONGO_PASSWORD") + "@" +
                dotenv.get("MONGO_CLUSTER");
        
        return new MongoTemplate(MongoClients.create(mongoUri), dotenv.get("MONGO_DATABASE"));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(false).maxAge(3600);
            }
        };
    }
}
