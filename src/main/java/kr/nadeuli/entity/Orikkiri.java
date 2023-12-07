package kr.nadeuli.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@ToString
@Table(name = "orikkiri")
public class Orikkiri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orikkiri_id")
    private Long orikkiriId;

    @Column(name = "dong_ne", nullable = false, length = 255)
    private String dongNe;

    @Column(name = "orikkiri_name", nullable = false, length = 255)
    private String orikkiriName;

    @Column(name = "orikkiri_picture", nullable = false, length = 255)
    private String orikkiriPicture;

    @Column(name = "orikkiri_introduction", nullable = false, length = 255)
    private String orikkiriIntroduction;

    @Column(name = "orikkiri_regist_time", nullable = false)
    private LocalDateTime orikkiriRegistTime;

    @Column(name = "master_tag", nullable = false, length = 20)
    private String masterTag;

    @OneToMany(mappedBy = "orikkiri", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AnsQuestion> ansQuestions;

    @OneToMany(mappedBy = "orikkiri", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "orikkiri", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrikkiriSchedule> orikkiriSchedules;

    @OneToMany(mappedBy = "orikkiri", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OriScheMemChatFav> oriScheMemChatFavs;
}
