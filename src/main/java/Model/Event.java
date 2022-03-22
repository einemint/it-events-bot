package Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity()
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Integer id;

    @Column
    @NotNull
    @Setter
    @Getter
    private String type;

    @Column
    @NotNull
    @Setter
    @Getter
    private String title;

    @Column
    @NotNull
    @Setter
    @Getter
    private String date;

    @Column
    @Setter
    @Getter
    private String location;

    @Column
    @Setter
    @Getter
    private String online;

    @Column
    @NotNull
    @Setter
    @Getter
    private String link;
}
