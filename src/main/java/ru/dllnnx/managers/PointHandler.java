package ru.dllnnx.managers;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.Getter;
import lombok.Setter;
import ru.dllnnx.entity.Point;
import ru.dllnnx.validation.Validator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
@ApplicationScoped
@Named("pointHandler")
public class PointHandler implements Serializable {
    private Point point = new Point(0F, 0F, 2F); // дефолтные значения
    private LinkedList<Point> points = new LinkedList<>();

    @Inject
    private PersistenceManager persistenceManager;
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        entityManager = persistenceManager.getEmf().createEntityManager();
        loadPointsFromDb();
    }


    public void loadPointsFromDb() {
        try {
            entityManager.getTransaction().begin();
            points = new LinkedList<>(entityManager.createQuery("SELECT p FROM Point p", Point.class).getResultList());
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void add() {
        long timer = System.nanoTime();
        point.setStartTime(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
        point.setHit(Validator.checkArea(point.getX(), point.getY(), point.getR()));
        point.setScriptTime((long) ((System.nanoTime() - timer) * 0.01));

        this.addPoint(point);
        point = new Point(point.getX(), point.getY(), point.getR());
    }

    public void addPoint(Point point) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(point);
        entityTransaction.commit();

        this.points.addFirst(point);
    }

    public void addFromJS() {
        long timer = System.nanoTime();
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        params.values().forEach(System.out::println);

        try {
            float x = Float.parseFloat(params.get("x"));
            float y = Float.parseFloat(params.get("y"));
            float r = Float.parseFloat(params.get("r"));

            final Point attemptBean = new Point(x, y, r);
            attemptBean.setStartTime(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
            attemptBean.setHit(Validator.checkArea(x, y, r));
            attemptBean.setScriptTime((long) ((System.nanoTime() - timer) * 0.01));

            this.addPoint(attemptBean);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void clear() {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.createQuery("DELETE FROM Point").executeUpdate();
            entityTransaction.commit();

            points.clear();
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }
    }

}
