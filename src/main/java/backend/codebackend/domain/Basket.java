package backend.codebackend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //항목 이름
    @Column(nullable = false)
    private Long chatroom_id;    //채팅방 번호
    @Column(length = 20, nullable = false)
    private String product_name;     // 항목 이름
    @Column(length = 10, nullable = false)
    private int price;           // 가격
    @Column(nullable = false)
    private int quantity;        // 수량
    @Column(length = 10, nullable = false)
    private String nickname;     // 파티원(누가 어느 것을 시켰는지 알기위해 해당 사람을 추적하는데 필요)
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created_at;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

    public Basket(Long id, Long chatroom_id, String product_name, int price, int quantity, String nickname, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.chatroom_id = chatroom_id;
        this.product_name = product_name;
        this.price = price;
        this.quantity = quantity;
        this.nickname = nickname;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
