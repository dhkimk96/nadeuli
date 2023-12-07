package kr.nadeuli.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@ToString(exclude = {"member", "ansQuestions", "orikkiri", "orikkiriSchedule", "product"})
@Table(name = "ori_sche_mem_chat_fav")
public class OriScheMemChatFav {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ori_sche_mem_chat_fav_id")
    private Long oriScheMemChatFavId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orikkiri_id")
    private Orikkiri orikkiri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orikkiri_schedule_id")
    private OrikkiriSchedule orikkiriSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "oriScheMemChatFav", fetch = FetchType.LAZY)
    private List<AnsQuestion> ansQuestions;

}
