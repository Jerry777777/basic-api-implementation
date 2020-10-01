package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vote")
public class VotePO {
    @Id
    @GeneratedValue
    private Integer id;
    private LocalDateTime localDateTime;
    private Integer num;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserPO user;
    @ManyToOne()
    @JoinColumn(name = "rs_event_id")
    private RsEventPO rsEvent;
}
