package ru.dllnnx.entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import org.primefaces.event.SlideEndEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Named("abstractPoint")
@ApplicationScoped
@Entity
@Table(name = "points")
public class Point implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coordinate_x")
    private Float x;
    @Column(name = "coordinate_y")
    private Float y;
    @Column(name = "radius")
    private Float r;
    @Column(name = "is_hit")
    private boolean isHit;
    @Column(name = "start_time")
    private String startTime;
    @Column(name = "script_time")
    private long scriptTime;

    public Point(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void onSlideEndR(SlideEndEvent event) {
        this.r = (float) event.getValue();
    }

    public void onSlideEndX(SlideEndEvent event) {
        this.x = (float) event.getValue();
    }


    public String isHitToString() {
        return isHit ? "yes!" : "no(";
    }

    public String getIsHitHTMLClass() {
        return isHit ? "success" : "fail";
    }

    @Override
    public String toString() {
        return x + " " + y + " " + r;
    }
}
