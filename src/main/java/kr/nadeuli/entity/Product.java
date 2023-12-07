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
@ToString(exclude = {"seller", "buyer"})
@Table(name = "product")
public class Product extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "video")
    private String video;

    @Column(name = "view_num", nullable = false)
    private Long viewNum;

    @Column(name = "is_bargain", nullable = false, columnDefinition = "boolean default false")
    private boolean isBargain;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "is_sold", nullable = false, columnDefinition = "boolean default false")
    private boolean isSold;

    @Column(name = "is_premium", nullable = false, columnDefinition = "boolean default false")
    private boolean isPremium;

    @Column(name = "premium_time")
    private Long premiumTime;

    @Column(name = "trading_location", nullable = false)
    private String tradingLocation;

    @Column(name = "gu", nullable = false)
    private String gu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_tag", referencedColumnName = "tag")
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_tag", referencedColumnName = "tag")
    private Member buyer;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<TradeReview> tradeReviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TradeSchedule> tradeSchedules;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<NadeuliPayHistory> nadeuliPayHistories;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Image> images;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Report> reports;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OriScheMemChatFav> oriScheMemChatFavs;

}