package ru.dllnnx.managers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Named("persistenceManager")
@ApplicationScoped
@Getter
public class PersistenceManager {

    private EntityManagerFactory emf;

    @PostConstruct
    public void init(){
        emf = this.updateEntityManagerFactory();
    }

    public EntityManagerFactory updateEntityManagerFactory() {
        if (emf == null) {
            try (InputStream input = PersistenceManager.class.getClassLoader().getResourceAsStream("db.properties")) {
                Properties properties = new Properties();
                if (input == null) {
                    throw new IllegalArgumentException("Sorry, unable to find db.properties");
                }
                properties.load(input);

                Map<String, String> persistenceProps = new HashMap<>();
                persistenceProps.put("jakarta.persistence.jdbc.user", properties.getProperty("db.username"));
                persistenceProps.put("jakarta.persistence.jdbc.password", properties.getProperty("db.password"));

                emf = Persistence.createEntityManagerFactory("Points", persistenceProps);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Failed to load database configuration", ex);
            }
        }
        return emf;
    }
}
